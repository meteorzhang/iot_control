package app.iot.adapter

import android.graphics.Color
import android.widget.LinearLayout
import android.widget.TextView
import app.iot.R
import app.iot.common.bindOperateType
import app.iot.entity.RecordData
import app.iot.model.Protocol
import app.iot.viewmodel.OperateRecordItemViewModel
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder


class OperationRecordAdapter :
    BaseQuickAdapter<Protocol, BaseViewHolder>(R.layout.layout_protocol_item) {

    override fun convert(holder: BaseViewHolder, data: Protocol) {
//        holder.setText(R.id.tv_record_time, "${data.item.time}")
//            .setText(R.id.tv_record_account, data.account)
//            .setText(R.id.tv_record_info, data.info)
//            .setText(R.id.tv_record_remark, data.item.remark.toString())
//        bindOperateType(holder.getView<TextView>(R.id.tv_record_type), data.item.type)
//        data.item.isSuccess.let {
//            holder.getView<TextView>(R.id.tv_record_remark)
//                .setTextColor(context.getColor(if (it!!) R.color.textColorGreen else R.color.colorRed))
//        }

        holder.setText(R.id.tv_protocol_content, data.value.toString())
        var tvTitle = holder.getView<TextView>(R.id.tv_protocol_title)
        if (data.desc == "设备状态" || data.desc == "实况数据") {
            tvTitle.textSize = 18f
            tvTitle.setTextColor(Color.parseColor("#FFFFFF"))
            holder.getView<LinearLayout>(R.id.ll_protocol)
                .setBackgroundResource(R.color.textColorGreen)
        } else {
            tvTitle.textSize = 16f
            tvTitle.setTextColor(Color.parseColor("#000000"))
            holder.getView<LinearLayout>(R.id.ll_protocol)
                .setBackgroundResource(R.color.colorPrimary)
        }
        tvTitle.text = data.desc
    }


}