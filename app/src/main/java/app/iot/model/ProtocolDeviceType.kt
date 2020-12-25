package app.iot.model

import app.iot.dataInfo
import app.iot.protocol.BLEConstant

/**
 * 协议定义设备类型(加P头)
 */
enum class ProtocolDeviceType(val protocol: String, val title: String) {

    PF0("240", getDeviceTypeTitle("PF0")),//冷藏箱监控设备主机
    PF2("242", getDeviceTypeTitle("PF2")),//罐箱监控设备主机
    PF3("243", getDeviceTypeTitle("PF3")),//货车监控设备主机
    PF4("244", getDeviceTypeTitle("PF4")),//集装箱监控设备主机
    P01("2401", getDeviceTypeTitle("P01")),//冷藏箱蓝牙基站
    P02("2402", getDeviceTypeTitle("P02")),//冷箱手持终端
    P03("2403", getDeviceTypeTitle("P03")),//冷箱门禁终端
    P04("2404", getDeviceTypeTitle("P04"))//罐箱数据采集盒
}

fun getDeviceTypeTitle(p: String): String {
    val r = p.replace(BLEConstant.PROTOCOL_PREFIX, "0x")
    val components = dataInfo?.dictionaryData?.componentCode ?: return "未知设备"

    for (c in components) {
        if (r == c.code) {
            return c.name
        }
    }
    return "未知设备"
}
