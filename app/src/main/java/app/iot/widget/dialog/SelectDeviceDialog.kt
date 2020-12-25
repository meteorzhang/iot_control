package app.iot.widget.dialog

import android.content.Intent
import android.view.View
import app.iot.R
import app.iot.protocol.ProtocolUtil
import app.iot.ui.BaseActivity
import app.iot.widget.toast.CommonToast
import com.inuker.bluetooth.library.beacon.BeaconItem
import com.xuexiang.xqrcode.XQRCode
import kotlinx.android.synthetic.main.layout_select_device_dialog.*

/**
 * Created by danbo on 2019/11/13.
 *
 * 选择设备对话框
 */
class SelectDeviceDialog(
    private val activity: BaseActivity<*>,
    private val title: String,
    private val ok: String,
    private val protocolPrefix: String?,//协议前缀过滤
    private var listener: SelectDeviceListener?
) : BaseDialog(activity), View.OnClickListener, BaseActivity.ActivityResultListener {

    override val layoutId: Int
        get() = R.layout.layout_select_device_dialog

    public override fun initView() {
        activity.listener = this

        dialog_title.text = title
        btn_ok.text = ok

        ble_model.setOnClickListener(this)
        scan_model.setOnClickListener(this)

        btn_ok!!.setOnClickListener(this)
        btn_cancel.setOnClickListener(this)

        this.setOnCancelListener {
            listener?.onCancel()
        }

    }

    override fun onClick(view: View) {
        if (listener != null) {
            when (view.id) {
                R.id.ble_model -> {//设备型号
                    SelectBLEDialog(
                        activity,
                        "设备列表",
                        protocolPrefix,
                        object : SelectBLEDialog.OnSelectListener<BeaconItem> {
                            override fun onSelect(t: BeaconItem?) {
                                super.onSelect(t)
                                device_model.setText(ProtocolUtil.getDeviceNo(t?.bytes))
                            }
                        }).show()
                }
                R.id.scan_model -> {
                    val intent = Intent(XQRCode.ACTION_DEFAULT_CAPTURE)
                    activity.startActivityForResult(
                        intent,
                        REQUEST_CODE_SCAN_DEVICE
                    )
                }
                R.id.btn_cancel -> {
                    listener?.onCancel()
                    dismiss()
                }
                R.id.btn_ok -> {
                    if (!device_model.text.isNullOrEmpty()) {
                        listener?.onSelected(
                            device_model.text.toString()
                        )
                        dismiss()
                    } else {
                        CommonToast.middleShow("请输入设备信息")
                    }
                }
            }

        }
    }

    interface SelectDeviceListener {
        //返回选择的装备编号或设备编号
        fun onSelected(result: String)
        fun onCancel() {}
    }

    override fun onScanResult(type: Int, result: String?) {
        //设备
        device_model.setText(result)
    }

}
