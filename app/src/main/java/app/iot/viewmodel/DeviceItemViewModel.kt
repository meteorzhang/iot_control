package app.iot.viewmodel

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import app.iot.model.DeviceBindDetail

/**
 * Created by danbo on 2019-11-07.
 */
class DeviceItemViewModel(
    deviceBindDetail: DeviceBindDetail?,
    val listener: ItemRemoveListener?
) : BaseViewModel() {

    val deviceType = MutableLiveData<String>().apply {
        value = deviceBindDetail?.deviceName
    }

    val deviceModel = MutableLiveData<String>().apply {
        value = deviceBindDetail?.deviceNo
    }

    val showClose = MutableLiveData<Boolean>().apply {
        value = listener != null
    }

    fun remove(view: View?) {
        listener?.onRemove(this)
    }

}

interface ItemRemoveListener {
    fun onRemove(item: DeviceItemViewModel)
}