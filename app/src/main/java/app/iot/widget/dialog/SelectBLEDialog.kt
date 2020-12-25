package app.iot.widget.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import app.iot.BR
import app.iot.IOTApplication
import app.iot.R
import app.iot.viewmodel.SelectBLEViewModel
import com.inuker.bluetooth.library.beacon.BeaconItem
import kotlinx.android.synthetic.main.layout_select_ble_dialog.view.*


/**
 * Created by danbo on 2020/04/23.
 *
 * 选择操作对话框
 */
class SelectBLEDialog(
    context: Context,
    val title: String,
    val protocolPrefix: String?,//协议前缀过滤
    val listener: OnSelectListener<BeaconItem>?
) : Dialog(context, R.style.CommonDialog) {

    lateinit var mBinding: ViewDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Obtain binding
        mBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.layout_select_ble_dialog,
            null,
            false
        )

        //bind ViewModel
        val viewModel = SelectBLEViewModel(this)
        viewModel.context = context
        mBinding.setVariable(BR.viewModel, viewModel)
        mBinding.setVariable(BR.recyclerViewViewModel, viewModel.recyclerViewViewModel)
        mBinding.root.recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val topRowVerticalPosition =
                    if (recyclerView.childCount == 0) {
                        0
                    } else {
                        recyclerView.getChildAt(0).top
                    }
                mBinding.root.swipe_refresh_layout.isEnabled = topRowVerticalPosition >= 0
            }
        })
        setContentView(mBinding.root)
        mBinding.root.select_title.text = title

        setOnCancelListener {
            IOTApplication.getApplication().btClient?.stopSearch()
        }

        viewModel.getNetworkData()
    }

    interface OnSelectListener<T> {
        fun onSelect(t: T?) {}
    }
}
