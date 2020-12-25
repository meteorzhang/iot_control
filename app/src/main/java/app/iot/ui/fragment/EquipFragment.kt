package app.iot.ui.fragment

import app.iot.BR
import app.iot.R
import app.iot.viewmodel.EquipViewModel

/**
 * 装备选择
 */
class EquipFragment : BaseFragment<EquipViewModel>() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_equip
    }

    override fun initView() {
        (mViewModel as EquipViewModel).let {
            it.getNetworkData()
        }
    }

    override fun getBR(): Int {
        return BR.viewModel
    }
}