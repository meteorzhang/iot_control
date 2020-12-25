package app.iot.ui.fragment

import app.iot.BR
import app.iot.R
import app.iot.viewmodel.HostViewModel

/**
 * 首页功能列表
 */
class HostFragment : BaseFragment<HostViewModel>() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_host
    }

    override fun initView() {
        (mViewModel as HostViewModel).let {
            it.getNetworkData()
        }
    }

    override fun getBR(): Int {
        return BR.viewModel
    }

}