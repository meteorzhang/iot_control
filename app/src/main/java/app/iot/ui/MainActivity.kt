package app.iot.ui

import android.app.Dialog
import android.content.Intent
import app.iot.IOTApplication
import app.iot.R
import app.iot.logout
import app.iot.token
import app.iot.ui.fragment.HostFragment
import app.iot.viewmodel.MainViewModel
import app.iot.widget.dialog.DoubleButtonDialog
import app.iot.widget.dialog.SingleButtonDialog
import com.inuker.bluetooth.library.utils.BluetoothUtils
import kotlinx.android.synthetic.main.activity_main.*


/**
 * Created by danbo on 2019/11/07.
 */
class MainActivity : BaseActivity<MainViewModel>() {

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        if (token.isEmpty()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        if (!IOTApplication.getApplication().btClient!!.isBleSupported || !BluetoothUtils.isBleSupported()) {
            SingleButtonDialog(
                this,
                R.drawable.ic_warning,
                null,
                "当前设备不支持蓝牙",
                "我知道了",
                object : SingleButtonDialog.DialogClickListener {
                    override fun onOk(dialog: Dialog) {
                        super.onOk(dialog)
                        finish()
                    }
                }
            ).show()
            return
        }
        if (!IOTApplication.getApplication().btClient!!.isBluetoothOpened) {
            DoubleButtonDialog(
                this,
                R.drawable.ic_power,
                null,
                "蓝牙未启用!",
                "退出",
                "启用",
                object : DoubleButtonDialog.DialogClickListener {
                    override fun onClick(dialog: Dialog) {
                        super.onClick(dialog)
                        IOTApplication.getApplication().btClient?.openBluetooth()
                    }

                    override fun onCancel(dialog: Dialog) {
                        super.onCancel(dialog)
                        finish()
                    }
                }
            ).show()
        }


        nav_view.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {

                }
                R.id.nav_record -> {
                    mViewModel?.startActivity(OperateRecordActivity::class.java)
                }
                R.id.nav_setting -> {
                    mViewModel?.startActivity(SettingsActivity::class.java)
                }

                R.id.nav_about -> {
                    mViewModel?.startActivity(AboutUsActivity::class.java)
                }
            }
            false
        }

        mViewModel?.startFragment(HostFragment(), R.id.container)
    }


    var isPaused = false
    override fun onResume() {
        super.onResume()
        isPaused = false

    }

    override fun onPause() {
        super.onPause()
        isPaused = true
    }

    override fun onBackPressed() {

        DoubleButtonDialog(
            this,
            null,
            "退出",
            "确认退出软件?",
            "取消",
            "确认",
            object : DoubleButtonDialog.DialogClickListener {
                override fun onClick(dialog: Dialog) {
                    super.onClick(dialog)
                    logout()
                }
            }
        ).show()
    }

}