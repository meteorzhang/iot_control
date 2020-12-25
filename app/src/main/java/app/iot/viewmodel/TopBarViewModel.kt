package app.iot.viewmodel

import android.view.View
import androidx.annotation.DrawableRes
import androidx.lifecycle.MutableLiveData
import app.iot.R

/**
 * Created by danbo on 2019-11-12.
 */
class TopBarViewModel(
    private val t: String,
    private val l: Int?,
    private val onTitleClickListener: OnTitleClickListener?
) : BaseViewModel() {

    // 带菜单TopBar
    constructor(
        t: String,
        @DrawableRes l: Int?,
        m: String,
        onTitleClickListener: OnTitleClickListener?
    ) : this(t, l, onTitleClickListener) {
        menu.value = m
    }

    var title = MutableLiveData<String>().apply {
        value = t
    }

    val logo = MutableLiveData<Int>().apply {
        value = l ?: R.drawable.ic_nav_back//default
    }

    val menu = MutableLiveData<String>().apply {
        value = ""
    }

    //是否显示返回按钮
    val logoEnable = MutableLiveData<Boolean>().apply {
        value = true
    }

    init {
        if (l == null) {
            logoEnable.value = false
        }
    }

    fun onLogoClick(view: View) {
        logoEnable.value?.let {
            if (it) {
                onTitleClickListener?.onLogoClick()
            }
        }
    }

    fun onMenuClick(view: View) {
        onTitleClickListener?.onMenuClick()
    }
}

interface OnTitleClickListener {
    fun onLogoClick() {}
    fun onMenuClick() {}
}