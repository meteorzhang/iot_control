package app.iot.adapter

import android.widget.TextView
import app.iot.R
import app.iot.common.bindOperateType
import app.iot.entity.RecordData
import app.iot.viewmodel.OperateRecordItemViewModel
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder


class OperationRecordAdapter :
    BaseQuickAdapter<RecordData, BaseViewHolder>(R.layout.layout_operate_record_item) {

    override fun convert(holder: BaseViewHolder, data: RecordData) {
        holder.setText(R.id.tv_record_time, "${data.item.time}")
            .setText(R.id.tv_record_account, data.account)
            .setText(R.id.tv_record_info, data.info)
            .setText(R.id.tv_record_remark, data.item.remark.toString())
        bindOperateType(holder.getView<TextView>(R.id.tv_record_type), data.item.type)
        data.item.isSuccess.let {
            holder.getView<TextView>(R.id.tv_record_remark)
                .setTextColor(context.getColor(if (it!!) R.color.textColorGreen else R.color.colorRed))
        }

    }


}