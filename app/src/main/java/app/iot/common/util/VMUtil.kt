package app.iot.common.util

import java.lang.reflect.ParameterizedType

/**
 * Created by danbo on 2019-11-07.
 */
object VMUtil {
    fun <VM> getVM(o: Any): VM? {
        try {
            return ((o.javaClass
                .genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<VM>)
                .newInstance()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    fun forName(className: String): Class<*>? {
        try {
            return Class.forName(className)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }

        return null
    }
}