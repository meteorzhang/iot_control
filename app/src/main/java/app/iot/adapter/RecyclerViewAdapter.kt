package app.iot.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import app.iot.common.util.LogUtils
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter

private const val TAG = "RecyclerView"

class RecyclerViewAdapter<T> : BindingRecyclerViewAdapter<T>() {

    override fun onCreateBinding(
        inflater: LayoutInflater, @LayoutRes layoutId: Int,
        viewGroup: ViewGroup
    ): ViewDataBinding {
        return super.onCreateBinding(inflater, layoutId, viewGroup).apply {
            LogUtils.d(TAG, "created binding: $this")
        }
    }

    override fun onBindBinding(
        binding: ViewDataBinding,
        variableId: Int, @LayoutRes layoutRes: Int,
        position: Int,
        item: T
    ) {
        super.onBindBinding(binding, variableId, layoutRes, position, item)
        LogUtils.d(TAG, "bound binding: $binding at position: $position")
    }
}