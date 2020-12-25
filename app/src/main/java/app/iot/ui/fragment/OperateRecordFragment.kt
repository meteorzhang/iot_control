package app.iot.ui.fragment

import app.iot.BR
import app.iot.R
import app.iot.common.util.ImeUtil
import app.iot.viewmodel.OperateRecordFragmentViewModel
import kotlinx.android.synthetic.main.fragment_operate_record.view.*

class OperateRecordFragment(private val isDelete: Boolean) :
    BaseFragment<OperateRecordFragmentViewModel>() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_operate_record
    }

    override fun initView() {
        mBinding.setVariable(BR.recyclerViewViewModel, mViewModel?.recyclerViewViewModel)
        mViewModel?.isDelete = isDelete
        mViewModel?.init()

        mBinding.root.search_text.setOnEditorActionListener { tv, _, _ ->
            ImeUtil.hideSoftKeyboard(tv)
            mViewModel?.init()
            true
        }
    }

    override fun getBR(): Int {
        return BR.viewModel
    }

}