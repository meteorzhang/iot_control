import android.content.Context
import app.iot.AppConstant.SP_NAME
import app.iot.common.util.Utils

/**
 * Created by danbo on 2019-11-07.
 */
object SPUtils {
    //存储的sharedPreferences文件名
    private const val FILE_NAME = SP_NAME

    /**
     * 保存数据到文件
     *
     * @param key
     * @param data
     */
    fun saveData(key: String, data: Any?) {

        val type = data?.javaClass?.simpleName
        val sharedPreferences = Utils.getContext()?.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()

        when (type) {
            "Integer" -> editor?.putInt(key, data as Int)
            "Boolean" -> editor?.putBoolean(key, data as Boolean)
            "String" -> editor?.putString(key, data as String)
            "Float" -> editor?.putFloat(key, data as Float)
            "Long" -> editor?.putLong(key, data as Long)
        }

        editor?.apply()
    }

    /**
     * 从文件中读取数据
     *
     * @param key
     * @param defValue
     * @return
     */
    fun getData(key: String, defValue: Any): Any? {

        val type = defValue.javaClass.simpleName
        val sharedPreferences = Utils.getContext()?.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

        //defValue为为默认值，如果当前获取不到数据就返回它
        return when (type) {
            "Integer" -> sharedPreferences?.getInt(key, defValue as Int)
            "Boolean" -> sharedPreferences?.getBoolean(key, defValue as Boolean)
            "String" -> sharedPreferences?.getString(key, defValue as String)
            "Float" -> sharedPreferences?.getFloat(key, defValue as Float)
            "Long" -> sharedPreferences?.getLong(key, defValue as Long)
            else -> defValue
        }
    }
}