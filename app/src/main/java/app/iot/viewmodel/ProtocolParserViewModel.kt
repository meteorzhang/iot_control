package app.iot.viewmodel

import app.iot.BR
import app.iot.R
import app.iot.common.util.LogUtils
import app.iot.model.Protocol
import app.iot.protocol.ProtocolParser

class ProtocolParserViewModel : BaseViewModel() {

    var recyclerViewViewModel: RecyclerViewViewModel<ProtocolItemViewModel> = RecyclerViewViewModel(
        null,
        R.layout.layout_protocol_item,
        RecyclerViewViewModel.VERTICAL_LINEARLAYOUTMANAGER, 1
    )

    var frist: Boolean = false

    fun init(protocolBytes: ByteArray) {
        val singData = ProtocolItemViewModel(
            Protocol(
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
        )
        ProtocolParser.getProtocol(protocolBytes)?.let {
            LogUtils.d("协议详情：" + it.size)
            for (protocol in it) {//设备详情列表
                if (protocol.dataType != "Header" && protocol.dataType != "Reserved") {
                    if (protocol.desc == "设备状态") {
                        frist = true
                    } else {
                        if (frist) {
                            recyclerViewViewModel.item.add(singData)
                            frist = false
                        }
                    }
                    val itemViewModel = ProtocolItemViewModel(protocol)
                    itemViewModel.context = context
                    recyclerViewViewModel.item.add(itemViewModel)
                }

            }
        }
    }

}