package app.iot.viewmodel

import app.iot.BR
import app.iot.R
import app.iot.common.util.LogUtils
import app.iot.protocol.ProtocolParser

class ProtocolParserViewModel : BaseViewModel() {

    var recyclerViewViewModel: RecyclerViewViewModel<ProtocolItemViewModel> = RecyclerViewViewModel(
        null,
        R.layout.layout_protocol_item,
        RecyclerViewViewModel.VERTICAL_LINEARLAYOUTMANAGER, 1
    )

    fun init(protocolBytes: ByteArray) {
        ProtocolParser.getProtocol(protocolBytes)?.let {
            LogUtils.d("协议详情：" + it.size)
            for (protocol in it) {//设备详情列表
                if (protocol.dataType != "Header" && protocol.dataType != "Reserved") {
                    val itemViewModel =
                        ProtocolItemViewModel(protocol)
                    itemViewModel.context = context
                    recyclerViewViewModel.item.add(itemViewModel)
                }

            }
        }
    }

}