package app.iot.viewmodel

import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.iot.adapter.RecyclerViewAdapter
import app.iot.common.util.Utils
import app.iot.widget.MyGridLayoutManager
import app.iot.widget.MyLinearLayoutManager
import me.tatarka.bindingcollectionadapter2.BR
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter
import me.tatarka.bindingcollectionadapter2.ItemBinding
import me.tatarka.bindingcollectionadapter2.collections.MergeObservableList

class RecyclerViewViewModel<M>(
    val listener: Listener<*>?,
    layoutId: Int,
    managerType: Int,
    val spanCount: Int?
) :
    BaseViewModel() {

    companion object {
        val NO_SCROLL_GRIDLAYOUTMANAGER = 0//0: 不可滑动的GrideLayout；
        val NORMAL_GRIDLAYOUTMANAGER = 1//1：可滑动的GrideLayout
        val VERTICAL_LINEARLAYOUTMANAGER = 2//  2：竖直的线性的LinearLayoutManager
        val HORIZONTAL_LINEARLAYOUTMANAGER = 3//  2：水平的线性的LinearLayoutManager
        val HORIZONTAL_GRIDLAYOUTMANAGER = 4//  2：水平的线性的GRIDLAYOUTMANAGER
        val NO_SCROLL_VERTICAL_LINEARLAYOUTMANAGER = 5//5: 水平的线性不可滑动的LinearLayout；
        val NO_SCROLL_HORIZONTAL_LINEARLAYOUTMANAGER = 6//6: 垂直的线性不可滑动的LinearLayout；
    }

    var layoutManager: RecyclerView.LayoutManager = when (managerType) {
        NO_SCROLL_GRIDLAYOUTMANAGER -> {
            MyGridLayoutManager(Utils.getContext(), spanCount!!)
        }
        NORMAL_GRIDLAYOUTMANAGER -> {
            GridLayoutManager(Utils.getContext(), spanCount!!)
        }
        VERTICAL_LINEARLAYOUTMANAGER -> {
            LinearLayoutManager(Utils.getContext(), LinearLayoutManager.VERTICAL, false)
        }
        HORIZONTAL_LINEARLAYOUTMANAGER -> {
            LinearLayoutManager(Utils.getContext(), LinearLayoutManager.HORIZONTAL, false)
        }
        HORIZONTAL_GRIDLAYOUTMANAGER -> {
            GridLayoutManager(Utils.getContext(), spanCount!!, GridLayoutManager.HORIZONTAL, false)
        }
        NO_SCROLL_VERTICAL_LINEARLAYOUTMANAGER -> {
            MyLinearLayoutManager(Utils.getContext()!!, GridLayoutManager.VERTICAL, false)
        }
        NO_SCROLL_HORIZONTAL_LINEARLAYOUTMANAGER -> {
            MyLinearLayoutManager(Utils.getContext()!!, GridLayoutManager.HORIZONTAL, false)
        }
        else -> {
            LinearLayoutManager(Utils.getContext())
        }
    }

    //ViewModel用
    val item: ObservableArrayList<M> = ObservableArrayList()
    //布局中用到
    val items = MergeObservableList<Any>()
        .insertList(item)!!

    /**
     * Items merged with a header on top and footer on bottom.
     */
    val headerFooterItems = MergeObservableList<Any>()
        .insertItem("Header")
        .insertList(items)
        .insertItem("Footer")

    /**
     * Custom adapter that logs calls.
     */
    val adapter = RecyclerViewAdapter<Any>()
    var itemBinding = ItemBinding.of<Any>(BR.itemViewModel, layoutId)

//    val multipleItems = ItemBinding.of<Any> { itemBinding, _, item ->
//        when (item::class) {
//            String::class -> itemBinding.set(BR.item, R.layout.item_header_footer)
//            MutableItem::class -> itemBinding.set(BR.item, R.layout.item)
//        }
//    }.bindExtra(BR.item, this)

    /**
     * Custom view holders for RecyclerView
     */
    val viewHolder =
        BindingRecyclerViewAdapter.ViewHolderFactory { binding -> MyAwesomeViewHolder(binding.root) }


    private class MyAwesomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}

/**
 * item点击事件的监听
 */
interface Listener<V> {
    fun onItemClickListener(v: V?, position: Int) {
    }
}
