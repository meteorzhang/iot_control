package app.iot.viewmodel

import SPUtils
import android.app.Dialog
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.MutableLiveData
import app.iot.*
import app.iot.AppConstant.ACCOUNT
import app.iot.AppConstant.TOKEN
import app.iot.model.AuthModel
import app.iot.model.DataInfo
import app.iot.net.RetrofitProvider
import app.iot.net.request.AuthRequest
import app.iot.net.response.BasicResponse
import app.iot.net.subscribers.DefaultObserver
import app.iot.ui.BaseActivity
import app.iot.ui.MainActivity
import app.iot.widget.SimpleTextWatcher
import app.iot.widget.dialog.DoubleButtonDialog
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by danbo on 2019-11-07.
 */
class LoginViewModel : BaseViewModel(), OnTitleClickListener, SyncListener,
    DoubleButtonDialog.DialogClickListener {

    val titleViewModel = TopBarViewModel("登录", null, "", this)

    //密码输入区是否可见
    var passwordEnable = MutableLiveData<Boolean>().apply {
        value = true
    }
    var commitEnable = MutableLiveData<Boolean>().apply {
        value = false
    }
    var account = MutableLiveData<String>().apply {
        value = ""
    }
    var password = MutableLiveData<String>().apply {
        if (!BuildConfig.DEBUG) {
            value = ""
        } else {
            value = "appTest1019"
        }
    }

    init {
        account.value = SPUtils.getData(ACCOUNT, "") as String
    }

    fun cleanAccount(view: View) {
        //清空账号取消保存
        cleanSavedAccount()
    }

    private fun cleanSavedAccount() {
        account.value = ""
        password.value = ""

        SPUtils.saveData(ACCOUNT, "")
    }

    var editAccountWatcher: TextWatcher = object : SimpleTextWatcher() {
        override fun afterTextChanged(s: Editable?) {
            passwordVisible.value = passwordVisible.value

            if (account.value.isNullOrEmpty() || account.value?.length != 11) {
                commitEnable.value = false
            } else {
                if (!password.value.isNullOrEmpty()) {
                    commitEnable.value = true
                }
            }

        }
    }

    var editPasswordWatcher: TextWatcher = object : SimpleTextWatcher() {
        override fun afterTextChanged(s: Editable?) {
            commitEnable.value = !password.value.isNullOrEmpty()
        }
    }

    //密码是否可见
    var passwordVisible = MutableLiveData<Boolean>().apply {
        value = false
    }

    fun swapVisible(view: View) {
        passwordVisible.value?.let {
            passwordVisible.value = !it
        }
    }

    fun commit(view: View) {
        if (!BuildConfig.DEBUG) {
            if (!commitEnable.value!!) {
                return
            }
        }

        login()
    }

    private fun login() {
        (context as BaseActivity<*>).let {
            val authRequest = AuthRequest(account.value, password.value)
            RetrofitProvider.getApiService()
                .auth(authRequest.getRequestBody())
                .compose(it.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : DefaultObserver<BasicResponse<AuthModel>>(it, true) {
                    override fun onSuccess(response: BasicResponse<AuthModel>) {

                        response.data?.let {
                            token = it.token//登录token不为null

                            SPUtils.saveData(ACCOUNT, account.value)
                            SPUtils.saveData(TOKEN, token)

                            //token刷新成功,同步数据
                            refreshLatestVersion(context!!, true, this@LoginViewModel)
                        }
                    }
                })
        }
    }

    override fun onSyncSuccess() {
        dataInfo = Gson().fromJson(
            SPUtils.getData(AppConstant.DATA_JSON, "{}") as String,
            DataInfo::class.java
        )

        //同步完成跳转至首页
        startActivity(MainActivity::class.java)
        finish()
    }

    override fun onSyncFail(reason: String?) {
        DoubleButtonDialog(
            context!!,
            R.drawable.ic_warning,
            null,
            "数据同步失败,请检查网络!",
            "退出",
            "重试",
            this
        ).show()
    }

    //重试
    override fun onClick(dialog: Dialog) {
        refreshLatestVersion(context!!, true, this@LoginViewModel)
    }

    override fun onCancel(dialog: Dialog) {
        finish()
    }

}