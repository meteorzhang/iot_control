package app.iot.ui

import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import app.iot.*
import app.iot.model.AuthModel
import app.iot.net.RetrofitProvider
import app.iot.net.request.AuthRequest
import app.iot.net.response.BasicResponse
import app.iot.net.subscribers.DefaultObserver
import app.iot.viewmodel.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_settings.*


class SettingsActivity : BaseActivity<BaseViewModel>() {

    override fun getLayoutId(): Int {
        return R.layout.activity_settings
    }

    override fun initView() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(true)
        }

        toolbar.setNavigationOnClickListener { finish() }

    }


}