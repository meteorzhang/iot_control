package app.iot.viewmodel

import android.app.Dialog
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.databinding.ObservableField
import app.iot.*
import app.iot.common.util.ImeUtil
import app.iot.library.log.db.DaoManager
import app.iot.ui.OperateRecordActivity
import app.iot.ui.fragment.OperateRecordFragment
import app.iot.widget.SimpleTextWatcher
import app.iot.widget.dialog.DoubleButtonDialog

class OperateRecordViewModel : BaseViewModel(), OnTitleClickListener {

    val titleViewModel = TopBarViewModel("操作记录", R.drawable.ic_nav_back, "清空", this)

    override fun onLogoClick() {
        super.onLogoClick()
        finish()
    }

    override fun onMenuClick() {
        super.onMenuClick()
        if (titleViewModel.menu.value == "清空") {
            DoubleButtonDialog(
                context!!,
                null,
                "提示",
                "确认清空所有操作记录吗?",
                "取消",
                "确认",
                object : DoubleButtonDialog.DialogClickListener {
                    override fun onClick(dialog: Dialog) {
                        super.onClick(dialog)
                        DaoManager.instance().deleteAll()
                        //刷新
                        (context as OperateRecordActivity).apply {
                            for (f in this.fragments) {
                                (f as OperateRecordFragment).mViewModel?.init()
                            }
                        }
                    }
                }
            ).show()
        } else {
            DoubleButtonDialog(
                context!!,
                null,
                "提示",
                "确认恢复历史操作记录吗?",
                "取消",
                "确认",
                object : DoubleButtonDialog.DialogClickListener {
                    override fun onClick(dialog: Dialog) {
                        super.onClick(dialog)
                        DaoManager.instance().restoreAll()
                        //刷新
                        (context as OperateRecordActivity).apply {
                            for (f in this.fragments) {
                                (f as OperateRecordFragment).mViewModel?.init()
                            }
                        }

                    }
                }
            ).show()
        }
    }
}
