package app.iot

import SPUtils
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import app.iot.AppConstant.DATA_JSON
import app.iot.AppConstant.DATA_VERSION
import app.iot.AppConstant.TOKEN
import app.iot.common.util.*
import app.iot.model.DataInfo
import app.iot.model.DataVersion
import app.iot.net.RetrofitProvider
import app.iot.net.StringRetrofitProvider
import app.iot.net.response.BasicResponse
import app.iot.net.subscribers.DefaultObserver
import app.iot.net.subscribers.StringObserver
import app.iot.ui.BaseActivity
import app.iot.ui.LoginActivity
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by danbo on 2018/12/12.
 */
//当前用户token
var token: String = ""

//用户信息(为null表示未登录)
var dataInfo: DataInfo? = null

fun logout() {
    if(token.isNotEmpty()){
        Utils.getContext().let {
            RetrofitProvider.getApiService()
                .logout()
                .compose((it as BaseActivity<*>).bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : DefaultObserver<BasicResponse<Any>>(it, false) {
                    override fun onSuccess(response: BasicResponse<Any>) {
                    }
                })
        }
    }
    token = ""
    SPUtils.saveData(TOKEN, "")

    ActivityStackManager.getInstance().finishAllActivity()
}

fun refreshLatestVersion(context: Context, showLoading: Boolean, listener: SyncListener?) {
    if (TextUtils.isEmpty(token)) {
        return
    }

    context.let {
        RetrofitProvider.getApiService()
            .latestVersion()
            .compose((it as BaseActivity<*>).bindToLifecycle())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DefaultObserver<BasicResponse<DataVersion>>(it, showLoading) {
                override fun onSuccess(response: BasicResponse<DataVersion>) {
                    response.data?.apply {
                        val currentVersion =
                            SPUtils.getData(DATA_VERSION, "0") as String
                        if (version > currentVersion) {//有新版本数据去下载
                            StringRetrofitProvider.getApiService()
                                .latestData(version)
                                .compose(it.bindToLifecycle())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(object : StringObserver<String>(it, showLoading) {
                                    override fun onSuccess(response: String) {
                                        //保存最新数据文件
                                        SPUtils.saveData(DATA_JSON, StringUtil.replaceBlank(response) )

                                        //保存最新包
                                        SPUtils.saveData(DATA_VERSION, version)

                                        listener?.onSyncSuccess()
                                    }

                                    override fun onError(e: Throwable) {
                                        super.onError(e)
                                        listener?.onSyncFail(e.message)
                                    }
                                })
                        } else {//无新版本
                            SPUtils.saveData(DATA_VERSION, version)

                            listener?.onSyncSuccess()
                        }
                    }
                }

                override fun onFail(response: BasicResponse<DataVersion>) {
                    super.onFail(response)
                    listener?.onSyncFail(response.message)
                }

                override fun onException(reason: ExceptionReason) {
                    super.onException(reason)
                    listener?.onSyncFail(reason.name)
                }

            })
    }
}

interface SyncListener {
    fun onSyncSuccess() {}
    fun onSyncFail(reason: String?) {}
}


