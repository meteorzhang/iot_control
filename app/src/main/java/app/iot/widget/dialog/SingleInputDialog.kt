package app.iot.widget.dialog

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import app.iot.R
import kotlinx.android.synthetic.main.layout_single_input_dialog.*

/**
 * Created by danbo on 2019/11/13.
 *
 * 单个输入框的弹框
 */
class SingleInputDialog(
    context: Context,
    private val title: String?,
    private val hint: String,
    private val cancelText: String,
    private val okText: String,
    private var listener: SingleInputListener?
) : BaseDialog(context), View.OnClickListener {

    private var gravity: Int = Gravity.CENTER

    override val layoutId: Int
        get() = R.layout.layout_single_input_dialog


    public override fun initView() {
        btn_ok!!.setOnClickListener(this)
        btn_cancel.setOnClickListener(this)

        title?.let {
            tv_title.text = title
        }

        tv_input.hint = hint

        tv_input.gravity = this.gravity

        btn_cancel.text = cancelText
        btn_ok.text = okText
    }

    override fun onClick(view: View) {
        dismiss()
        if (listener != null) {
            when (view.id) {
                R.id.btn_cancel -> {
                    listener?.onCancel(this)
                }
                R.id.btn_ok -> {
                    listener?.onClick(tv_input.text.toString(), this)
                }
            }

        }
    }

    interface SingleInputListener {
        fun onClick(message: String?, dialog: Dialog){}
        fun onCancel(dialog: Dialog){}
    }

}
