package app.iot.viewmodel

import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.ObservableField
import app.iot.*
import app.iot.library.log.db.DaoManager
import app.iot.widget.SimpleTextWatcher

class OperateRecordFragmentViewModel : BaseViewModel() {

    var isDelete = false

    val isEmpty = ObservableField<Boolean>().apply {
        set(true)
    }
    val emptyViewModel = EmptyViewModel("记录为空", null)

    var recyclerViewViewModel: RecyclerViewViewModel<OperateRecordItemViewModel> =
        RecyclerViewViewModel(
            null,
            R.layout.layout_operate_record_item,
            RecyclerViewViewModel.VERTICAL_LINEARLAYOUTMANAGER, 1
        )

    val searchText = ObservableField<String>()

    var searchWatcher: TextWatcher = object : SimpleTextWatcher() {
        override fun afterTextChanged(s: Editable?) {
            init()
        }
    }

    //已绑定设备信息
    fun init() {
        recyclerViewViewModel.item.clear()
        val records = DaoManager.instance()?.query(searchText.get(), isDelete)

        records?.let {
            for (record in it) {
                val item = OperateRecordItemViewModel(record)
                item.context = context
                recyclerViewViewModel.item.add(item)
            }
        }

        isEmpty.set(recyclerViewViewModel.item.isEmpty())
    }

}
