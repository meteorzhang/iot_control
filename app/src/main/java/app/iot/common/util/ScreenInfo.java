
package app.iot.common.util;

import android.content.Context;
import android.util.DisplayMetrics;

import java.io.Serializable;

/**
 * ScreenInfo.
 */
public class ScreenInfo implements Serializable {
    /**
     * The instance.
     */
    private static ScreenInfo instance = null;
    //屏幕宽度
    private int screenWidth;
    //屏幕高度
    private int screenHeight;
    //屏幕密度
    private float density;
    //状态栏高度
    private int statusBarHeight;
    //导航栏高度
    private int navigationBarHeight;

    /**
     * Instantiates a new screen info.
     *
     */
    private ScreenInfo() {
        Context context = Utils.INSTANCE.getContext();

        DisplayMetrics displayMetrics = context.getResources()
                .getDisplayMetrics();

        // 获取屏幕密度
        this.density = displayMetrics.density;

        // 获取屏幕像素宽度
        // 获取屏幕像素高度
        this.screenWidth = displayMetrics.widthPixels;
        this.screenHeight = displayMetrics.heightPixels;

        // 横屏时，重设高宽度
        if (this.screenWidth > this.screenHeight) {
            int width = this.screenWidth;
            this.screenWidth = this.screenHeight;
            this.screenHeight = width;
        }

        //状态栏高度
        this.statusBarHeight = NavUtils.getStatusBarHeight(context);
        //导航栏高度
        this.navigationBarHeight = NavUtils.getNavigationBarHeight(context);
    }

    public synchronized static ScreenInfo getInstance() {
        if (instance == null) {
            instance = new ScreenInfo();
        }
        return instance;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public float getDensity() {
        return density;
    }

    public void setDensity(float density) {
        this.density = density;
    }

    public int getStatusBarHeight() {
        return statusBarHeight;
    }

    public void setStatusBarHeight(int statusBarHeight) {
        this.statusBarHeight = statusBarHeight;
    }

    public int getNavigationBarHeight() {
        return navigationBarHeight;
    }

    public void setNavigationBarHeight(int navigationBarHeight) {
        this.navigationBarHeight = navigationBarHeight;
    }
}
