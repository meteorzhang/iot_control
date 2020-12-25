package app.iot.protocol

import app.iot.common.util.FormatUtil
import app.iot.widget.toast.CommonToast
import com.inuker.bluetooth.library.utils.ByteUtils
import java.io.Serializable
import java.math.BigInteger

object Protocol2401Parser {

    fun get2401Protocol(protocol: ByteArray): Protocol2401? {
        val status = parse2401Status(protocol) ?: return null

        return Protocol2401(
            ProtocolUtil.getDeviceNo(protocol),
            status,
            parseBanZaiTemp(protocol),
            parseBanZaiHumidity(protocol),
            parseTanTou1Temp(protocol),
            parseTanTou2Temp(protocol),
            parseTanTou3Temp(protocol),
            parseTimes(protocol)
        )

    }

    data class Protocol2401(
        val deviceNo: String?,
        val status: Protocol2401Status,
        val banzaiTemp: Int?,
        val banzaiHumidity: Int?,
        val tanTou1Temp: Double?,
        val tanTou2Temp: Double?,
        val tanTou3Temp: Double?,
        val mills: Long?
    ) : Serializable

    data class Protocol2401Status(
        val mainStatus: Int,//本机总体状态：7
        val thSensorStatus: Int,//温湿度传感器状态：6
        val storageStatus: Int,//存储模块状态：5
        val tempSensor1Status: Int,//温度传感器1状态：4
        val tempSensor2Status: Int,//温度传感器2状态：3
        val tempSensor3Status: Int,//温度传感器3状态：2
    ) : Serializable

    /**
     * 2401设备状态(协议从第4到第4个字节表示2401设备状态)
     * 设备状态，8 位二进制数，0 为异常，1 位正常，高位优先
     */

    /**
     * 解析2401状态
     */
    private fun parse2401Status(protocol: ByteArray): Protocol2401Status? {
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
        return Protocol2401Status(
            statusStr[0].toInt() - 48,
            statusStr[1].toInt() - 48,
            statusStr[2].toInt() - 48,
            statusStr[3].toInt() - 48,
            statusStr[4].toInt() - 48,
            statusStr[5].toInt() - 48
        )
    }

    //板载温度，1 字节，整型，单位°C，取值范围-128~127
    private fun parseBanZaiTemp(protocol: ByteArray): Int? {
        val hex = ByteUtils.byteToString(ByteUtils.getBytes(protocol, 5, 5))
        val bi = BigInteger(hex, 16)
        return bi.toInt()
    }

    //板载湿度，1 字节，无符号整型，单位%，取值范围 0~100
    private fun parseBanZaiHumidity(protocol: ByteArray): Int? {
        return ByteUtils.byteToString(ByteUtils.getBytes(protocol, 6, 6)).toInt(16)
    }

    //7 8 9 10 11为设备编号

    //探头 1 温度，2 字节，整型，单位 0.01°C，取值范围-32768~+23767
    private fun parseTanTou1Temp(protocol: ByteArray): Double? {
        val hex = ByteUtils.byteToString(ByteUtils.getBytes(protocol, 12, 13))
        val bi = BigInteger(hex, 16)
        return bi.toDouble()/100
    }

    //探头 2 温度，2 字节，整型，单位 0.01°C，取值范围-32768~+23767
    private fun parseTanTou2Temp(protocol: ByteArray): Double? {
        val hex = ByteUtils.byteToString(ByteUtils.getBytes(protocol, 14, 15))
        val bi = BigInteger(hex, 16)
        return bi.toDouble()/100
    }

    //探头 3 温度，2 字节，整型，单位 0.01°C，取值范围-32768~+23767
    private fun parseTanTou3Temp(protocol: ByteArray): Double? {
        val hex = ByteUtils.byteToString(ByteUtils.getBytes(protocol, 16, 17))
        val bi = BigInteger(hex, 16)
        return bi.toDouble()/100
    }

    //18保留

    //时间戳，4 字节，无符号整型，UTC+8(北京时间)
    private fun parseTimes(protocol: ByteArray): Long? {
        return ByteUtils.byteToString(ByteUtils.getBytes(protocol, 19, 22)).toLong(16)
    }
}