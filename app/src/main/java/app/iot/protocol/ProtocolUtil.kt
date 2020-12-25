package app.iot.protocol

import app.iot.common.util.FormatUtil
import com.inuker.bluetooth.library.utils.ByteUtils

object ProtocolUtil {

    /**
     * 从data中读取设备类型(从下标2开始第5个字节表示设备类型)
     * @return 16进制设备代码
     */
    fun getDeviceType(data: ByteArray): String? {
        return ByteUtils.byteToString(ByteUtils.getBytes(data, 5, 5))
    }

    /**
     * 从data中读取设备序列号(从下标5开始到第9个字节表示设备序列号)(设备类型+设备id)
     * 第6到9字节使用十进制显示
     */
    fun getDeviceNo(data: ByteArray?): String? {
        data?.let {
            val deviceType = getDeviceType(it)
            val deviceId = ByteUtils.byteToString(ByteUtils.getBytes(data, 6, 9))
            return deviceType + FormatUtil.hexToInt(deviceId)
        }

        return null
    }

}