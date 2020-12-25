package app.iot.ui

import app.iot.AppConstant
import app.iot.BR
import app.iot.R
import app.iot.library.log.entity.OperateRecord
import app.iot.model.DeviceModel
import app.iot.model.DeviceType
import app.iot.model.EquipmentModel
import app.iot.model.EquipmentType
import app.iot.ui.fragment.EquipBindFragment
import app.iot.ui.fragment.EquipFragment
import app.iot.viewmodel.BindViewModel
import com.google.gson.Gson


class BindActivity : BaseActivity<BindViewModel>() {

    companion object {
        var equipTypeIndex = 0
        var equipModelIndex = 0
        var deviceTypeIndex = 0
        var deviceModelIndex = 0
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_bind
    }


    override fun initView() {
        mBinding.setVariable(BR.data, mViewModel?.titleViewModel)
        mViewModel?.titleViewModel?.context = mBinding.root.context

        //初始化
        equipTypeIndex = 0
        equipModelIndex = 0
        deviceTypeIndex = 0
        deviceModelIndex = 0

        //从intent中判断是否详情日志
        val or = intent.getSerializableExtra(AppConstant.OPERATE_RECORD)
        if (or != null) {
            val record = or as OperateRecord
            startFragment(
                EquipBindFragment(
                    Gson().fromJson(record.equipType, EquipmentType::class.java),
                    Gson().fromJson(record.equipModel, EquipmentModel::class.java),
                    Gson().fromJson(record.deviceType, DeviceType::class.java),
                    Gson().fromJson(record.deviceModel, DeviceModel::class.java),
                    record
                ), R.id.container
            )

            mViewModel?.titleViewModel?.title?.value = "设备安装绑定"
        } else {
            startFragment(EquipFragment(), R.id.container)
        }

    }

    override fun onBackPressed() {
        finish()
    }

}

