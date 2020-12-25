package app.iot.common.util

import android.annotation.SuppressLint
import android.content.Context


/**
 * Created by danbo on 2019-11-07.
 */
@SuppressLint("StaticFieldLeak")
object Utils {

    private var context: Context? = null

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    fun init(context: Context) {
        this.context = context
    }

    /**
     * 获取AContext
     *
     * @return Context
     */
    fun getContext(): Context? {
        if (context != null) return context
        throw NullPointerException("u should init first")
    }

}
