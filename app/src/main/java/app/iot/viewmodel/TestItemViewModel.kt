package app.iot.viewmodel

import androidx.lifecycle.MutableLiveData

/**
 * Created by danbo on 2019-11-07.
 */
class TestItemViewModel(item: String) : BaseViewModel() {

    val text = MutableLiveData<String>().apply {
        value = "This is test Fragment"
    }

    init {
        text.value = item
    }
}