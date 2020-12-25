package app.iot.viewmodel

import app.iot.*

class BindViewModel : BaseViewModel(), OnTitleClickListener{

    val titleViewModel = TopBarViewModel("选择装备", R.drawable.ic_nav_back, "", this)

    override fun onLogoClick() {
        super.onLogoClick()
        finish()
    }

}
