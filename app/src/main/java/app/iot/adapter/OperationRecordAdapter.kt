package app.iot.adapter

import android.graphics.Color
import android.text.TextUtils
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.RecyclerView
import app.iot.R
import app.iot.common.bindOperateType
import app.iot.common.util.LogUtils
import app.iot.entity.RecordData
import app.iot.ext.initVertical
import app.iot.model.Protocol
import app.iot.model.ProtocolDetail
import app.iot.viewmodel.OperateRecordItemViewModel
import app.iot.viewmodel.ProtocolDetailItemViewModel
import app.iot.viewmodel.RecyclerViewViewModel
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder


class OperationRecordAdapter :
    BaseQuickAdapter<Protocol, BaseViewHolder>(R.layout.layout_protocol_item) {
    val details = ObservableField<List<ProtocolDetail>>()

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

        holder.setText(
            R.id.tv_protocol_content,
            if (TextUtils.isEmpty(data.value.toString()) || data.value.toString() == "null") "" else data.value.toString()
        )
        var tvTitle = holder.getView<TextView>(R.id.tv_protocol_title)
        if (data.desc == "设备状态" || data.desc == "实况数据") {
            tvTitle.textSize = 18f
            tvTitle.maxEms = 100
            holder.getView<View>(R.id.center_line).visibility = View.GONE
            tvTitle.setTextColor(Color.parseColor("#FFFFFF"))
            holder.getView<LinearLayout>(R.id.ll_protocol)
                .setBackgroundResource(R.color.textColorGreen)
        } else {
            tvTitle.textSize = 16f
            tvTitle.maxEms = 7
            holder.getView<View>(R.id.center_line).visibility = View.VISIBLE
            tvTitle.setTextColor(Color.parseColor("#000000"))
            holder.getView<LinearLayout>(R.id.ll_protocol)
                .setBackgroundResource(R.color.colorPrimary)
        }
        tvTitle.text = data.desc


//
        data.detail?.let {
            val detailList: MutableList<ProtocolDetail> = arrayListOf()
            for (detail in it) {
                if (detail.dataType == "UsedBit") {
                    // 设备状态 --UsedBit
                    detailList.add(detail)
                }
                LogUtils.e("协议--->" + detail.toString())
            }
            var detailsAdapter = ProtocolDetailsAdapter()
            detailsAdapter.setList(detailList)
            holder.getView<RecyclerView>(R.id.recycler_details_view)
                .initVertical(bindAdapter = detailsAdapter)
        }


    }


}