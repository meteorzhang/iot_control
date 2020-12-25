package app.iot.ui.fragment

import app.iot.BR
import app.iot.R
import app.iot.protocol.Protocol240Parser
import app.iot.viewmodel.EquipViewModel
import app.iot.viewmodel.Protocol240ViewModel

/**
 *  协议240
 */
class Protocol240Fragment(private val protocol: ByteArray) :
    BaseFragment<Protocol240ViewModel>() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_protocol_240
    }

    override fun initView() {
        (mViewModel as Protocol240ViewModel).let {
            it.init(protocol)
        }
    }

    override fun getBR(): Int {
        return BR.viewModel
    }
}