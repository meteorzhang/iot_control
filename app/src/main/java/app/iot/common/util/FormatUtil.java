package app.iot.common.util;


import java.text.DecimalFormat;

/**
 * Created bt danbo on 2020/4/22.
 */
public class FormatUtil {

    public static String formatMemberId(long memberId) {
        DecimalFormat df = new DecimalFormat("000000");
        return df.format(memberId);
    }

    /**
     * 16进制字符串转10进制数字
     *
     * @param hex
     * @return
     */
    public static Integer hexToInt(String hex) {
        return Integer.parseInt(hex, 16);
    }

    /**
     * byte数组转换为二进制字符串
     **/
    public static String byteArrToBinStr(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte bt : bytes) {
            result.append(getBit(bt));
        }
        return result.toString().substring(0, result.length() - 1);
    }

    /**
     * 从byte中获取bit
     *
     * @param bt
     * @return
     */
    public static String getBit(byte bt) {
        StringBuffer sb = new StringBuffer();
        sb.append((bt >> 7) & 0x1)
                .append((bt >> 6) & 0x1)
                .append((bt >> 5) & 0x1)
                .append((bt >> 4) & 0x1)
                .append((bt >> 3) & 0x1)
                .append((bt >> 2) & 0x1)
                .append((bt >> 1) & 0x1)
                .append((bt) & 0x1);
        return sb.toString();
    }
}
