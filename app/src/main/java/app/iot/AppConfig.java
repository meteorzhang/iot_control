package app.iot;

public class AppConfig {

    /**
     * 开关
     */
    public static final boolean LOG_ENABLE;
    public static final int LOG_LEVEL;

    /**
     * 开发地址
     */
    public static final String BASE_URL;

    static boolean debug = true;//是否debug环境

    static {
        if (debug) {
            LOG_LEVEL = 0;
            LOG_ENABLE = true;
            BASE_URL = "http://platform.hwainno.com/api/";
        } else {
            LOG_LEVEL = 5;
            LOG_ENABLE = false;
            BASE_URL = "https://iot.apptask.club/";
        }
    }
}
