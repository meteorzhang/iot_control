package app.iot.widget.dialog

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import app.iot.R
import kotlinx.android.synthetic.main.layout_double_dialog.*

/**
 * Created by danbo on 2019/11/13.
 *
 * 两个按钮的对话框
 */
class DoubleButtonDialog(
    context: Context,
    private val logo: Int?,
    private val title: String?,
    private val info: String,
    private val cancelText: String,
    private val okText: String,
    private var listener: DialogClickListener?
) : BaseDialog(context), View.OnClickListener {

    private var gravity: Int = Gravity.CENTER

    constructor(
        context: Context,
        logo: Int?,
        title: String?,
        gravity: Int,
        info: String,
        cancelText: String,
        okText: String,
        listener: DialogClickListener?
    ) : this(context, logo, title, info, cancelText, okText, listener) {
        this.gravity = gravity
    }

    override val layoutId: Int
        get() = R.layout.layout_double_dialog


    public override fun initView() {
        btn_ok!!.setOnClickListener(this)
        btn_cancel.setOnClickListener(this)

        if (logo == null) {
            iv_info.visibility = View.GONE
        }
        logo?.let {
            iv_info.setImageResource(logo)
        }
        title?.let {
            tv_title.text = title
        }
//        if (title != null) {
//            tv_info.setTextColor(Color.parseColor("#999999"))
//        } else {
//            tv_info.setTextColor(Color.parseColor("#000000"))
//        }

        tv_info.text = info

        tv_info.gravity = this.gravity

        btn_cancel.text = cancelText
        btn_ok.text = okText

        this.setOnCancelListener {
            listener?.onCancel(this)
        }
    }

    override fun onClick(view: View) {
        dismiss()
        if (listener != null) {
            when (view.id) {
                R.id.btn_cancel -> {
                    listener?.onCancel(this)
                }
                R.id.btn_ok -> {
                    listener?.onClick(this)
                }
            }

        }
    }

    interface DialogClickListener {
        fun onClick(dialog: Dialog){}
        fun onCancel(dialog: Dialog){}
    }

}
