package app.iot.viewmodel

import app.iot.R

class WebViewViewModel : BaseViewModel(), OnTitleClickListener {

    val titleViewModel = TopBarViewModel("", R.drawable.ic_nav_back, "", this)

    override fun onLogoClick() {
        super.onLogoClick()
        finish()
    }

}