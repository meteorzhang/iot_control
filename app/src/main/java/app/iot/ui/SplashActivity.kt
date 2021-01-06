package app.iot.ui

import SPUtils
import android.app.Dialog
import android.content.Intent
import android.os.Handler
import app.iot.*
import app.iot.AppConstant.SYNC_DATA
import app.iot.model.DataInfo
import app.iot.viewmodel.SplashViewModel
import app.iot.widget.dialog.DoubleButtonDialog
import app.iot.widget.toast.CommonToast
import com.google.gson.Gson

/**
 * Created by danbo on 2019/11/07.
 */
class SplashActivity : BaseActivity<SplashViewModel>(), SyncListener,
    DoubleButtonDialog.DialogClickListener {

    override fun getLayoutId(): Int {
        return R.layout.activity_spalash
    }

    override fun initView() {
        //在应用层就获取token同步用户信息
        token = SPUtils.getData(AppConstant.TOKEN, "") as String
        if (token.isNotEmpty()) {//token不为空 清空

            token = ""
            SPUtils.saveData(AppConstant.TOKEN, "")

            dataInfo = Gson().fromJson(
                SPUtils.getData(AppConstant.DATA_JSON, "{}") as String,
                DataInfo::class.java
            )
        }

        init()
    }

    private fun init() {
        Handler().postDelayed({
            if (token.isEmpty()) {//未登录
                jumpToLogin()
            } else {//已登录
                jumpToHome()
            }
        }, 3000)
    }

    private fun jumpToHome() {
        //token刷新成功,同步数据
        refreshLatestVersion(this, false, this)
    }

    private fun jumpToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun onSyncSuccess() {
        dataInfo = Gson().fromJson(
            SPUtils.getData(AppConstant.DATA_JSON, "{}") as String,
            DataInfo::class.java
        )

        //同步完成跳转至首页
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onSyncFail(reason: String?) {
        var identification = SPUtils.getData(SYNC_DATA, 0) as Int
        // 等这个值大于3次的时候，提示获取失败
        if (identification >= 3) {
            CommonToast.show("获取数据失败")
        } else {
            if (this.isDestroyed) {
                return
            }
            DoubleButtonDialog(
                this,
                R.drawable.ic_warning,
                null,
                "数据同步失败,请检查网络!",
                "退出",
                "重试",
                this
            ).show()
        }
        if (this.isDestroyed) {
            return
        }
        DoubleButtonDialog(
            this,
            R.drawable.ic_warning,
            null,
            "数据同步失败,请检查网络!",
            "退出",
            "重试",
            this
        ).show()
    }

    override fun onClick(dialog: Dialog) {
        //
        refreshLatestVersion(this, false, this)
    }

    override fun onCancel(dialog: Dialog) {
        // 在这里记录她的值
        var identification = SPUtils.getData(SYNC_DATA, 0) as Int
        identification++
        SPUtils.saveData(SYNC_DATA, identification) as Int
        finish()
    }

    override fun onBackPressed() {
        //super.onBackPressed()
    }

}
