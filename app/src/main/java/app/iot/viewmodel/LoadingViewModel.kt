package app.iot.viewmodel

import android.view.View
import androidx.databinding.ObservableField


/**
 * Created by danbo on 2019-11-07.
 */
class LoadingViewModel(val listener: OnRetryListener?) : BaseViewModel() {
    //title 的ViewModel
    val isLoading = ObservableField<Boolean>().apply {
        set(false)
    }

    val loadingInfo = ObservableField<String>().apply {
        set("加载中...")
    }

    fun retry(view: View) {
        listener?.retry()
    }

    interface OnRetryListener {
        fun retry()
    }
}