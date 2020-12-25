package app.iot.ui.fragment

import app.iot.BR
import app.iot.R
import app.iot.protocol.Protocol2402Parser
import app.iot.viewmodel.Protocol2402ViewModel

/**
 *  协议2402
 */
class Protocol2402Fragment(private val protocol: ByteArray) :
    BaseFragment<Protocol2402ViewModel>() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_protocol_2402
    }

    override fun initView() {
        (mViewModel as Protocol2402ViewModel).let {
            it.init(protocol)
        }
    }

    override fun getBR(): Int {
        return BR.viewModel
    }
}