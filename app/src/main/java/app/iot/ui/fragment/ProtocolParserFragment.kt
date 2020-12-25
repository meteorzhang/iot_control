package app.iot.ui.fragment

import app.iot.BR
import app.iot.R
import app.iot.protocol.Protocol240Parser
import app.iot.viewmodel.EquipViewModel
import app.iot.viewmodel.Protocol240ViewModel
import app.iot.viewmodel.ProtocolParserViewModel

/**
 *  协议通用解析
 */
class ProtocolParserFragment(private val protocol: ByteArray) :
    BaseFragment<ProtocolParserViewModel>() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_protocol_parser
    }

    override fun initView() {
        (mViewModel as ProtocolParserViewModel).let {
            mBinding.setVariable(BR.recyclerViewViewModel, it.recyclerViewViewModel)

            it.init(protocol)
        }
    }

    override fun getBR(): Int {
        return BR.viewModel
    }
}