package app.iot.viewmodel

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import app.iot.AppConstant
import app.iot.library.log.db.DaoManager
import app.iot.library.log.entity.OperateRecord
import app.iot.library.log.entity.OperateType
import app.iot.model.BindDetail
import app.iot.model.DeviceBindDetail
import app.iot.net.RetrofitProvider
import app.iot.net.request.ExchangeRequest
import app.iot.net.response.BasicResponse
import app.iot.net.subscribers.DefaultObserver
import app.iot.ui.BaseActivity
import app.iot.ui.SwitchActivity
import app.iot.widget.dialog.SelectDeviceDialog
import app.iot.widget.toast.CommonToast
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by danbo on 2019-11-07.
 *
 * 已绑定设备元素
 */
class DeviceBindItemViewModel(
    private val bindDetail: BindDetail,
    private val deviceBindDetail: DeviceBindDetail,
    private val record: OperateRecord?
) : BaseViewModel() {
    val isLog = ObservableField<Boolean>().apply {
        if (record != null) {
            set(true)
        } else {
            set(false)
        }
    }
    val logDeviceNo = ObservableField<String>().apply {
        if (record != null) {
            if (deviceBindDetail.deviceNo == record.deviceNo) {
                set(record.newDeviceNo)
            }
        }
    }

    val deviceDetail = MutableLiveData<DeviceBindDetail>().apply {
        value = deviceBindDetail
    }

    //选择的deviceNo
    val deviceNo = MutableLiveData<String>().apply {
        value = deviceBindDetail.deviceNo
    }

    fun selectDevice(view: View) {
        if (isLog.get()!!) {
            return
        }
        //TODO 更换设备选择设备过滤
        SelectDeviceDialog(
            context as SwitchActivity,
            "更换${deviceBindDetail.deviceName}设备",
            "更换",
            deviceBindDetail.componentCode,
            object : SelectDeviceDialog.SelectDeviceListener {
                override fun onSelected(deviceNo: String) {
                    bindDevice(deviceNo)
                }
            }).show()
    }

    private fun bindDevice(newDeviceNo: String) {
        val exchangeRequest = ExchangeRequest(newDeviceNo, deviceBindDetail.id)
        (context as BaseActivity<*>).let {
            RetrofitProvider.getApiService()
                .exchange(exchangeRequest.getRequestBody())
                .compose(it.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : DefaultObserver<BasicResponse<Any>>(it, true) {
                    override fun onSuccess(response: BasicResponse<Any>) {
                        CommonToast.middleShow("更换绑定完成")
                        //记录日志
                        val bindRecord = OperateRecord(
                            null,
                            SPUtils.getData(AppConstant.ACCOUNT, "") as String,
                            OperateType.SWITCH.value,
                            true,
                            Gson().toJson(bindDetail),
                            bindDetail.equipmentNo,
                            deviceBindDetail.deviceNo,
                            newDeviceNo,
                            System.currentTimeMillis(),
                            "更换绑定完成!"
                        )
                        DaoManager.instance().insertOrReplace(bindRecord)

                        //只有更换完成才绑定设备编号
                        this@DeviceBindItemViewModel.deviceNo.value = newDeviceNo
                    }

                    override fun onFail(response: BasicResponse<Any>) {
                        super.onFail(response)
                        //记录日志
                        val bindRecord = OperateRecord(
                            null,
                            SPUtils.getData(AppConstant.ACCOUNT, "") as String,
                            OperateType.SWITCH.value,
                            false,
                            Gson().toJson(bindDetail),
                            bindDetail.equipmentNo,
                            deviceBindDetail.deviceNo,
                            newDeviceNo,
                            System.currentTimeMillis(),
                            response.message
                        )
                        DaoManager.instance().insertOrReplace(bindRecord)
                    }

                    override fun onException(reason: ExceptionReason) {
                        super.onException(reason)
                        //记录日志
                        val bindRecord = OperateRecord(
                            null,
                            SPUtils.getData(AppConstant.ACCOUNT, "") as String,
                            OperateType.SWITCH.value,
                            false,
                            Gson().toJson(bindDetail),
                            bindDetail.equipmentNo,
                            deviceBindDetail.deviceNo,
                            newDeviceNo,
                            System.currentTimeMillis(),
                            "网络错误!"
                        )
                        DaoManager.instance().insertOrReplace(bindRecord)
                    }
                })
        }
    }
}