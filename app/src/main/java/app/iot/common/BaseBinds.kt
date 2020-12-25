package app.iot.common

import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.text.method.SingleLineTransformationMethod
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.*
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import app.iot.R
import app.iot.adapter.ProtocolDetailAdapter
import app.iot.adapter.RecyclerViewAdapter
import app.iot.common.util.GlideUtil
import app.iot.library.log.entity.OperateType
import app.iot.model.*
import app.iot.ui.BindActivity
import app.iot.viewmodel.BaseViewModel
import app.iot.viewmodel.EquipViewModel
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by danbo on 2019-11-07.
 */
@BindingAdapter("url")
fun bindImageView(imageView: ImageView, url: String?) {
    url?.let {
        if (url.startsWith("http", true)) {
            GlideUtil.display(url, imageView)
        }
    }
}

@BindingAdapter("round")
fun bindRoundImageView(imageView: ImageView, url: String?) {
    url?.let {
        if (url.startsWith("http", true)) {
            GlideUtil.displayRound(url, imageView)
        }
    }
}

@BindingAdapter("arc")
fun bindArcImageView(imageView: ImageView, url: String?) {
    url?.let {
        if (url.startsWith("http", true)) {
            GlideUtil.displayArc(url, imageView)
        }
    }
}

//给ImageView加载本地资源图片
@BindingAdapter("drawable")
fun bindDrawable(imageView: ImageView, drawableId: Int) {
    if (drawableId > 0)
        imageView.setImageResource(drawableId)
}

//给ImageView加载本地资源图片
@BindingAdapter("anim")
fun bindAnim(imageView: ImageView, anim: Boolean) {
    if (anim) {
        imageView.visibility = View.VISIBLE
        imageView.startAnimation(getRotateAnimation())
    } else {
        imageView.clearAnimation()
        imageView.visibility = View.GONE
    }
}

//旋转动画
private fun getRotateAnimation(): RotateAnimation {
    val animation = RotateAnimation(
        0f, 359f, Animation.RELATIVE_TO_SELF,
        0.5f, Animation.RELATIVE_TO_SELF, 0.5f
    )
    animation.duration = 800
    animation.fillAfter = true
    //匀速
    animation.interpolator = LinearInterpolator()
    animation.repeatCount = ValueAnimator.INFINITE
    return animation
}

/**
 * inputType绑定
 */
@BindingAdapter("bindInputType")
fun bindInputType(editText: EditText, isShowPassword: Boolean) {
    editText.typeface = Typeface.SANS_SERIF
    editText.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
    editText.setSelection(editText.length())
    editText.maxLines = 1

    if (isShowPassword) {
        editText.transformationMethod = SingleLineTransformationMethod()
    } else {
        editText.transformationMethod = PasswordTransformationMethod()
    }
}

/**
 * 刷新监听绑定
 */
@BindingAdapter("bindRefreshListener")
fun bindRefreshListener(refreshLayout: SwipeRefreshLayout, viewModel: BaseViewModel) {
    refreshLayout.setColorSchemeResources(R.color.colorAccent)

    viewModel.refreshLayout = refreshLayout
    refreshLayout.setOnRefreshListener(viewModel)
}

/**
 * Drawable Tint颜色
 */
@BindingAdapter("drawableTint")
fun bindDrawableTint(textView: TextView, green: Boolean) {
    textView.compoundDrawableTintList =
        if (green) {
            ColorStateList.valueOf(textView.context.getColor(R.color.textColorGreen))
        } else {
            ColorStateList.valueOf(textView.context.getColor(R.color.colorRed))
        }
}

@BindingAdapter("normalDrawableTint")
fun bindNormalDrawableTint(textView: TextView, green: Boolean) {
    textView.compoundDrawableTintList =
        if (green) {
            ColorStateList.valueOf(textView.context.getColor(R.color.textColorGreen))
        } else {
            ColorStateList.valueOf(textView.context.getColor(R.color.textColorLightHint))
        }
}

/**
 * Spinner entries下拉列表
 */
@BindingAdapter("entries")
fun bindSpinnerEntries(spinner: Spinner, items: List<*>?) {
    if (items == null) {
        return
    }
    var selection = 0
    val strings = arrayListOf<String>()
    for (item in items) {
        when (item) {
            is EquipmentType -> {
                strings.add(item.codeKeyName)
                selection = BindActivity.equipTypeIndex
            }
            is EquipmentModel -> {
                strings.add(item.modelName)
                selection = BindActivity.equipModelIndex
            }
            is DeviceType -> {
                strings.add(item.codeKeyName)
                selection = BindActivity.deviceTypeIndex
            }
            is DeviceModel -> {
                strings.add(item.modelName)
                selection = BindActivity.deviceModelIndex
            }
        }
    }

    val adapter = ArrayAdapter(spinner.context, android.R.layout.simple_spinner_item, strings)
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    spinner.adapter = adapter
    spinner.setSelection(selection)
}

/**
 * Spinner 元素选择
 */
@BindingAdapter("onItemSelected")
fun bindSpinnerOnItemSelected(spinner: Spinner, viewModel: EquipViewModel) {
    when (spinner.id) {
        R.id.equip_type_spinner -> {
            spinner.onItemSelectedListener = viewModel.equipTypeSelectListener
        }
        R.id.equip_model_spinner -> {
            spinner.onItemSelectedListener = viewModel.equipModelSelectListener
        }
        R.id.device_type_spinner -> {
            spinner.onItemSelectedListener = viewModel.deviceTypeSelectListener
        }
        R.id.device_model_spinner -> {
            spinner.onItemSelectedListener = viewModel.deviceModelSelectListener
        }
    }
}

//给ImageView加载本地资源图片
@BindingAdapter("date")
fun bindDate(textView: TextView, mills: Long?) {
    mills?.let {
        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date(mills * 1000))
        textView.text = date
    }
}

@BindingAdapter("mills")
fun bindMillls(textView: TextView, mills: Long?) {
    mills?.let {
        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date(mills))
        textView.text = date
    }
}

@BindingAdapter("operateType")
fun bindOperateType(textView: TextView, type: Int?) {
    type?.let {
        textView.text = when (it) {
            OperateType.BIND.value -> {
                OperateType.BIND.title
            }
            OperateType.UNBIND.value -> {
                OperateType.UNBIND.title
            }
            OperateType.SWITCH.value -> {
                OperateType.SWITCH.title
            }
            OperateType.CHECK.value -> {
                OperateType.CHECK.title
            }
            else -> {
                "未知操作"
            }
        }

    }
}


@BindingAdapter("recyclerView")
fun bindRecyclerView(recyclerView: RecyclerView, details: List<ProtocolDetail>?) {
    details?.let {
        if (it.isNotEmpty()) {
            val layoutManager = LinearLayoutManager(recyclerView.context)
            recyclerView.layoutManager = layoutManager
            val adapter = ProtocolDetailAdapter(details)
            recyclerView.adapter = adapter
        }
    }
}
