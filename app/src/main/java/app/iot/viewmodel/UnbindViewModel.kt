package app.iot.viewmodel

import android.app.Dialog
import android.view.View
import androidx.databinding.ObservableField
import app.iot.*
import app.iot.library.log.db.DaoManager
import app.iot.library.log.entity.OperateRecord
import app.iot.library.log.entity.OperateType
import app.iot.model.BindDetail
import app.iot.net.RetrofitProvider
import app.iot.net.request.OutRequest
import app.iot.net.response.BasicResponse
import app.iot.net.subscribers.DefaultObserver
import app.iot.ui.BaseActivity
import app.iot.widget.dialog.DoubleButtonDialog
import app.iot.widget.toast.CommonToast
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class UnbindViewModel : BaseViewModel(), OnTitleClickListener {
    val isLog = ObservableField<Boolean>().apply {
        set(false)
    }
    val logRemark = ObservableField<String>()

    val titleViewModel = TopBarViewModel("设备解绑", R.drawable.ic_nav_back, "", this)

    val bindDetail = ObservableField<BindDetail>()

    val isEmpty = ObservableField<Boolean>().apply {
        set(true)
    }

    var recyclerViewViewModel: RecyclerViewViewModel<DeviceItemViewModel> = RecyclerViewViewModel(
        null,
        R.layout.layout_device_item,
        RecyclerViewViewModel.VERTICAL_LINEARLAYOUTMANAGER, 1
    )

    fun init(record: OperateRecord) {
        this.isLog.set(true)
        logRemark.set(record.remark)

        val bd = Gson().fromJson(record.content, BindDetail::class.java)
        init(bd)
    }

    fun init(bindDetail: BindDetail) {
        this.bindDetail.set(bindDetail)

        if (bindDetail.deviceBindDetailList == null) {
            return
        }
        for (deviceItem in bindDetail.deviceBindDetailList) {
            val itemViewModel =
                DeviceItemViewModel(deviceItem, null)
            itemViewModel.context = context
            recyclerViewViewModel.item.add(itemViewModel)
        }

    }

    fun unbind(view: View) {
        if (isLog.get()!!) {
            return
        }
        DoubleButtonDialog(
            context!!,
            null,
            "提示",
            "确认解除设备绑定?",
            "取消",
            "确认",
            object : DoubleButtonDialog.DialogClickListener {
                override fun onClick(dialog: Dialog) {
                    super.onClick(dialog)
                    requestUnbind()
                }
            }
        ).show()

    }

    private fun requestUnbind() {
        //解除绑定
        (context as BaseActivity<*>).let {
            val outRequest = OutRequest(
                bindDetail.get()?.id
            )
            RetrofitProvider.getApiService()
                .out(outRequest.getRequestBody())
                .compose(it.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : DefaultObserver<BasicResponse<Any>>(it, true) {
                    override fun onSuccess(response: BasicResponse<Any>) {
                        CommonToast.middleShow("解绑完成")
                        //记录日志
                        val outRecord = OperateRecord(
                            null,
                            SPUtils.getData(AppConstant.ACCOUNT, "") as String,
                            OperateType.UNBIND.value,
                            true,
                            Gson().toJson(bindDetail.get()),
                            bindDetail.get()?.equipmentNo,
                            null,
                            System.currentTimeMillis(),
                            "解绑完成"
                        )
                        DaoManager.instance().insertOrReplace(outRecord)

                        finish()
                    }

                    override fun onFail(response: BasicResponse<Any>) {
                        super.onFail(response)
                        //记录日志
                        val outRecord = OperateRecord(
                            null,
                            SPUtils.getData(AppConstant.ACCOUNT, "") as String,
                            OperateType.UNBIND.value,
                            false,
                            Gson().toJson(bindDetail.get()),
                            bindDetail.get()?.equipmentNo,
                            null,
                            System.currentTimeMillis(),
                            response.message
                        )
                        DaoManager.instance().insertOrReplace(outRecord)
                    }

                    override fun onException(reason: ExceptionReason) {
                        super.onException(reason)
                        //记录日志
                        val outRecord = OperateRecord(
                            null,
                            SPUtils.getData(AppConstant.ACCOUNT, "") as String,
                            OperateType.UNBIND.value,
                            false,
                            Gson().toJson(bindDetail.get()),
                            bindDetail.get()?.equipmentNo,
                            null,
                            System.currentTimeMillis(),
                            "网络错误!"
                        )
                        DaoManager.instance().insertOrReplace(outRecord)
                    }
                })
        }
    }

    override fun onLogoClick() {
        super.onLogoClick()
        finish()
    }

}
