package app.iot.viewmodel

import androidx.databinding.ObservableField
import app.iot.*
import app.iot.library.log.entity.OperateRecord
import app.iot.model.BindDetail
import com.google.gson.Gson

class SwitchViewModel : BaseViewModel(), OnTitleClickListener {
    val isLog = ObservableField<Boolean>().apply {
        set(false)
    }
    val logRemark = ObservableField<String>()

    val titleViewModel = TopBarViewModel("设备更换部件", R.drawable.ic_nav_back, "", this)

    val bindDetail = ObservableField<BindDetail>()

    val isEmpty = ObservableField<Boolean>().apply {
        set(true)
    }
    val emptyViewModel = EmptyViewModel("无设备列表", null)

    var recyclerViewViewModel: RecyclerViewViewModel<DeviceBindItemViewModel> =
        RecyclerViewViewModel(
            null,
            R.layout.layout_device_bind_item,
            RecyclerViewViewModel.VERTICAL_LINEARLAYOUTMANAGER, 1
        )

    fun init(record: OperateRecord) {
        this.isLog.set(true)
        logRemark.set(record.remark)

        val bd = Gson().fromJson(record.content, BindDetail::class.java)
        init(bd,record)
    }

    //已绑定设备信息
    fun init(bindDetail: BindDetail,record: OperateRecord?) {
        this.bindDetail.set(bindDetail)

        if (bindDetail.deviceBindDetailList == null) {
            return
        }
        for (bindItem in bindDetail.deviceBindDetailList) {
            val itemViewModel =
                DeviceBindItemViewModel(bindDetail, bindItem, record)
            itemViewModel.context = context
            recyclerViewViewModel.item.add(itemViewModel)
        }

        isEmpty.set(recyclerViewViewModel.item.isEmpty())
    }

    override fun onLogoClick() {
        super.onLogoClick()
        finish()
    }

}
