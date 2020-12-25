package app.iot.viewmodel

import app.iot.R
import app.iot.common.util.AppInfoUtils

/**
 * Created by danbo on 2019-11-27.
 */
class AboutUsViewModel : BaseViewModel(), OnTitleClickListener {

    val titleViewModel = TopBarViewModel("关于", R.drawable.ic_nav_back, "", this)

    val version = AppInfoUtils.getVersionName()

    override fun onLogoClick() {
        finish()
    }

}