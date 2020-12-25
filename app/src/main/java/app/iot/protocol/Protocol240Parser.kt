package app.iot.protocol

import app.iot.common.util.FormatUtil
import app.iot.common.util.LogUtils
import app.iot.widget.toast.CommonToast
import com.inuker.bluetooth.library.utils.ByteUtils
import java.io.Serializable
import java.math.BigInteger

object Protocol240Parser {

    fun get240Protocol(protocol: ByteArray): Protocol240? {
        val status = parse240Status(protocol) ?: return null

        return Protocol240(
            ProtocolUtil.getDeviceNo(protocol),
            status,
            parseXiangWaiTemp(protocol),
            parseBanZaiTemp(protocol),
            parseBanZaiHumidity(protocol),
            parseBatteryVoltage(protocol),
            parseBatteryQuantity(protocol),
            parseColdQuantity(protocol),
            parseLimitTimes(protocol),
            parseTimes(protocol)
        )

    }

    data class Protocol240(
        val deviceNo: String?,
        val status: Protocol240Status,
        val xiangwaiTemp: Int?,
        val banzaiTemp: Int?,
        val banzaiHumidity: Int?,
        val batteryVoltage: Int?,
        val batteryQuantity: Int?,
        val coldQuantity: Int?,
        val limitTimes: Int?,
        val mills: Long?
    ) : Serializable

    data class Protocol240Status(
        val mainStatus: Int,//本机总体状态：15
        val locStatus: Int,//定位模块状态：14
        val communicateStatus: Int,//通讯模块状态：13
        val accelerationSensorStatus: Int,//加速度传感器状态：12
        val batteryStatus: Int,//电压测量模块状态：11
        val ble1Status: Int,//蓝牙1基站状态：10
        val ble2Status: Int,//蓝牙2基站状态：9
        val ble3Status: Int,//蓝牙3基站状态：8
        val entranceGuardStatus: Int,//门禁终端状态：7
        val thSensorStatus: Int,//温湿度传感器状态：6
        val storageStatus: Int,//存储模块状态：5
    ) : Serializable

    /**
     * 240设备状态(协议从第4到第5个字节表示240设备状态)
     * 设备状态，16 位二进制数，0 为异常，1 位正常，高位优先
     */

    /**
     * 解析240状态
     */
    private fun parse240Status(protocol: ByteArray): Protocol240Status? {
        val statusBytes = ByteUtils.getBytes(
            protocol,
            4,
            5
        )
        val statusStr = FormatUtil.byteArrToBinStr(statusBytes)
        if (statusStr.isNullOrEmpty() && statusStr.length != 16) {
            CommonToast.middleShow("协议错误！")
            return null
        }

        //开始读字节
        //char与int的相互转化，联想ASCII码，字符‘0’对应的值为48，所以不能直接加减‘ ’
        return Protocol240Status(
            statusStr[0].toInt() - 48,
            statusStr[1].toInt() - 48,
            statusStr[2].toInt() - 48,
            statusStr[3].toInt() - 48,
            statusStr[4].toInt() - 48,
            statusStr[5].toInt() - 48,
            statusStr[6].toInt() - 48,
            statusStr[7].toInt() - 48,
            statusStr[8].toInt() - 48,
            statusStr[9].toInt() - 48,
            statusStr[10].toInt() - 48
        )
    }

    //箱外温度，1 字节，整型，单位°C，取值范围-128~127
    private fun parseXiangWaiTemp(protocol: ByteArray): Int? {
        val hex = ByteUtils.byteToString(ByteUtils.getBytes(protocol, 6, 6))
        val bi = BigInteger(hex, 16)
        return bi.toInt()
    }

    //7 8 9 10 11为设备编号

    //板载温度，1 字节，整型，单位°C，取值范围-128~127
    private fun parseBanZaiTemp(protocol: ByteArray): Int? {
        val hex = ByteUtils.byteToString(ByteUtils.getBytes(protocol, 12, 12))
        val bi = BigInteger(hex, 16)
        return bi.toInt()
    }

    //板载湿度，1 字节，无符号整型，单位%，取值范围 0~100
    private fun parseBanZaiHumidity(protocol: ByteArray): Int? {
        return ByteUtils.byteToString(ByteUtils.getBytes(protocol, 13, 13)).toInt(16)
    }

    //电池电压，2 字节，无符号整型，单位 mV，取值范围 0~65535
    private fun parseBatteryVoltage(protocol: ByteArray): Int? {
        return ByteUtils.byteToString(ByteUtils.getBytes(protocol, 14, 15)).toInt(16)
    }

    //电池电量，1 字节，无符号整型，单位%，取值范围 0~100
    private fun parseBatteryQuantity(protocol: ByteArray): Int? {
        return ByteUtils.byteToString(ByteUtils.getBytes(protocol, 16, 16)).toInt(16)
    }

    //冷量，1 字节，整型，单位%，取值-128~127
    private fun parseColdQuantity(protocol: ByteArray): Int? {
        val hex = ByteUtils.byteToString(ByteUtils.getBytes(protocol, 17, 17))
        val bi = BigInteger(hex, 16)
        return bi.toInt()
    }

    //可用时长，1 字节，无符号整型，单位 h，取值范围 0~255
    private fun parseLimitTimes(protocol: ByteArray): Int? {
        return ByteUtils.byteToString(ByteUtils.getBytes(protocol, 18, 18)).toInt(16)
    }

    //时间戳，4 字节，无符号整型，UTC+8(北京时间)
    private fun parseTimes(protocol: ByteArray): Long? {
        return ByteUtils.byteToString(ByteUtils.getBytes(protocol, 19, 22)).toLong(16)
    }
}