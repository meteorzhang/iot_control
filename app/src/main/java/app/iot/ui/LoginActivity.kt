package app.iot.ui

import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import app.iot.AppConfig
import app.iot.BR
import app.iot.R
import app.iot.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity<LoginViewModel>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun initView() {
        mBinding.setVariable(BR.data, mViewModel?.titleViewModel)
        mViewModel?.titleViewModel?.context = mBinding.root.context
        but_change.setOnClickListener {
            showRenameDialog()
        }
    }


    //自定义dialog,自定义重命名dialog
    fun showRenameDialog() {
        val dialog = Dialog(this)
        val view: View = LayoutInflater.from(this).inflate(R.layout.dialog, null)
        val confirm: TextView //确定按钮
        val cancel: TextView //取消按钮
        val confirm_again: TextView //取消按钮
        val edit_address: EditText //确定按钮
        cancel = view.findViewById(R.id.cancel)
        confirm = view.findViewById(R.id.confirm)
        edit_address = view.findViewById(R.id.edit_address)
        //显示数据
        dialog.setContentView(view)
        cancel.setOnClickListener {
            dialog.dismiss()
            SPUtils.saveData(AppConfig.BASE_URL_REPLACE, "")
        }
        confirm.setOnClickListener {
            // 设置本次请求网络的链接,使用一个临时的变量存储
            SPUtils.saveData(AppConfig.BASE_URL_REPLACE, edit_address.text.toString())
            dialog.dismiss()
        }
        dialog.show()

        val params: WindowManager.LayoutParams = dialog.window!!.attributes
        params.width = this.windowManager.defaultDisplay.width
        dialog.window!!.attributes = params
    }

}
