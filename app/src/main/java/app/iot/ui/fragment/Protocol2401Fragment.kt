package app.iot.ui.fragment

import app.iot.BR
import app.iot.R
import app.iot.protocol.Protocol2401Parser
import app.iot.protocol.Protocol240Parser
import app.iot.viewmodel.EquipViewModel
import app.iot.viewmodel.Protocol2401ViewModel
import app.iot.viewmodel.Protocol240ViewModel

/**
 *  协议2401
 */
class Protocol2401Fragment(private val protocol: ByteArray) :
    BaseFragment<Protocol2401ViewModel>() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_protocol_2401
    }

    override fun initView() {
        (mViewModel as Protocol2401ViewModel).let {
            it.init(protocol)
        }
    }

    override fun getBR(): Int {
        return BR.viewModel
    }
}