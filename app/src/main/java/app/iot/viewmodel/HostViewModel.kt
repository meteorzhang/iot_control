package app.iot.viewmodel

import android.os.Bundle
import android.view.View
import app.iot.AppConstant
import app.iot.model.BindDetail
import app.iot.ui.*
import app.iot.widget.dialog.SearchDialog
import app.iot.widget.dialog.SelectBLEDialog
import com.inuker.bluetooth.library.beacon.BeaconItem

class HostViewModel : BaseViewModel() {

    fun install(view: View) {
        startActivity(BindActivity::class.java)
    }

    fun unbind(view: View) {
        //选择运输装备或设备
        SearchDialog(
            context as BaseActivity<*>,
            "查找装备设备",
            object : SearchDialog.SearchListener {
                override fun onSearched(bindDetail: BindDetail) {
                    super.onSearched(bindDetail)
                    //检查设备是否存在或设备没有绑定过运输装备
                    val bundle = Bundle()
                    bundle.putSerializable(AppConstant.SERIALIZABLE, bindDetail)
                    startActivity(UnbindActivity::class.java, bundle)
                }
            }).show()
    }

    fun modify(view: View) {
        //选择运输装备或设备
        SearchDialog(
            context as BaseActivity<*>,
            "查找装备设备",
            object : SearchDialog.SearchListener {
                override fun onSearched(bindDetail: BindDetail) {
                    super.onSearched(bindDetail)
                    //检查设备是否存在或设备没有绑定过运输装备
                    val bundle = Bundle()
                    bundle.putSerializable(AppConstant.SERIALIZABLE, bindDetail)
                    startActivity(SwitchActivity::class.java, bundle)
                }
            }).show()
    }

    fun check(view: View) {
        SelectBLEDialog(
            context!!,
            "检测设备",
            null,
            object : SelectBLEDialog.OnSelectListener<BeaconItem> {
                override fun onSelect(t: BeaconItem?) {
                    super.onSelect(t)
                    val bundle = Bundle()
                    bundle.putByteArray(AppConstant.BYTES, t?.bytes)
                    startActivity(DeviceDetailActivity::class.java, bundle)
                }
            }
        ).show()
    }
}