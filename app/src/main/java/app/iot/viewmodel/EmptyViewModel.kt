package app.iot.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData

/**
 * Created by danbo on 2019-11-07.
 */
class EmptyViewModel(info: String,private val listener: OnClickListener?) : BaseViewModel() {

    val text = MutableLiveData<String>().apply {
        value = "还没有数据!"
    }

    init {
        text.value = info
    }

    fun click(view:View){
        listener?.click()
    }

    interface OnClickListener {
        fun click()
    }
}