package app.iot.common.util;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by HaiyuKing
 * Used 底部虚拟导航栏工具类
 */
public class NavUtils {

    private NavUtils() {
        throw new RuntimeException("NavUtils cannot be initialized!");
    }

    static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return resourceId > 0 ? context.getResources().getDimensionPixelSize(resourceId) : 0;
    }

    /**
     * 获取底部虚拟导航栏高度
     *
     * @param context
     * @return
     */
    static int getNavigationBarHeight(Context context) {
        //方法1
        boolean hasNavigationBar = navigationBarExist(scanForActivity(context)) && !vivoNavigationGestureEnabled(context);

        if (!hasNavigationBar) {//如果不含有虚拟导航栏，则返回高度值0
            return 0;
        }
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height",
                "dimen", "android");
        //获取NavigationBar的高度
        return resources.getDimensionPixelSize(resourceId);
    }

    /*========================================方法1======================================================*/

    /**
     * 通过获取不同状态的屏幕高度对比判断是否有NavigationBar
     * https://blog.csdn.net/u010042660/article/details/51491572
     * https://blog.csdn.net/android_zhengyongbo/article/details/68941464
     */
    static boolean navigationBarExist(Activity activity) {
        if(activity==null){
            return false;
        }
        WindowManager windowManager = activity.getWindowManager();
        Display d = windowManager.getDefaultDisplay();

        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        d.getRealMetrics(realDisplayMetrics);

        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);

        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;
        return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
    }

    /**
     * 解决java.lang.ClassCastException: android.view.ContextThemeWrapper cannot be cast to android.app.Activity问题
     * https://blog.csdn.net/yaphetzhao/article/details/49639097
     */
    private static Activity scanForActivity(Context cont) {
        if (cont == null)
            return null;
        else if (cont instanceof Activity)
            return (Activity) cont;
        else if (cont instanceof ContextWrapper)
            return scanForActivity(((ContextWrapper) cont).getBaseContext());

        return null;
    }

    /**
     * 获取vivo手机设置中的"navigation_gesture_on"值，判断当前系统是使用导航键还是手势导航操作
     *
     * @param context app Context
     * @return false 表示使用的是虚拟导航键(NavigationBar)， true 表示使用的是手势， 默认是false
     * https://blog.csdn.net/weelyy/article/details/79284332#更换部分被拉伸的图片资源文件
     */
    private static boolean vivoNavigationGestureEnabled(Context context) {
        int val = Settings.Secure.getInt(context.getContentResolver(), "navigation_gesture_on", 0);
        return val != 0;
    }
}