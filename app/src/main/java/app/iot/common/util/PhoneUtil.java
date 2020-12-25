package app.iot.common.util;

/**
 * Created by danbo on 2020-01-03.
 */
public class PhoneUtil {
    /**
     * 验证手机号是否合法
     *
     * @return
     */
    public static boolean isMobileNO(String str) {
        return str != null && str.length() == 11;
    }
}
