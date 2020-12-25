package app.iot.ui

import androidx.appcompat.widget.Toolbar
import app.iot.R
import app.iot.viewmodel.BaseViewModel


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