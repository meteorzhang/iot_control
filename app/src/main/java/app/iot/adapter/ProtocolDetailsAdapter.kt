package app.iot.adapter

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.ObservableField
import app.iot.R
import app.iot.model.ProtocolDetail
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder


class ProtocolDetailsAdapter :
    BaseQuickAdapter<ProtocolDetail, BaseViewHolder>(R.layout.layout_protocol_detail_item) {
    val details = ObservableField<List<ProtocolDetail>>()

    override fun convert(holder: BaseViewHolder, data: ProtocolDetail) {
        holder.getView<LinearLayout>(R.id.ll_details_layout).visibility = View.VISIBLE
        holder.setText(R.id.name, data.desc)
        var tvState = holder.getView<TextView>(R.id.state)
        val status: Int? = data.status
        if (status != null) {
            if (status == 0) {
                tvState.text = ("故障")
                tvState.setTextColor(context.getColor(R.color.colorRed))
            } else {
                tvState.text = ("正常")
                tvState.setTextColor(context.getColor(R.color.textColorGreen))
            }
        }

    }


}