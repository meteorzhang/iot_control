package app.iot.common.util

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.View

/**
 * Created by danbo on 2019-11-07.
 */
object ScreenUtils {
    // 将px值转换为dip或dp值
    fun px2dip(pxValue: Float): Int {
        val scale = Utils.getContext()!!.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    // 将dip或dp值转换为px值
    fun dip2px(dipValue: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dipValue.toFloat(),
            Resources.getSystem().displayMetrics
        ).toInt()
    }

    // 将px值转换为sp值
    fun px2sp(context: Context, pxValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

    // 将sp值转换为px值
    fun sp2px(spValue: Float): Int {
        Utils.getContext()?.let {
            val fontScale = it.resources.displayMetrics.scaledDensity
            return (spValue * fontScale + 0.5f).toInt()
        }
        return 0
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    fun hideBottomUIMenu(activity: Activity) {
        //隐藏虚拟按键，并且全屏

        //for new api versions.
        val decorView = activity.window.decorView
        val uiOptions = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_FULLSCREEN)
        decorView.systemUiVisibility = uiOptions

        activity.window.decorView.setOnSystemUiVisibilityChangeListener {
            var uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                .or(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
                .or(View.SYSTEM_UI_FLAG_FULLSCREEN)
                .or(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
                .or(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            uiOptions = uiOptions.or(0x00001000)

            activity.window.decorView.systemUiVisibility = uiOptions
        }

    }

    fun hideBottomUIMenu(dialog: Dialog) {
        dialog.window?.let {
            it.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            it.decorView.setOnSystemUiVisibilityChangeListener {
                var uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    .or(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
                    .or(View.SYSTEM_UI_FLAG_FULLSCREEN)
                    .or(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
                    .or(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
                uiOptions = uiOptions.or(0x00001000)

                dialog.window!!.decorView.systemUiVisibility = uiOptions
            }
        }
    }
}