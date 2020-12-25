package app.iot.ui.fragment

import app.iot.BR
import app.iot.R
import app.iot.library.log.entity.OperateRecord
import app.iot.model.DeviceModel
import app.iot.model.DeviceType
import app.iot.model.EquipmentModel
import app.iot.model.EquipmentType
import app.iot.viewmodel.EquipBindViewModel

/**
 * 装备选择
 */
class EquipBindFragment(
    private val equipType: EquipmentType,
    private val equipModel: EquipmentModel,
    val deviceType: DeviceType,
    val deviceModel: DeviceModel,
    val record: OperateRecord?
) :
    BaseFragment<EquipBindViewModel>() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_equip_bind
    }

    override fun initView() {
        mBinding.setVariable(BR.recyclerViewViewModel, mViewModel?.recyclerViewViewModel)

        (mViewModel as EquipBindViewModel).let {
            it.init(equipType, equipModel, deviceType, deviceModel,record)
        }
    }

    override fun getBR(): Int {
        return BR.viewModel
    }
}