package app.iot.viewmodel

import SPUtils
import android.app.Dialog
import android.content.Intent
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import app.iot.AppConstant.ACCOUNT
import app.iot.R
import app.iot.library.log.db.DaoManager
import app.iot.library.log.entity.OperateRecord
import app.iot.library.log.entity.OperateType
import app.iot.model.DeviceModel
import app.iot.model.DeviceType
import app.iot.model.EquipmentModel
import app.iot.model.EquipmentType
import app.iot.net.RetrofitProvider
import app.iot.net.request.BindRequest
import app.iot.net.request.DeviceModelDetail
import app.iot.net.response.BasicResponse
import app.iot.net.subscribers.DefaultObserver
import app.iot.protocol.ProtocolUtil
import app.iot.ui.BaseActivity
import app.iot.ui.BindActivity
import app.iot.ui.fragment.EquipFragment
import app.iot.widget.dialog.BaseDialog
import app.iot.widget.dialog.DoubleButtonDialog
import app.iot.widget.dialog.SelectBLEDialog
import app.iot.widget.toast.CommonToast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.inuker.bluetooth.library.beacon.BeaconItem
import com.xuexiang.xqrcode.XQRCode
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class EquipBindViewModel : BaseViewModel(), OnTitleClickListener,
    BaseActivity.ActivityResultListener {

    val isLog = ObservableField<Boolean>().apply {
        set(false)
    }
    val logRemark = ObservableField<String>()

    var equipType: EquipmentType? = null
    var equipModel: EquipmentModel? = null
    var deviceType: DeviceType? = null
    var deviceModel: DeviceModel? = null

    //运输装备编号
    var equipNo = MutableLiveData<String>().apply {
        value = ""
    }

    val isEmpty = ObservableField<Boolean>().apply {
        set(true)
    }
    val emptyViewModel = EmptyViewModel("无设备列表", null)

    var recyclerViewViewModel: RecyclerViewViewModel<BindDeviceItemViewModel> =
        RecyclerViewViewModel(
            null,
            R.layout.layout_bind_device_item,
            RecyclerViewViewModel.VERTICAL_LINEARLAYOUTMANAGER, 1
        )

    fun init(
        equipType: EquipmentType,
        equipModel: EquipmentModel,
        deviceType: DeviceType,
        deviceModel: DeviceModel,
        record: OperateRecord?
    ) {
        this.equipType = equipType
        this.equipModel = equipModel
        this.deviceType = deviceType
        this.deviceModel = deviceModel

        val deviceModelDetails: List<DeviceModelDetail>? = if (record != null) {
            Gson().fromJson(
                record.content,
                object : TypeToken<List<DeviceModelDetail?>?>() {}.type
            )
        } else {
            null
        }
        deviceModel.modelDetail?.let {
            for (modelDetail in it) {//设备详情列表
                var bindDetail: DeviceModelDetail? = null
                deviceModelDetails?.let {
                    for (item in it) {
                        if (modelDetail.id == item.id) {
                            bindDetail = item
                        }
                    }
                }

                val itemViewModel =
                    BindDeviceItemViewModel(modelDetail, bindDetail)
                itemViewModel.context = context
                recyclerViewViewModel.item.add(itemViewModel)
            }

            isEmpty.set(recyclerViewViewModel.item.isEmpty())
        }

        record?.let {
            isLog.set(true)

            //
            equipNo.value = record.equipNo
            logRemark.set(record.remark)
        }

    }

    fun selectEquipNo(view: View) {
        if (isLog.get()!!) {
            return
        }
        SelectBLEDialog(
            context!!,
            "装备列表",
            equipType?.codeKeyName,
            object : SelectBLEDialog.OnSelectListener<BeaconItem> {
                override fun onSelect(t: BeaconItem?) {
                    super.onSelect(t)
                    equipNo.value = ProtocolUtil.getDeviceNo(t?.bytes)
                }
            }).show()
    }

    fun scanEquipNo(view: View) {
        if (isLog.get()!!) {
            return
        }
        (context as BindActivity).let {//扫描数据开始注册监听器
            it.listener = this

            val intent = Intent(XQRCode.ACTION_DEFAULT_CAPTURE)
            it.startActivityForResult(
                intent,
                BaseDialog.REQUEST_CODE_SCAN_EQUIP
            )
        }

    }

    override fun onScanResult(type: Int, result: String?) {
        (context as BindActivity).listener = null

        //扫码装备号结果
        equipNo.value = result
    }

    fun commit(view: View) {
        if (isLog.get()!!) {
            return
        }

        for (item in recyclerViewViewModel.item) {
            if (item.isSelected.get() == false) {
                CommonToast.middleShow("请先选择设备")
                return
            }
        }
        //提交绑定
        (context as BaseActivity<*>).let {
            val deviceModelDetail = arrayListOf<DeviceModelDetail>()
            for (item in recyclerViewViewModel.item) {
                val deviceModelDetailItem =
                    DeviceModelDetail(item.modelDetail.value?.id, item.deviceNo.value)
                deviceModelDetail.add(deviceModelDetailItem)
            }

            val bindRequest = BindRequest(
                equipType?.codeKey,
                equipModel?.id,
                equipNo.value,
                "",
                deviceType?.codeKey,
                deviceModel?.id,
                deviceModelDetail
            )

            RetrofitProvider.getApiService()
                .bind(bindRequest.getRequestBody())
                .compose(it.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : DefaultObserver<BasicResponse<Any>>(it, true) {
                    override fun onSuccess(response: BasicResponse<Any>) {
                        //记录日志
                        val bindRecord = OperateRecord(
                            null, SPUtils.getData(ACCOUNT, "") as String,
                            OperateType.BIND.value, true,
                            Gson().toJson(deviceModelDetail),
                            Gson().toJson(equipType),
                            Gson().toJson(equipModel),
                            Gson().toJson(deviceType),
                            Gson().toJson(deviceModel),
                            equipNo.value,
                            System.currentTimeMillis(), "绑定成功!"
                        )
                        DaoManager.instance().insertOrReplace(bindRecord)

                        DoubleButtonDialog(
                            context!!,
                            R.drawable.ic_success,
                            null,
                            "设备绑定完成! 是否继续?",
                            "退出",
                            "继续绑定",
                            object : DoubleButtonDialog.DialogClickListener {
                                override fun onClick(dialog: Dialog) {
                                    super.onClick(dialog)
                                    startFragment(EquipFragment(), R.id.container)
                                }

                                override fun onCancel(dialog: Dialog) {
                                    super.onCancel(dialog)
                                    finish()
                                }
                            }
                        ).show()
                    }

                    override fun onFail(response: BasicResponse<Any>) {
                        super.onFail(response)
                        //记录日志
                        val bindRecord = OperateRecord(
                            null, SPUtils.getData(ACCOUNT, "") as String,
                            OperateType.BIND.value, false,
                            Gson().toJson(deviceModelDetail),
                            Gson().toJson(equipType),
                            Gson().toJson(equipModel),
                            Gson().toJson(deviceType),
                            Gson().toJson(deviceModel),
                            equipNo.value,
                            System.currentTimeMillis(), response.message
                        )
                        DaoManager.instance().insertOrReplace(bindRecord)
                    }

                    override fun onException(reason: ExceptionReason) {
                        super.onException(reason)
                        //记录日志
                        val bindRecord = OperateRecord(
                            null, SPUtils.getData(ACCOUNT, "") as String,
                            OperateType.BIND.value, false,
                            Gson().toJson(deviceModelDetail),
                            Gson().toJson(equipType),
                            Gson().toJson(equipModel),
                            Gson().toJson(deviceType),
                            Gson().toJson(deviceModel),
                            equipNo.value,
                            System.currentTimeMillis(), "网络错误!"
                        )
                        DaoManager.instance().insertOrReplace(bindRecord)
                    }

                })
        }
    }

    override fun onLogoClick() {
        super.onLogoClick()
        finish()
    }

}

