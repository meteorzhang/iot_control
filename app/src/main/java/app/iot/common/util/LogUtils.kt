package app.iot.common.util

import android.util.Log

/**
 * Created by danbo on 2019-11-07.
 */
class LogUtils private constructor() {

    init {
        throw UnsupportedOperationException("cannot be instantiated")
    }

    companion object {
        private val defaultTag = "----------"
        private var logEnable: Boolean = app.iot.AppConfig.LOG_ENABLE
        private var logLevel = app.iot.AppConfig.LOG_LEVEL

        //0
        fun d(tag: String, msg: String) {
            if (logEnable && logLevel <= 0) {
                Log.d(tag, msg)
            }
        }

        fun d(msg: String) {
            if (logEnable && logLevel <= 0) {
                Log.d(defaultTag, msg)
            }
        }

        //1
        fun i(msg: String) {
            if (logEnable && logLevel <= 1) {
                Log.i(defaultTag, msg)
            }
        }

        fun i(tag: String, msg: String) {
            if (logEnable && logLevel <= 1) {
                Log.i(tag, msg)
            }
        }

        //2
        fun w(msg: String) {
            if (logEnable && logLevel <= 2) {
                Log.w(defaultTag, msg)
            }
        }

        fun w(tag: String, msg: String) {
            if (logEnable && logLevel <= 2) {
                Log.w(tag, msg)
            }
        }

        //3
        fun e(msg: String) {
            if (logEnable && logLevel <= 3) {
                Log.e(defaultTag, msg)
            }
        }

        fun e(tag: String, msg: String) {
            if (logEnable && logLevel <= 3) {
                Log.e(tag, msg)
            }
        }

    }
}
