package app.iot.viewmodel

import app.iot.*
import app.iot.common.util.LogUtils
import app.iot.protocol.*
import app.iot.ui.fragment.*
import com.inuker.bluetooth.library.utils.ByteUtils

class DeviceDetailViewModel : BaseViewModel(), OnTitleClickListener {

    val titleViewModel = TopBarViewModel("设备详情", R.drawable.ic_nav_back, "", this)

    //处理蓝牙协议
    fun init(protocol: ByteArray) {
        startFragment(ProtocolParserFragment(protocol), R.id.container)
    }

    override fun onLogoClick() {
        super.onLogoClick()
        finish()
    }

}
