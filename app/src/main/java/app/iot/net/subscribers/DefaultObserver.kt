package app.iot.net.subscribers

import android.content.Context
import android.net.ParseException
import android.text.TextUtils
import android.widget.Toast
import app.iot.R
import app.iot.logout
import app.iot.net.response.BasicResponse
import app.iot.ui.BaseActivity
import app.iot.widget.dialog.DefaultLoadingDialog
import app.iot.widget.toast.CommonToast
import com.google.gson.JsonParseException
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.json.JSONException
import retrofit2.HttpException
import java.io.InterruptedIOException
import java.io.PrintWriter
import java.io.StringWriter
import java.net.ConnectException
import java.net.UnknownHostException


/**
 * Created by danbo on 2018/11/30.
 */
abstract class DefaultObserver<T : BasicResponse<*>> : Observer<T> {
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
        if (response.statusCode == 200) {
            onSuccess(response)
        } else {
            onFail(response)
        }
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

        if (e is HttpException) {     //HTTP错误
            onException(ExceptionReason.BAD_NETWORK)

            when (e.code()) {
                400 -> {
                    onException(ExceptionReason.AUTH_ERROR)
                }
                300 -> {
                    onException(ExceptionReason.ACCESS_ERROR)
                }
                else -> {
                    onException(ExceptionReason.BAD_NETWORK)
                }
            }
        } else if (e is ConnectException || e is UnknownHostException) {   //连接错误
            onException(ExceptionReason.CONNECT_ERROR)
        } else if (e is InterruptedIOException) {   //连接超时
            onException(ExceptionReason.CONNECT_TIMEOUT)
        } else if (e is JsonParseException
            || e is JSONException
            || e is ParseException
        ) {   //解析错误
            onException(ExceptionReason.PARSE_ERROR)
        } else {
            onException(ExceptionReason.CONNECT_TIMEOUT)
        }
    }

    override fun onComplete() {}

    /**
     * 请求成功
     *
     * @param response 服务器返回的数据
     */
    abstract fun onSuccess(response: T)

    /**
     * 服务器返回数据，但响应码不为200
     *
     * @param response 服务器返回的数据
     */
    open fun onFail(response: T) {
        if (response.statusCode == 401) {
            logout()

            CommonToast.show(
                "认证失败,请重新登录",
                Toast.LENGTH_SHORT
            )
            return
        }
        val message = response.message
        if (TextUtils.isEmpty(message)) {
            CommonToast.show(R.string.connect_timeout)
        } else {
            CommonToast.middleShow(message)
        }
    }

    /**
     * 请求异常
     *
     * @param reason
     */
    open fun onException(reason: ExceptionReason) {
        when (reason) {
            ExceptionReason.CONNECT_ERROR -> CommonToast.show(
                R.string.connect_error,
                Toast.LENGTH_SHORT
            )

            ExceptionReason.CONNECT_TIMEOUT -> CommonToast.show(
                R.string.connect_timeout,
                Toast.LENGTH_SHORT
            )

            ExceptionReason.BAD_NETWORK -> CommonToast.show(
                R.string.bad_network,
                Toast.LENGTH_SHORT
            )

            ExceptionReason.ACCESS_ERROR -> {
                CommonToast.show(
                    "访问异常300",
                    Toast.LENGTH_SHORT
                )
            }
            ExceptionReason.AUTH_ERROR -> {
                logout()
                CommonToast.show(
                    "认证过期，请重新登录！",
                    Toast.LENGTH_SHORT
                )
            }

            ExceptionReason.PARSE_ERROR -> CommonToast.show(
                R.string.parse_error,
                Toast.LENGTH_SHORT
            )

            ExceptionReason.UNKNOWN_ERROR -> CommonToast.show(
                R.string.unknown_error,
                Toast.LENGTH_SHORT
            )
        }
    }

    /**
     * 请求网络失败原因
     */
    enum class ExceptionReason {
        /**
         * 解析数据失败
         */
        PARSE_ERROR,

        /**
         * 网络问题
         */
        BAD_NETWORK,

        /**
         * 认知错误
         */
        AUTH_ERROR,

        /**
         * 访问异常
         */
        ACCESS_ERROR,

        /**
         * 连接错误
         */
        CONNECT_ERROR,

        /**
         * 连接超时
         */
        CONNECT_TIMEOUT,

        /**
         * 未知错误
         */
        UNKNOWN_ERROR
    }
}