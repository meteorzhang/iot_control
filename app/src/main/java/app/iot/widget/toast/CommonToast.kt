package app.iot.widget.toast

import android.view.Gravity
import android.widget.Toast
import app.iot.common.util.Utils


/**
 * CustomToast
 */
object CommonToast {

    fun show(resId: Int) {
        show(Utils.getContext()?.resources?.getText(resId), Toast.LENGTH_SHORT)
    }

    fun show(resId: Int, duration: Int) {
        show(Utils.getContext()?.resources?.getText(resId), duration)
    }


    fun show(text: CharSequence?, duration: Int = Toast.LENGTH_SHORT) {
        middleShow(text)
    }

    fun middleShow(text: CharSequence?) {
        if (text.isNullOrEmpty()) {
            return
        }
        val toast = Toast.makeText(
            Utils.getContext(),
            text, Toast.LENGTH_SHORT
        )
        toast.setGravity(Gravity.CENTER, 0, 0)

        //toastLayout.setBackgroundResource(R.mipmap.toast_bg)
        toast.show()
    }
}
