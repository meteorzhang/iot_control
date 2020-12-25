package app.iot

import SPUtils
import androidx.multidex.MultiDexApplication
import app.iot.common.util.Utils
import app.iot.library.DBManager
import app.iot.model.DataInfo
import com.google.gson.Gson
import com.inuker.bluetooth.library.BluetoothClient

/**
 * Created by danbo on 2019-11-07.
 */
class IOTApplication : MultiDexApplication() {

    //  创建单例
    private object SingletonHolder {
        val INSTANCE = IOTApplication()
    }

    var btClient: BluetoothClient? = null

    companion object {
        fun getApplication(): IOTApplication {
            return SingletonHolder.INSTANCE
        }
    }

    override fun onCreate() {
        super.onCreate()
        Utils.init(this)

        //初始化数据库
        DBManager.getInstance().setup(this)

        getApplication().btClient = BluetoothClient(this)

        //在应用层就获取token同步用户信息
        token = SPUtils.getData(AppConstant.TOKEN, "") as String
        if (token.isNotEmpty()) {
            dataInfo = Gson().fromJson(
                SPUtils.getData(AppConstant.DATA_JSON, "{}") as String,
                DataInfo::class.java
            )
        }

    }

}