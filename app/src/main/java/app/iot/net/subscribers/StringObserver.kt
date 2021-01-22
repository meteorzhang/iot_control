package app.iot.net.subscribers

import android.content.Context
import app.iot.R
import app.iot.common.util.LogUtils
import app.iot.ui.BaseActivity
import app.iot.widget.dialog.DefaultLoadingDialog
import app.iot.widget.toast.CommonToast
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.io.PrintWriter
import java.io.StringWriter

/**
 * Created by danbo on 2018/11/30.
 */
abstract class StringObserver<T> : Observer<T> {
    private var context: Context? = null

    //是否是网络加载动画
    private var isNetLoadAnimation = false

    private var loadingDialog: DefaultLoadingDialog? = null

    constructor(context: Context?, isShowLoading: Boolean) {
        init(context, isShowLoading, false, true)
    }

    constructor(isDialog: Boolean, context: Context?, isShowLoading: Boolean) {
        init(context, isShowLoading, false, isDialog)
    }

    constructor(context: Context?, isShowLoading: Boolean, cancelAble: Boolean) {
        init(context, isShowLoading, cancelAble, false)
    }

    private fun init(
        mContext: Context?,
        isShowLoading: Boolean,
        cancelAble: Boolean,
        isDialog: Boolean
    ) {
        this.context = mContext

        isNetLoadAnimation = isShowLoading
        if (isShowLoading) {
            loadingDialog = context?.let { DefaultLoadingDialog(it as BaseActivity<*>, isDialog) }
            loadingDialog?.setDialogFail()
            loadingDialog?.show(context?.getString(R.string.loading), cancelAble)
        }
    }

    override fun onSubscribe(d: Disposable) {
    }

    override fun onNext(response: T) {
        LogUtils.e("------RESPONSE------", response.toString())

        onSuccess(response)

        dismissProgress()
    }

    private fun dismissProgress() {
        if (isNetLoadAnimation) {
            loadingDialog?.setDialogFail()
        }
    }

    private fun getStackTrace(throwable: Throwable): String {
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)

        printWriter.use {
            throwable.printStackTrace(it)
            return stringWriter.toString()
        }
    }

    override fun onError(e: Throwable) {
        e.printStackTrace()

        dismissProgress()
        CommonToast.middleShow("数据异常")
    }

    override fun onComplete() {}

    /**
     * 请求成功
     *
     * @param response 服务器返回的数据
     */
    abstract fun onSuccess(response: T)

}