package app.iot.protocol

import app.iot.common.util.FormatUtil
import app.iot.common.util.LogUtils
import app.iot.dataInfo
import app.iot.model.Protocol
import app.iot.model.ProtocolDeviceType
import com.inuker.bluetooth.library.utils.ByteUtils
import java.math.BigDecimal
import java.math.BigInteger

object ProtocolParser {

    fun getProtocol(protocol: ByteArray): List<Protocol>? {
        val protocolType = ProtocolDeviceType.valueOf(
            "${BLEConstant.PROTOCOL_PREFIX}${ProtocolUtil.getDeviceType(protocol)}"
        )

        LogUtils.d("获取协议详情：" + protocolType.title + ":" + ByteUtils.byteToString(protocol))

        when (protocolType) {
            ProtocolDeviceType.PF0 -> {//240
                return parse240(protocol)
            }
            ProtocolDeviceType.PF2 -> {//242
                return parse242(protocol)
            }
            ProtocolDeviceType.PF3 -> {//243
                return parse243(protocol)
            }
            ProtocolDeviceType.PF4 -> {//244
                return parse244(protocol)
            }
            ProtocolDeviceType.P01 -> {//2401
                return parse2401(protocol)
            }
            ProtocolDeviceType.P02 -> {//2402
                return parse2402(protocol)
            }
            ProtocolDeviceType.P03 -> {//2403
                return parse2403(protocol)
            }
            ProtocolDeviceType.P04 -> {//2404
                return parse2404(protocol)
            }
        }

        return null
    }

    private fun parse240(protocolBytes: ByteArray): List<Protocol>? {
        val protocol240 = dataInfo?.bluetoothProtocolData?.P240 ?: return null
        return parse(protocolBytes, protocol240)
    }

    private fun parse242(protocolBytes: ByteArray): List<Protocol>? {
        val protocol242 = dataInfo?.bluetoothProtocolData?.P242 ?: return null
        return parse(protocolBytes, protocol242)
    }

    private fun parse243(protocolBytes: ByteArray): List<Protocol>? {
        val protocol243 = dataInfo?.bluetoothProtocolData?.P243 ?: return null
        return parse(protocolBytes, protocol243)
    }

    private fun parse244(protocolBytes: ByteArray): List<Protocol>? {
        val protocol244 = dataInfo?.bluetoothProtocolData?.P244 ?: return null
        return parse(protocolBytes, protocol244)
    }

    private fun parse2401(protocolBytes: ByteArray): List<Protocol>? {
        val protocol2401 = dataInfo?.bluetoothProtocolData?.P2401 ?: return null
        return parse(protocolBytes, protocol2401)
    }

    private fun parse2402(protocolBytes: ByteArray): List<Protocol>? {
        val protocol2402 = dataInfo?.bluetoothProtocolData?.P2402 ?: return null
        return parse(protocolBytes, protocol2402)
    }

    private fun parse2403(protocolBytes: ByteArray): List<Protocol>? {
        val protocol2403 = dataInfo?.bluetoothProtocolData?.P2403 ?: return null
        return parse(protocolBytes, protocol2403)
    }

    private fun parse2404(protocolBytes: ByteArray): List<Protocol>? {
        val protocol2404 = dataInfo?.bluetoothProtocolData?.P2404 ?: return null
        return parse(protocolBytes, protocol2404)
    }


    //通用解析
    private fun parse(protocolBytes: ByteArray, protocolList: List<Protocol>): List<Protocol> {
        var skippedByteIndex = 0
        for (protocol in protocolList) {

            if (protocol.detail.isNullOrEmpty()) {//设备详情为空
                val itemBytes =
                    ByteUtils.getBytes(
                        protocolBytes,
                        skippedByteIndex,
                        (skippedByteIndex + protocol.size - 1)
                    )
                val itemStr = ByteUtils.byteToString(itemBytes)

                if (protocol.dataType == "DeviceType" || protocol.dataType == "Header") {//设备类型和协议头用16位
                    protocol.value = itemStr
                } else {

                    LogUtils.d("处理协议："+ByteUtils.byteToString(protocolBytes))
                    LogUtils.d("读取字节："+skippedByteIndex+">"+ (skippedByteIndex + protocol.size - 1))
                    LogUtils.d("处理0.01℃  dataType=" + protocol.dataType + " valueType=" + protocol.valueType + " value="+ itemStr+ " valueUnit=" + protocol.valueUnit +" 系数："+protocol.coefficient)

                    when (protocol.valueType) {//处理类型值
                        "int" -> {//整型
                            if (protocol.coefficient != 1.0) {//处理其他系数

                                parseDouble(itemStr)?.let {
                                    if (protocol.valueUnit.startsWith("0.01")) {//处理0.01单位
                                        val b = BigDecimal((it / 100) * protocol.coefficient)
                                        val df: Double =
                                            b.setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()
                                        protocol.value = df
                                    } else {
                                        val b = BigDecimal(it * protocol.coefficient)
                                        val df: Double =
                                            b.setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()
                                        protocol.value = df
                                    }
                                }
                            } else {//系数为1
                                protocol.value = parseInt(itemStr)
                            }

                        }
                        "unsignedInt" -> {//无符号整型
                            if (protocol.coefficient != 1.0) {//处理其他系数
                                parseUnsignedDouble(itemStr)?.let {
                                    if (protocol.valueUnit.startsWith("0.01")) {//处理0.01单位
                                        val b = BigDecimal((it / 100) * protocol.coefficient)
                                        val df: Double =
                                            b.setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()
                                        protocol.value = df
                                    } else {
                                        val b = BigDecimal(it * protocol.coefficient)
                                        val df: Double =
                                            b.setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()
                                        protocol.value = df
                                    }
                                }
                            } else {//系数为1
                                protocol.value = parseUnsignedInt(itemStr)
                            }

                        }
                    }
                }

            } else {//不为空则是设备状态
                //处理设备状态
                val statusBytes =
                    ByteUtils.getBytes(
                        protocolBytes,
                        skippedByteIndex,
                        (skippedByteIndex + protocol.size - 1)
                    )
                val statusStr = FormatUtil.byteArrToBinStr(statusBytes)
                var skippedBitIndex = 0
                for (detail in protocol.detail) {
                    detail.size?.let {
                        skippedBitIndex += it
                    }
                }
                for (detail in protocol.detail) {
                    detail.size?.let {
                        detail.status = statusStr[skippedBitIndex - 1].toInt() - 48
                        skippedBitIndex--
                    }
                }
                //设备状态处理完毕
            }

            skippedByteIndex += protocol.size
        }

        return protocolList
    }

    private fun parseInt(hex: String): Int? {
        val bi = BigInteger(hex, 16)
        return bi.toInt()
    }

    //0.01单位处理
    private fun parseDouble(hex: String): Double? {
        val bi = BigInteger(hex, 16)
        return bi.toDouble()
    }

    //0.01单位处理
    private fun parseUnsignedDouble(hex: String): Double? {
        if (hex.isEmpty()) {
            return null
        }

        return hex.toLong(16).toDouble()
    }

    private fun parseUnsignedInt(hex: String): Long? {
        if (hex.isEmpty()) {
            return null
        }
        return hex.toLong(16)
    }
}