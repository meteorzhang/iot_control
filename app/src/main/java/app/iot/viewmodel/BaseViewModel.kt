package app.iot.viewmodel


import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import app.iot.AppConstant.BUNDLE
import app.iot.ui.BaseActivity
import java.io.Serializable


/**
 * Created by danbo on 2019-11-07.
 */
open class BaseViewModel : ViewModel(), SwipeRefreshLayout.OnRefreshListener,
    View.OnLongClickListener {
    var context: Context? = null

    var refreshLayout: SwipeRefreshLayout? = null

    open fun getNetworkData() {
        refreshLayout?.isRefreshing = false
    }

    open fun startActivity(cls: Class<*>) {
        startActivity(cls, null, null)
    }

    open fun startActivity(cls: Class<*>, key: String?, value: Any?) {
        context?.let {
            val intent = Intent(it, cls)
            if (value is String && !TextUtils.isEmpty(value) && !TextUtils.isEmpty(key)) {
                intent.putExtra(key, value)
            } else if (!TextUtils.isEmpty(key) && value != null && value is Serializable) {
                intent.putExtra(key, value)
            }
            it.startActivity(intent)
        }

    }

    open fun startActivityForResult(cls: Class<*>, requestCode: Int) {
        startActivityForResult(cls, requestCode, null, null)
    }

    open fun startActivityForResult(cls: Class<*>, requestCode: Int, key: String?, value: Any?) {
        context?.let {
            val intent = Intent(it, cls)
            if (value is String && !TextUtils.isEmpty(value) && !TextUtils.isEmpty(key)) {
                intent.putExtra(key, value)
            } else if (!TextUtils.isEmpty(key) && value != null && value is Serializable) {
                intent.putExtra(key, value)
            }

            ((scanForActivity(it)) as BaseActivity<*>).startActivityForResult(intent, requestCode)
        }

    }

    private  fun scanForActivity(cont: Context?): Activity? {
        return when (cont) {
            null -> null
            is Activity -> cont
            is ContextWrapper -> scanForActivity(
                cont.baseContext
            )
            else -> null
        }
    }
    open fun startActivity(cls: Class<*>, bundle: Bundle) {
        val intent = Intent(context, cls)
        intent.putExtra(BUNDLE, bundle)
        ((scanForActivity(context)) as BaseActivity<*>).startActivity(intent)
    }

    open fun startActivityForResult(cls: Class<*>, requestCode: Int, bundle: Bundle) {
        val intent = Intent(context, cls)
        intent.putExtra(BUNDLE, bundle)
        ((scanForActivity(context)) as BaseActivity<*>).startActivityForResult(intent, requestCode)
    }

    fun startFragment(fragment: Fragment?, @IdRes layout: Int) {
        if (fragment == null) {
            return
        }
        ((scanForActivity(context)) as BaseActivity<*>).apply {
            supportFragmentManager.beginTransaction()
                .replace(layout, fragment).commit()
        }

    }

    open fun getIntent(): Intent {
        return ((scanForActivity(context)) as BaseActivity<*>).intent
    }

    open fun setResult(code: Int) {
        ((scanForActivity(context)) as BaseActivity<*>).setResult(code)
    }

    open fun finish() {
        ((scanForActivity(context)) as BaseActivity<*>).finish()
    }

    override fun onRefresh() {
    }

    override fun onLongClick(v: View): Boolean {
        return true
    }

}