package app.iot.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.MenuRes
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import app.iot.ActivityStackManager
import app.iot.AppConstant.REQUEST_CODE
import app.iot.BR
import app.iot.R
import app.iot.common.BaseView
import app.iot.common.util.PermissionUtils
import app.iot.common.util.Utils
import app.iot.common.util.VMUtil
import app.iot.viewmodel.BaseViewModel
import app.iot.widget.dialog.BaseDialog
import app.iot.widget.dialog.BaseDialog.Companion.SCAN_DEVICE
import app.iot.widget.dialog.BaseDialog.Companion.SCAN_EQUIP
import app.iot.widget.toast.CommonToast
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.xuexiang.xqrcode.XQRCode


/**
 * Created by danbo on 2019/11/07.
 */
abstract class BaseActivity<VM : BaseViewModel> : RxAppCompatActivity(), BaseView {
    companion object {
        val PERMISSIONS = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.P -> listOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.WAKE_LOCK,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.FOREGROUND_SERVICE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA
            )
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> listOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.WAKE_LOCK,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA
            )
            else -> listOf(
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA
            )
        }
    }

    fun setStatusBarColor(statusColor: Int) {
        //取消状态栏透明
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        //添加Flag把状态栏设为可绘制模式
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        //设置状态栏颜色
        window.statusBarColor = statusColor
        //设置系统状态栏处于可见状态
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        //让view不根据系统窗口来调整自己的布局
        val mContentView = window.findViewById(Window.ID_ANDROID_CONTENT) as ViewGroup
        val mChildView: View? = mContentView.getChildAt(0)
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, false)
            ViewCompat.requestApplyInsets(mChildView)
        }
    }

    open lateinit var mBinding: ViewDataBinding

    open var mViewModel: VM? = null

    var listener: ActivityResultListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)

        //状态栏透明
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = resources.getColor(R.color.navigationBarColor)

        Utils.init(this)

        ActivityStackManager.getInstance().addActivity(this)

        val layoutId = getLayoutId()
        if (layoutId == 0) {
            return
        }

        // Obtain ViewModel from ViewModelProviders
        val vm = VMUtil.getVM<VM>(this)
        vm?.let {
            mViewModel = ViewModelProviders.of(this).get(vm::class.java)
        }
        //bind context
        mViewModel?.context = this

        // Obtain binding
        mBinding =
            DataBindingUtil.setContentView(this, layoutId)

        //bind ViewModel
        mBinding.setVariable(getBR(), mViewModel)

        // LiveData needs the lifecycle owner
        mBinding.lifecycleOwner = this

        //优先通过权限
        checkPermissions()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (PermissionUtils.checkMorePermissions(this, PERMISSIONS.toTypedArray()).size > 0) {

            CommonToast.middleShow("为保证程序功能正常使用,请同意授权请求")
            Handler().postDelayed({
                //继续请求
                checkPermissions()
            }, 2000)
        } else {
            //授予成功
            initView()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 扫描二维码/条码回传
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val type = when (requestCode) {
                    BaseDialog.REQUEST_CODE_SCAN_EQUIP -> {
                        SCAN_EQUIP
                    }
                    BaseDialog.REQUEST_CODE_SCAN_DEVICE -> {
                        SCAN_DEVICE
                    }
                    else -> {
                        -1
                    }
                }

                val bundle = data.extras
                if (bundle != null) {
                    if (bundle.getInt(XQRCode.RESULT_TYPE) == XQRCode.RESULT_SUCCESS) {
                        val result = bundle.getString(XQRCode.RESULT_DATA)
                        listener?.onScanResult(type, result)
                    } else if (bundle.getInt(XQRCode.RESULT_TYPE) == XQRCode.RESULT_FAILED) {
                        CommonToast.middleShow("解析二维码失败")
                    }
                }

            }
        }
    }

    //1
    private fun checkPermissions() {
        PermissionUtils.checkAndRequestMorePermissions(
            this,
            PERMISSIONS.toTypedArray(),
            REQUEST_CODE
        ) { initView() }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (menuResId() != 0) {
            menuInflater.inflate(menuResId(), menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun getBR(): Int {
        return BR.viewModel
    }

    @MenuRes
    protected open fun menuResId(): Int {
        return 0
    }

    /**
     * 创建Fragment
     */
    fun startFragment(fragment: Fragment?, @IdRes container: Int) {
        if (fragment == null) {
            return
        }

        supportFragmentManager.beginTransaction()
            .replace(container, fragment).commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityStackManager.getInstance().removeActivity(this)
    }

    interface ActivityResultListener {
        fun onScanResult(type: Int, result: String?)
    }
}
