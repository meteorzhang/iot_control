package app.iot.ui

import app.iot.AppConstant
import app.iot.AppConstant.BUNDLE
import app.iot.AppConstant.BYTES
import app.iot.BR
import app.iot.R
import app.iot.library.log.db.DaoManager
import app.iot.library.log.entity.OperateRecord
import app.iot.library.log.entity.OperateType
import app.iot.protocol.ProtocolUtil
import app.iot.viewmodel.DeviceDetailViewModel
import com.inuker.bluetooth.library.utils.ByteUtils

/**
 * 设备检查详情
 */
class DeviceDetailActivity : BaseActivity<DeviceDetailViewModel>() {

    override fun getLayoutId(): Int {
        return R.layout.activity_device_detail
    }

    override fun initView() {
        mBinding.setVariable(BR.data, mViewModel?.titleViewModel)
        mViewModel?.titleViewModel?.context = mBinding.root.context

        val bundle = intent.getBundleExtra(BUNDLE)
        val bytes = bundle?.getByteArray(BYTES)//蓝牙协议
        if (bytes != null && bytes.isNotEmpty()) {
            //检测日志记录
            //记录日志
            val checkRecord = OperateRecord(
                null,
                SPUtils.getData(AppConstant.ACCOUNT, "") as String,
                OperateType.CHECK.value,
                true,
                ByteUtils.byteToString(bytes),
                ProtocolUtil.getDeviceNo(bytes),
                System.currentTimeMillis(),
                "设备检测成功!"
            )
            DaoManager.instance().insertOrReplace(checkRecord)

            mViewModel?.init(bytes)
            return
        }

        //从intent中判断是否详情日志
        val or = intent.getSerializableExtra(AppConstant.OPERATE_RECORD)
        if (or != null) {
            val record = or as OperateRecord
            mViewModel?.init(ByteUtils.stringToBytes(record.content))
        }
    }

    override fun onBackPressed() {
        finish()
    }

}

