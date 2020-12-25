package app.iot.ui

import app.iot.AppConstant
import app.iot.AppConstant.BUNDLE
import app.iot.AppConstant.SERIALIZABLE
import app.iot.BR
import app.iot.R
import app.iot.library.log.entity.OperateRecord
import app.iot.model.BindDetail
import app.iot.model.DeviceModel
import app.iot.viewmodel.UnbindViewModel
import app.iot.widget.toast.CommonToast
import com.google.gson.Gson

/**
 * 解绑部件
 */
class UnbindActivity : BaseActivity<UnbindViewModel>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_unbind
    }

    override fun initView() {
        mBinding.setVariable(BR.data, mViewModel?.titleViewModel)
        mBinding.setVariable(BR.recyclerViewViewModel, mViewModel?.recyclerViewViewModel)
        mViewModel?.titleViewModel?.context = mBinding.root.context

        val bundle = intent.getBundleExtra(BUNDLE)
        val bindDetail = bundle?.getSerializable(SERIALIZABLE) as BindDetail?

        if (bindDetail != null) {
            mViewModel?.init(bindDetail)
            return
        }

        //从intent中判断是否详情日志
        val or = intent.getSerializableExtra(AppConstant.OPERATE_RECORD)
        if (or != null) {
            val record = or as OperateRecord
            mViewModel?.init(record)
        }
    }

    override fun onBackPressed() {
        finish()
    }

}

