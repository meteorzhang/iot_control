package app.iot.viewmodel

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import app.iot.library.log.entity.OperateRecord
import app.iot.model.DeviceType
import app.iot.model.ModelDetail
import app.iot.net.request.DeviceModelDetail
import app.iot.ui.BindActivity
import app.iot.widget.dialog.SelectDeviceDialog

/**
 * Created by danbo on 2019-11-07.
 */
class BindDeviceItemViewModel(
    private val deviceModelDetail: ModelDetail,
    private val bindDetail: DeviceModelDetail?
) : BaseViewModel() {

    val modelDetail = MutableLiveData<ModelDetail>().apply {
        value = deviceModelDetail
    }

    //选择的deviceNo
    val deviceNo = MutableLiveData<String>().apply {
        value = bindDetail?.deviceNo
    }

    val isSelected = ObservableField<Boolean>().apply {
        if (bindDetail != null) {//log record
            set(true)
            return@apply
        }
        set(false)
    }

    fun selectDevice(view: View) {
        if (bindDetail != null) {//log record
            return
        }

        SelectDeviceDialog(
            context as BindActivity,
            "添加${deviceModelDetail.defaultName}设备",
            "添加",
            deviceModelDetail.componentCode,
            object : SelectDeviceDialog.SelectDeviceListener {
                override fun onSelected(deviceNo: String) {
                    setDevice(deviceNo)
                }
            }).show()
    }

    private fun setDevice(deviceNo: String) {
        this.deviceNo.value = deviceNo

        isSelected.set(true)
    }
}