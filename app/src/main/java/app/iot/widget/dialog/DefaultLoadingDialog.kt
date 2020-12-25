package app.iot.widget.dialog

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.os.Build
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import app.iot.R
import app.iot.ui.BaseActivity


@SuppressLint("StaticFieldLeak")
/**
 * 默认加载框
 */
class DefaultLoadingDialog(val activity: BaseActivity<*>, val isDialog: Boolean) {
    private var mDialog: AlertDialog? = null

    private lateinit var mLoadingView: ImageView
    //private lateinit var mLoadingCenterView: ImageView
    lateinit var mLoadingText: TextView

    private lateinit var mLayout: LinearLayout

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    //显示上传中动画
    fun show(textString: String?, cancelAble: Boolean) {
        if (activity.isFinishing) {
            return
        }

        if (isLoading(activity)) {
            return
        }

        val loadingView =
            LayoutInflater.from(activity).inflate(R.layout.default_dialog_loading, null)
        mLoadingView = loadingView.findViewById(R.id.iv_loading)
        //mLoadingCenterView = loadingView.findViewById(R.id.iv_loading_center)
        mLoadingText = loadingView.findViewById(R.id.tv_loading)
        mLayout = loadingView.findViewById(R.id.layout_loading)

        loadingView.findViewById<View>(R.id.cover).setOnClickListener {

            if (cancelAble) {
                hideLoading(activity)
            }
        }

        if (activity.isDestroyed) {
            return
        }

        if (isDialog) {
            loadingView.findViewById<View>(R.id.cover).visibility = View.GONE

            val builder =
                activity.let { AlertDialog.Builder(it, R.style.defaultLoadingDialogStyle) }
            builder.setCancelable(cancelAble)
            mDialog = builder.create()
            if (activity.isDestroyed) {
                return
            }
            mDialog?.show()
            mDialog?.setContentView(loadingView)

//            mLoadingView.post {
//                mDialog?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//            }

        } else {
            val root = activity.window.decorView.findViewById<FrameLayout>(android.R.id.content)
            root.removeView(loadingView)
            root.addView(loadingView)
        }

        mLoadingView.setBackgroundResource(R.drawable.ic_loading)
        mLoadingView.startAnimation(getRotateAnimation())

        //mLoadingCenterView.setBackgroundResource(R.layout.loading_center)
        //val rotateYAnimation = RotateYAnimation()
        //rotateYAnimation.repeatCount = ValueAnimator.INFINITE
        //mLoadingCenterView.startAnimation(rotateYAnimation)

        mLoadingText.text = textString
    }

    //旋转动画
    //共用旋转动画
    private fun getRotateAnimation(): RotateAnimation {
        val animation = RotateAnimation(
            0f, 359f, Animation.RELATIVE_TO_SELF,
            0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        )
        animation.duration = 800
        animation.fillAfter = true
        //匀速
        animation.interpolator = LinearInterpolator()
        animation.repeatCount = ValueAnimator.INFINITE
        return animation
    }

    //失败时加载的动画
    fun setDialogFail() {
        hideLoading(activity)

        mDialog?.dismiss()
        mDialog = null

    }

    //成功时更新状态，结束动画
    fun setDialogSuccess() {
        //mLoadingView.clearAnimation()
        //mLoadingView.setBackgroundResource(R.layout.updating_ok)

        Handler().postDelayed({
            hideLoading(activity)

            mDialog?.dismiss()
            mDialog = null
        }, 500)

    }

    /**
     * 隐藏加载圈
     * @param activity
     */
    private fun hideLoading(activity: BaseActivity<*>) {
        val root = activity.window.decorView.findViewById<FrameLayout>(android.R.id.content)
        if (root != null) {
            val loadingView = root.findViewById<View>(R.id.cover_root)
            if (loadingView != null) {
                root.removeView(loadingView)
            }
        }
    }


    /**
     * 加载圈是否正在显示
     * @param activity
     * @return
     */
    private fun isLoading(activity: BaseActivity<*>): Boolean {
        val root = activity.window.decorView.findViewById<FrameLayout>(android.R.id.content)
        if (root != null) {
            val loadingView = root.findViewById<View>(R.id.cover_root)
            return loadingView != null && root.indexOfChild(loadingView) != -1
        }
        return false
    }

}