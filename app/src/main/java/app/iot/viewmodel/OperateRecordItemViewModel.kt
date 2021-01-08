package app.iot.viewmodel

import android.app.Dialog
import android.view.View
import androidx.databinding.ObservableField
import app.iot.AppConstant.ACCOUNT
import app.iot.AppConstant.OPERATE_RECORD
import app.iot.library.log.db.DaoManager
import app.iot.library.log.entity.OperateRecord
import app.iot.library.log.entity.OperateType
import app.iot.ui.*
import app.iot.ui.fragment.OperateRecordFragment
import app.iot.widget.dialog.DoubleButtonDialog
import app.iot.widget.toast.CommonToast

/**
 * Created by danbo on 2019-11-07.
 */
class OperateRecordItemViewModel(
    private val record: OperateRecord
) : BaseViewModel() {

    val account = ObservableField<String>().apply {
        set(SPUtils.getData(ACCOUNT, "") as String + " ")
    }
    val item = ObservableField<OperateRecord>().apply {
        set(record)
    }
    val info = ObservableField<String>().apply {

        when (record.type) {
            OperateType.BIND.value -> {//绑定只有装备编号
                record.equipNo?.let {
                    set("装备编号:${it}")
                }
            }
            OperateType.UNBIND.value -> {//解绑只装备编号
                record.equipNo?.let {
                    set("装备编号:${it}")
                }

            }
            OperateType.SWITCH.value -> {//更换有装备编号和设备编号
                var text: String? = ""
                record.equipNo?.let {
                    text = "装备编号:$it \n"
                }

                record.deviceNo?.let {
                    text = "${text}设备编号:$it"
                }
                set(text)
            }
            OperateType.CHECK.value -> {
                record.deviceNo?.let {
                    set("设备编号:${it}")
                }
            }
        }
    }

    fun detail(view: View) {
        when (record.type) {
            OperateType.BIND.value -> {
                startActivity(BindActivity::class.java, OPERATE_RECORD, record)
            }
            OperateType.UNBIND.value -> {
                startActivity(UnbindActivity::class.java, OPERATE_RECORD, record)
            }
            OperateType.SWITCH.value -> {
                startActivity(SwitchActivity::class.java, OPERATE_RECORD, record)
            }
            OperateType.CHECK.value -> {
                startActivity(DeviceDetailActivity::class.java, OPERATE_RECORD, record)
            }
        }
    }

    fun detailLong(view: View): Boolean {
        DoubleButtonDialog(
            context!!,
            null,
            "提示",
            "确定删除此条记录吗?",
            "取消",
            "确认",
            object : DoubleButtonDialog.DialogClickListener {
                override fun onClick(dialog: Dialog) {
                    super.onClick(dialog)
                    DaoManager.instance().delete(record)
                    //刷新
                    (context as OperateRecordActivity).apply {
                        for (f in this.fragments) {
                            (f as OperateRecordFragment).initView()
                        }
                    }

                }
            }
        ).show()
        return true
    }
}