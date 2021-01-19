package app.iot.ui.fragment

import app.iot.BR
import app.iot.R
import app.iot.adapter.OperationRecordAdapter
import app.iot.common.util.LogUtils
import app.iot.ext.initVertical
import app.iot.model.Protocol
import app.iot.model.ProtocolDeviceType
import app.iot.protocol.BLEConstant
import app.iot.protocol.Protocol240Parser
import app.iot.protocol.ProtocolParser
import app.iot.protocol.ProtocolUtil
import app.iot.viewmodel.EquipViewModel
import app.iot.viewmodel.Protocol240ViewModel
import app.iot.viewmodel.ProtocolItemViewModel
import app.iot.viewmodel.ProtocolParserViewModel
import com.inuker.bluetooth.library.utils.ByteUtils
import kotlinx.android.synthetic.main.fragment_protocol_parser.*

/**
 *  协议通用解析
 */
class ProtocolParserFragment(private val protocol: ByteArray) :
    BaseFragment<ProtocolParserViewModel>() {

    var mAdapter = OperationRecordAdapter()

    override fun getLayoutId(): Int {
        return R.layout.fragment_protocol_parser
    }

    override fun initView() {
//        (mViewModel as ProtocolParserViewModel).let {
//            mBinding.setVariable(BR.recyclerViewViewModel, it.recyclerViewViewModel)
//
//            it.init(protocol)
//        }
        mBinding.root.post {
            recycler_view.initVertical(bindAdapter = mAdapter)
            initData()
        }
    }

    var frist: Boolean = false

    private fun initData() {
        val protocolType = ProtocolDeviceType.valueOf(
            "${BLEConstant.PROTOCOL_PREFIX}${ProtocolUtil.getDeviceType(protocol)}"
        )
        tv_info.text = " ${ByteUtils.byteToString(protocol)}"
//        tv_info.text = "协议详情---> \n ${}"
        val singData = Protocol(
            number = 0,
            dataType = "",
            size = 0,
            sizeUnit = "",
            valueUnit = "",
            valueType = "",
            coefficient = 0.0,
            desc = "实况数据",
            detail = null,
            value = null
        )

        var mDataList = ArrayList<Protocol>()
//        ProtocolParser.getProtocol(ByteUtils.stringToBytes("484C6F0915020BEE812A0F0917110EBD1F5E3B553C49"))
        ProtocolParser.getProtocol(protocol)
            ?.let {
                LogUtils.e("协议详情：" + ByteUtils.byteToString(protocol))
                for (protocol in it) {//设备详情列表
                    if (protocol.dataType != "Header" && protocol.dataType != "Reserved") {
                        if (protocol.desc == "设备状态") {
                            frist = true
                        } else {
                            if (frist) {
                                mDataList.add(singData)
                                frist = false
                            }
                        }
                        mDataList.add(protocol)
                    }
                }
            }
        mAdapter.setList(mDataList)
    }

    override fun getBR(): Int {
        return BR.viewModel
    }
}