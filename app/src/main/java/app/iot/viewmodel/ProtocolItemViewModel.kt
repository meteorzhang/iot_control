package app.iot.viewmodel

import androidx.databinding.ObservableField
import app.iot.BR
import app.iot.R
import app.iot.model.Protocol
import app.iot.model.ProtocolDetail
import app.iot.model.ProtocolDeviceType
import app.iot.protocol.BLEConstant
import app.iot.protocol.ProtocolUtil
import java.text.SimpleDateFormat
import java.util.*

class ProtocolItemViewModel(protocol: Protocol) : BaseViewModel() {

    val name = ObservableField<String>()
    val value = ObservableField<String>()
    val isTitle = ObservableField<Boolean>()

    val details = ObservableField<List<ProtocolDetail>>()

    var recyclerViewViewModel: RecyclerViewViewModel<ProtocolDetailItemViewModel> =
        RecyclerViewViewModel(
            null,
            R.layout.layout_protocol_detail_item,
            RecyclerViewViewModel.VERTICAL_LINEARLAYOUTMANAGER, 1
        )

    init {
        if (protocol.desc == "设备状态") {
            isTitle.set(true)
        } else {
            isTitle.set(false)
        }
        name.set(protocol.desc)
        protocol.value?.let {
            when (protocol.dataType) {//Reserved Header Data DeviceType DeviceNo UTCTime DeviceStatus CRC-8 CRC-16 DoorStatus ErrorStatus
                "DeviceType" -> {
                    val protocolType = ProtocolDeviceType.valueOf(
                        "${BLEConstant.PROTOCOL_PREFIX}${it}"
                    )
                    value.set(protocolType.title)
                }
                "UTCTime" -> {
                    val date =
                        SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date((it as Long) * 1000))
                    value.set(date)
                }
                else -> {
                    value.set(it.toString() + protocol.valueUnit.removePrefix("0.01"))
                }
            }

        }

        protocol.detail?.let {
            val detailList: MutableList<ProtocolDetail> = arrayListOf()
            for (detail in it) {
                if (detail.dataType == "UsedBit") {
                    detailList.add(detail)
                }
            }
            details.set(detailList)
        }
    }
}