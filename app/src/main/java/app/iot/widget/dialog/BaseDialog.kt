package app.iot.widget.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import app.iot.R

/**
 * Created by danbo on 2019-11-13.
 */
abstract class BaseDialog(context: Context) : Dialog(context,  R.style.CommonDialog) {

    companion object{
        const val REQUEST_CODE_SCAN_EQUIP:Int = 222
        const val REQUEST_CODE_SCAN_DEVICE:Int = 333

        const val SCAN_EQUIP = 0
        const val SCAN_DEVICE = 1
    }

    abstract val layoutId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        //点击外部的话不让popuWindow消失
        setCanceledOnTouchOutside(false)

        initView()
    }

    protected abstract fun initView()
}
