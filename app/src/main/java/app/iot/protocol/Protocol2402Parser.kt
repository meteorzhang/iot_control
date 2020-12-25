package app.iot.protocol

import app.iot.common.util.FormatUtil
import app.iot.widget.toast.CommonToast
import com.inuker.bluetooth.library.utils.ByteUtils
import java.io.Serializable
import java.math.BigInteger

object Protocol2402Parser {

    fun get2402Protocol(protocol: ByteArray): Protocol2402? {
        val status = parse2402Status(protocol) ?: return null

        return Protocol2402(
            ProtocolUtil.getDeviceNo(protocol),
            status,
            parseBanZaiTemp(protocol),
            parseBanZaiHumidity(protocol),
            parseTanTouTemp(protocol),
            parseBatteryVoltage(protocol),
            parseBatteryQuantity(protocol),
            parseTimes(protocol)
        )

    }

    data class Protocol2402(
        val deviceNo: String?,
        val status: Protocol2402Status,
        val banzaiTemp: Double?,
        val banzaiHumidity: Int?,
        val tanTouTemp: Double?,
        val batteryVoltage: Int?,
        val batteryQuantity: Int?,
        val mills: Long?
    ) : Serializable

    data class Protocol2402Status(
        val mainStatus: Int,//本机总体状态：7
        val thSensorStatus: Int,//温湿度传感器状态：6
        val storageStatus: Int,//存储模块状态：5
        val tempSensorStatus: Int,//温度传感器1状态：4
        val batteryStatus: Int//电压测量模块状态 模块状态：3
    ) : Serializable

    /**
     * 2402设备状态(协议从第4到第4个字节表示2402设备状态)
     * 设备状态，8 位二进制数，0 为异常，1 位正常，高位优先
     */

    /**
     * 解析2402状态
     */
    private fun parse2402Status(protocol: ByteArray): Protocol2402Status? {
        val statusBytes = ByteUtils.getBytes(
            protocol,
            4,
            4
        )
        val statusStr = FormatUtil.byteArrToBinStr(statusBytes)
        if (statusStr.isNullOrEmpty() && statusStr.length != 8) {
            CommonToast.middleShow("协议错误！")
            return null
        }

        //开始读字节
        return Protocol2402Status(
            statusStr[0].toInt() - 48,
            statusStr[1].toInt() - 48,
            statusStr[2].toInt() - 48,
            statusStr[3].toInt() - 48,
            statusStr[4].toInt() - 48
        )
    }

    //板载温度，2 字节，整型，单位 0.01°C，取值范围-32768~+23767
    private fun parseBanZaiTemp(protocol: ByteArray): Double? {
        val hex = ByteUtils.byteToString(ByteUtils.getBytes(protocol, 5, 6))
        val bi = BigInteger(hex, 16)
        return bi.toDouble()/100
    }

    //7 8 9 10 11为设备编号

    //板载湿度，1 字节，无符号整型，单位%，取值范围 0~100
    private fun parseBanZaiHumidity(protocol: ByteArray): Int? {
        return ByteUtils.byteToString(ByteUtils.getBytes(protocol, 12, 12)).toInt(16)
    }

    //探头温度，2 字节，整型，单位 0.01°C，取值范围-32768~+23767
    private fun parseTanTouTemp(protocol: ByteArray): Double? {
        val hex = ByteUtils.byteToString(ByteUtils.getBytes(protocol, 13, 14))
        val bi = BigInteger(hex, 16)
        return bi.toDouble()/100
    }

    //15保留

    //电池电压，2 字节，无符号整型，单位 mV，取值范围 0~65535
    private fun parseBatteryVoltage(protocol: ByteArray): Int? {
        return ByteUtils.byteToString(ByteUtils.getBytes(protocol, 16, 17)).toInt(16)
    }

    //电池电量，1 字节，无符号整型，单位%，取值范围 0~100
    private fun parseBatteryQuantity(protocol: ByteArray): Int? {
        return ByteUtils.byteToString(ByteUtils.getBytes(protocol, 18, 18)).toInt(16)
    }

    //时间戳，4 字节，无符号整型，UTC+8(北京时间)
    private fun parseTimes(protocol: ByteArray): Long? {
        return ByteUtils.byteToString(ByteUtils.getBytes(protocol, 19, 22)).toLong(16)
    }
}