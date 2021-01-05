package app.iot.ext

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

//绑定普通的Recyclerview
fun RecyclerView.initVertical(
    bindAdapter: RecyclerView.Adapter<*>,
    isScroll: Boolean = true,
): RecyclerView {
    var lm = LinearLayoutManager(context)
    layoutManager = lm
    setHasFixedSize(true)
    adapter = bindAdapter
    isNestedScrollingEnabled = isScroll
    return this
}