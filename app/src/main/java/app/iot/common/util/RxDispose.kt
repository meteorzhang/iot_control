package app.iot.common.util

import android.content.Context
import android.content.Intent
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

/**
 * Created by danbo on 2019/5/9.
 */
class RxDispose {
    companion object {
        private var disposable: Disposable? = null  //tip定时器

        //循环
        fun startDispose(context: Context) {
            if (disposable == null) {
                disposable = Observable.interval(0, 3, TimeUnit.SECONDS)
                    .map { t -> t + 1 }
                    .subscribe {

                    }
            }

        }

        fun cancelDispose() {
            disposable?.dispose()
            disposable = null
        }
    }

}