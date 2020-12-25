package app.iot.ui.fragment

import app.iot.BR
import app.iot.R
import app.iot.protocol.Protocol2403Parser
import app.iot.protocol.Protocol240Parser
import app.iot.viewmodel.EquipViewModel
import app.iot.viewmodel.Protocol2403ViewModel
import app.iot.viewmodel.Protocol240ViewModel

/**
 *  协议2403
 */
class Protocol2403Fragment(private val protocol: ByteArray) :
    BaseFragment<Protocol2403ViewModel>() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_protocol_2403
    }

    override fun initView() {
        (mViewModel as Protocol2403ViewModel).let {
            it.init(protocol)
        }
    }

    override fun getBR(): Int {
        return BR.viewModel
    }
}