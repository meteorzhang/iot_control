package app.iot.common.util

import android.Manifest
import android.content.pm.PackageManager
import android.provider.Settings

/**
 * Created by danbo on 2018/12/5.
 */
object AppInfoUtils {

    /**
     * 获取当前本地apk的版本
     *
     * @return
     */
    fun getVersionCode(): Int? {
        try {
            //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            return Utils.getContext()?.let {
                it.packageManager.getPackageInfo(it.packageName, 0).versionCode
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return -1
    }

    fun getVersionName(): String? {
        try {
            //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            return Utils.getContext()?.let {
                it.packageManager.getPackageInfo(it.packageName, 0).versionName
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return ""
    }

    private val permissions = listOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private fun hasLocationPermission(): Boolean {
        val checkMorePermissions =
            PermissionUtils.checkMorePermissions(Utils.getContext(), permissions.toTypedArray())
        if (checkMorePermissions.size > 0) {
            return false
        }
        return true
    }

    fun getDeviceId(): String {
        val androidId = Settings.System.getString(
            Utils.getContext()?.contentResolver,
            Settings.Secure.ANDROID_ID
        )

        return androidId
    }

}