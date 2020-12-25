package app.iot.widget.dialog

import android.app.Dialog
import android.content.Context
import android.view.View
import app.iot.R
import kotlinx.android.synthetic.main.layout_single_dialog.btn_ok
import kotlinx.android.synthetic.main.layout_single_dialog.iv_info
import kotlinx.android.synthetic.main.layout_single_dialog.tv_info
import kotlinx.android.synthetic.main.layout_single_dialog.tv_title


/**
 * Created by danbo on 2019/11/13.
 *
 * 单个按钮的对话框
 */
class SingleButtonDialog(
    context: Context,
    private val logo: Int?,
    private val title: String?,
    private val info: String,
    private val okText: String,
    private var listener: DialogClickListener?
) : BaseDialog(context), View.OnClickListener {

    override val layoutId: Int
        get() = R.layout.layout_single_dialog


    public override fun initView() {
        btn_ok!!.setOnClickListener(this)

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

        btn_ok.text = okText
    }

    override fun onClick(view: View) {
        dismiss()
        if (listener != null) {
            listener?.onOk(this)
        }
    }

    interface DialogClickListener {
        fun onOk(dialog: Dialog){}
    }

}
