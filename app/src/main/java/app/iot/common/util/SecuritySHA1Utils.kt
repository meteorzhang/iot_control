package app.iot.common.util

import java.security.MessageDigest

/**
 * Created by danbo on 2019-11-11.
 */
object SecuritySHA1Utils {

    /**
     * SHA1实现
     *
     * @return hex值
     */
    @Throws(Exception::class)
    fun shaEncode(inStr: String): String {
        val sha: MessageDigest
        try {
            sha = MessageDigest.getInstance("SHA1")
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }

        val byteArray = inStr.toByteArray(charset("UTF-8"))
        val md5Bytes = sha.digest(byteArray)
        val hexValue = StringBuffer()
        for (i in md5Bytes.indices) {
            val value = md5Bytes[i].toInt() and 0xff
            if (value < 16) {
                hexValue.append("0")
            }
            hexValue.append(Integer.toHexString(value))
        }
        return hexValue.toString()
    }

}