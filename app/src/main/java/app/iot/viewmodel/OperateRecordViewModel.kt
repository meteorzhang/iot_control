package app.iot.viewmodel

import android.app.Dialog
import android.graphics.Color
import app.iot.R
import app.iot.common.util.LogUtils
import app.iot.library.log.db.DaoManager
import app.iot.ui.OperateRecordActivity
import app.iot.ui.fragment.OperateRecordFragment
import app.iot.widget.dialog.DoubleButtonDialog
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import java.util.*


class OperateRecordViewModel : BaseViewModel(), OnTitleClickListener {

    val titleViewModel = TopBarViewModel("操作记录", R.drawable.ic_nav_back, "清空", this)

    override fun onLogoClick() {
        super.onLogoClick()
        finish()
    }

    override fun onMenuClick() {
        super.onMenuClick()
        if (titleViewModel.menu.value == "清空") {
            selectDate()//弹出时间
        } else {
//            DoubleButtonDialog(
//                context!!,
//                null,
//                "提示",
//                "确认恢复历史操作记录吗?",
//                "取消",
//                "确认",
//                object : DoubleButtonDialog.DialogClickListener {
//                    override fun onClick(dialog: Dialog) {
//                        super.onClick(dialog)
//                        DaoManager.instance().restoreAll()
//                        //刷新
//                        (context as OperateRecordActivity).apply {
//                            for (f in this.fragments) {
//                                (f as OperateRecordFragment).mViewModel?.init()
//                            }
//                        }
//
//                    }
//                }
//            ).show()
        }
    }

    /**
     * 选择时间
     */
    fun selectDate() {
        val selectedDate: Calendar = Calendar.getInstance()
        val startDate: Calendar = Calendar.getInstance()
        val endDate: Calendar = Calendar.getInstance()
        // 设置起始时间 当年年份少3年 如2020 - 3 = 2017
        startDate.set(endDate.get(Calendar.YEAR) - 3, 0, 1)
        // 根据系统当前时间做最后时间
        endDate.set(
            endDate.get(Calendar.YEAR),
            endDate.get(Calendar.MONTH),
            endDate.get(Calendar.DATE)
        )

        var pvTime =
            TimePickerBuilder(titleViewModel.context, OnTimeSelectListener { date, v -> //选中事件回调
                LogUtils.e("我是选择的时间-" + date.time)
                delOperateAll(date.time)
            })
                .setType(booleanArrayOf(true, true, true, false, false, false))
                .setCancelText("取消") //取消按钮文字
                .setSubmitText("确认") //确认按钮文字
                .setContentTextSize(18) //滚轮文字大小
                .setTitleSize(20) //标题文字大小
                .setTitleText("选择时间") //标题文字
                .setTitleColor(Color.BLACK) //标题文字颜色
                .setSubmitColor(Color.BLACK) //确定按钮文字颜色
                .setCancelColor(Color.BLACK) //取消按钮文字颜色
                .setTitleBgColor(Color.WHITE) //标题背景颜色 Night mode
                .setBgColor(Color.WHITE) //滚轮背景颜色 Night mode
                .setDate(selectedDate) // 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate, endDate)
                .setLabel("年", "月", "日", "时", "分", "秒") //默认设置为年月日时分秒
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(true) //是否显示为对话框样式
                .build()
        pvTime.show()
    }

    /**
     * 清空某个时间段之前的数据
     * befor = 确认清空所有操作记录吗
     * now = 确认清空该时间段之前的操作记录吗
     */
    fun delOperateAll(time: Long) {
        DoubleButtonDialog(
            context!!,
            null,
            "提示",
            "确认清空该时间段之前的操作记录吗?",
            "取消",
            "确认",
            object : DoubleButtonDialog.DialogClickListener {
                override fun onClick(dialog: Dialog) {
                    super.onClick(dialog)
                    DaoManager.instance().deleteDateAll(time)
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
