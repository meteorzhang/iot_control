package app.iot.viewmodel

import android.view.View
import android.widget.AdapterView
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import app.iot.R
import app.iot.common.util.LogUtils
import app.iot.dataInfo
import app.iot.model.DeviceModel
import app.iot.model.DeviceType
import app.iot.model.EquipmentModel
import app.iot.model.EquipmentType
import app.iot.ui.BindActivity
import app.iot.ui.fragment.EquipBindFragment
import app.iot.widget.toast.CommonToast
import kotlinx.android.synthetic.main.fragment_equip.view.*

class EquipViewModel : BaseViewModel() {
    //装备类型
    var equipTypes = MutableLiveData<List<EquipmentType>>().apply {
        value = dataInfo?.dictionaryData?.equipmentType
    }

    //装备型号(联动装备类型)
    var equipModels = MutableLiveData<List<EquipmentModel>>().apply {
        //选择存储的下标
        value =
            dataInfo?.dictionaryData?.equipmentType?.get(BindActivity.equipModelIndex)?.equipmentModel
    }

    val equipTypeSelectListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            BindActivity.equipTypeIndex = position

            //联动装备型号改变
            equipModels.value = equipTypes.value?.get(position)?.equipmentModel

            if (equipModels.value.isNullOrEmpty()) {
                BindActivity.equipModelIndex = 0
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            BindActivity.equipTypeIndex = 0
        }

    }
    val equipModelSelectListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            BindActivity.equipModelIndex = position
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            BindActivity.equipModelIndex = 0
        }
    }

    /*********************************************/
    //设备类型
    var deviceTypes = MutableLiveData<List<DeviceType>>().apply {
        value = dataInfo?.dictionaryData?.deviceType
    }

    //设备型号(联动设备类型)
    var deviceModels = MutableLiveData<List<DeviceModel>>().apply {
        value =
            dataInfo?.dictionaryData?.deviceType?.get(BindActivity.deviceModelIndex)?.deviceModel
    }

    val deviceTypeSelectListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            BindActivity.deviceTypeIndex = position
            //联动
            deviceModels.value = deviceTypes.value?.get(position)?.deviceModel

            if (deviceModels.value.isNullOrEmpty()) {
                BindActivity.deviceModelIndex = 0
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            BindActivity.deviceTypeIndex = 0
        }
    }
    val deviceModelSelectListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            BindActivity.deviceModelIndex = position
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            BindActivity.deviceModelIndex = 0
        }
    }

    fun commit(view: View) {
        if(equipTypes.value.isNullOrEmpty()){
            CommonToast.middleShow("装备类型为空")
            return
        }
        val selectedEquipType = equipTypes.value!![BindActivity.equipTypeIndex]

        if(selectedEquipType.equipmentModel.isNullOrEmpty()){
            CommonToast.middleShow("装备型号为空")
            return
        }
        val selectedEquipModel = selectedEquipType.equipmentModel[BindActivity.equipModelIndex]

        if(deviceTypes.value.isNullOrEmpty()){
            CommonToast.middleShow("设备类型为空")
            return
        }
        val selectedDeviceType = deviceTypes.value!![BindActivity.deviceTypeIndex]

        if(selectedDeviceType.deviceModel.isNullOrEmpty()){
            CommonToast.middleShow("设备型号为空")
            return
        }
        val selectedDeviceModel = selectedDeviceType.deviceModel[BindActivity.deviceModelIndex]

        startFragment(
            EquipBindFragment(
                selectedEquipType,
                selectedEquipModel,
                selectedDeviceType,
                selectedDeviceModel,
                null
            ), R.id.container
        )
        (context as BindActivity).mViewModel?.titleViewModel?.title?.value = "设备安装绑定"
    }
}