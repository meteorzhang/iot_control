package app.iot.ui

import app.iot.BR
import app.iot.R
import app.iot.viewmodel.LoginViewModel

class LoginActivity : BaseActivity<LoginViewModel>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun initView() {
        mBinding.setVariable(BR.data, mViewModel?.titleViewModel)
        mViewModel?.titleViewModel?.context = mBinding.root.context

    }

}
