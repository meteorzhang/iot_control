package app.iot.viewmodel

import android.content.Intent
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import app.iot.protocol.ProtocolUtil
import app.iot.model.BindDetail
import app.iot.net.RetrofitProvider
import app.iot.net.response.BasicResponse
import app.iot.net.subscribers.DefaultObserver
import app.iot.ui.BaseActivity
import app.iot.widget.dialog.BaseDialog
import app.iot.widget.dialog.BaseDialog.Companion.SCAN_DEVICE
import app.iot.widget.dialog.BaseDialog.Companion.SCAN_EQUIP
import app.iot.widget.dialog.SearchDialog
import app.iot.widget.dialog.SelectBLEDialog
import app.iot.widget.toast.CommonToast
import com.inuker.bluetooth.library.beacon.BeaconItem
import com.xuexiang.xqrcode.XQRCode
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SearchViewModel(private val activity: BaseActivity<*>, private val dialog: SearchDialog) :
    BaseViewModel(),
    BaseActivity.ActivityResultListener {

    val title = MutableLiveData<String>().apply {
        value = dialog.title
    }

    //搜索的deviceNo
    var equipNo = ObservableField<String>()

    //搜索的deviceNo
    var deviceNo = ObservableField<String>()

    fun bleEquipNo(view: View) {
        SelectBLEDialog(
            activity,
            "搜索装备",
            null,
            object : SelectBLEDialog.OnSelectListener<BeaconItem> {
                override fun onSelect(t: BeaconItem?) {
                    super.onSelect(t)
                    equipNo.set(ProtocolUtil.getDeviceNo(t?.bytes))
                }
            }).show()
    }

    fun scanEquipNo(view: View) {
        //注册监听器
        activity.listener = this

        val intent = Intent(XQRCode.ACTION_DEFAULT_CAPTURE)
        activity.startActivityForResult(
            intent,
            BaseDialog.REQUEST_CODE_SCAN_EQUIP
        )
    }

    fun bleDeviceNo(view: View) {
        SelectBLEDialog(
            activity,
            "搜索设备",
            null,
            object : SelectBLEDialog.OnSelectListener<BeaconItem> {
                override fun onSelect(t: BeaconItem?) {
                    super.onSelect(t)
                    deviceNo.set(ProtocolUtil.getDeviceNo(t?.bytes))
                }
            }).show()
    }

    fun scanDeviceNo(view: View) {
        //注册监听器
        activity.listener = this

        val intent = Intent(XQRCode.ACTION_DEFAULT_CAPTURE)
        activity.startActivityForResult(
            intent,
            BaseDialog.REQUEST_CODE_SCAN_DEVICE
        )
    }

    override fun onScanResult(type: Int, result: String?) {
        if (type == SCAN_EQUIP) {//装备
            equipNo.set(result)
        } else if (type == SCAN_DEVICE) {//设备
            deviceNo.set(result)
        }
    }

    fun search(view: View) {
        activity.let {
            RetrofitProvider.getApiService()
                .bindDetail(equipNo.get(), deviceNo.get())
                .compose((it).bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : DefaultObserver<BasicResponse<BindDetail>>(it, true) {
                    override fun onSuccess(response: BasicResponse<BindDetail>) {
                        if (response.data == null) {
                            CommonToast.middleShow("未绑定!")
                            return
                        }
                        response.data?.let {
                            dialog.listener.onSearched(it)
                            dialog.dismiss()
                        }
                    }

                })
        }
    }
}