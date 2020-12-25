package app.iot.common.util

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import app.iot.ui.BaseActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

/**
 * Created by danbo on 2019-11-28.
 */
object GlideUtil {

    fun display(url: String?, view: ImageView) {
        if ((view.context as BaseActivity<*>).isDestroyed) {
            return
        }
        Glide.with(view.context as BaseActivity<*>).load(url).into(view)
    }

    fun display(res: Int, view: ImageView) {
        Glide.with(view.context).load(res).into(view)
    }

    fun displayRound(url: String?, view: ImageView) {
        if ((view.context as BaseActivity<*>).isDestroyed) {
            return
        }
        Glide.with(view.context as BaseActivity<*>).load(url)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .into(view)
    }

    fun displayArc(url: String?, view: ImageView) {
        if ((view.context as BaseActivity<*>).isDestroyed) {
            return
        }
        Glide.with(view.context as BaseActivity<*>).load(url)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(16)))
            .into(view)
    }

    fun display(data: ByteArray?, view: ImageView) {
        if ((view.context as BaseActivity<*>).isDestroyed) {
            return
        }
        data?.let {
            AsyncManager.me().execute {
                val bitmap = BitmapFactory.decodeByteArray(data, 0, it.size)
                view.apply {
                    (context as BaseActivity<*>).runOnUiThread {
                        setBackgroundDrawable(BitmapDrawable(bitmap))
                    }
                }

            }
        }

    }

}