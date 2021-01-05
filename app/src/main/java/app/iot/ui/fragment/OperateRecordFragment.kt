package app.iot.ui.fragment

import SPUtils
import android.app.Dialog
import android.view.View
import android.widget.TextView
import app.iot.AppConstant
import app.iot.BR
import app.iot.R
import app.iot.adapter.OperationRecordAdapter
import app.iot.common.util.ImeUtil
import app.iot.entity.RecordData
import app.iot.ext.initVertical
import app.iot.library.log.db.DaoManager
import app.iot.library.log.entity.OperateType
import app.iot.ui.BindActivity
import app.iot.ui.DeviceDetailActivity
import app.iot.ui.SwitchActivity
import app.iot.ui.UnbindActivity
import app.iot.viewmodel.OperateRecordFragmentViewModel
import app.iot.widget.dialog.DoubleButtonDialog
import kotlinx.android.synthetic.main.fragment_operate_record.*
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

    private fun init() {
        var mAdapter = OperationRecordAdapter()
        recyclerView.initVertical(bindAdapter = mAdapter)
        var noDataView = layoutInflater.inflate(R.layout.layout_no_content, null)
        noDataView.findViewById<TextView>(R.id.empty_view).text = "暂无记录"
        mAdapter.setEmptyView(noDataView)
        val records = DaoManager.instance()?.query(search_text.text.toString(), isDelete)

        records?.let {
            var mList = ArrayList<RecordData>()
            for (record in it) {
                var info: String = ""
                when (record.type) {
                    OperateType.BIND.value -> {//绑定只有装备编号
                        record.equipNo?.let { it ->
                            info = ("装备编号:${it}")
                        }
                    }
                    OperateType.UNBIND.value -> {//解绑只装备编号
                        record.equipNo?.let { it ->
                            info = ("装备编号:${it}")
                        }
                    }
                    OperateType.SWITCH.value -> {//更换有装备编号和设备编号
                        var text: String? = ""
                        record.equipNo?.let {
                            text = "装备编号:$it \n"
                        }

                        record.deviceNo?.let {
                            text = "${text}设备编号:$it"
                        }
                        info = (text.toString())
                    }
                    OperateType.CHECK.value -> {
                        record.deviceNo?.let { it ->
                            info = ("设备编号:${it}")
                        }
                    }
                }
                mList.add(
                    RecordData(
                        account = (SPUtils.getData(AppConstant.ACCOUNT, "") as String + " "),
                        info = info, item = record
                    )
                )
            }
            mAdapter.setList(mList)
        }
        ProxyClick(mAdapter, noDataView)
    }

    private fun ProxyClick(mAdapter: OperationRecordAdapter, noDataView: View) {
        mAdapter.run {
            setOnItemClickListener { _, _, position ->
                when (this.data[position].item.type) {
                    OperateType.BIND.value -> {
                        startActivity(
                            BindActivity::class.java,
                            AppConstant.OPERATE_RECORD,
                            this.data[position].item
                        )
                    }
                    OperateType.UNBIND.value -> {
                        startActivity(
                            UnbindActivity::class.java,
                            AppConstant.OPERATE_RECORD,
                            this.data[position].item
                        )
                    }
                    OperateType.SWITCH.value -> {
                        startActivity(
                            SwitchActivity::class.java,
                            AppConstant.OPERATE_RECORD,
                            this.data[position].item
                        )
                    }
                    OperateType.CHECK.value -> {
                        startActivity(
                            DeviceDetailActivity::class.java,
                            AppConstant.OPERATE_RECORD, this.data[position].item
                        )
                    }
                }
            }
            setOnItemLongClickListener { _, _, position ->
                context?.let {
                    DoubleButtonDialog(
                        it,
                        null,
                        "提示",
                        "确定删除此记录吗?",
                        "取消",
                        "确认",
                        object : DoubleButtonDialog.DialogClickListener {
                            override fun onClick(dialog: Dialog) {
                                super.onClick(dialog)
                                DaoManager.instance().delete(mAdapter.data[position].item)
                                initView()
                            }
                        }
                    ).show()
                }
                false
            }
        }
        noDataView.setOnClickListener { initView() }
    }

    override fun getBR(): Int {
        return BR.viewModel
    }

}