package app.iot.widget.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import app.iot.R
import app.iot.BR
import app.iot.model.BindDetail
import app.iot.ui.BaseActivity
import app.iot.viewmodel.SearchViewModel

/**
 * Created by danbo on 2020/04/23.
 *
 * 搜索装备/设备对话框
 */
class SearchDialog(
    val activity: BaseActivity<*>, val title:String, val listener: SearchListener
) : Dialog(activity, R.style.CommonDialog) {

    lateinit var mBinding: ViewDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Obtain binding
        mBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.layout_search_dialog,
            null,
            false
        )

        //bind ViewModel
        val viewModel = SearchViewModel(activity,this)
        viewModel.context = activity
        mBinding.setVariable(BR.viewModel, viewModel)

        setContentView(mBinding.root)

    }

    interface SearchListener {
        fun onSearched(bindDetail: BindDetail) {}
    }
}
