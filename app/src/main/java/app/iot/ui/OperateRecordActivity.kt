package app.iot.ui

import androidx.viewpager.widget.ViewPager
import app.iot.BR
import app.iot.R
import app.iot.adapter.HomeNavigatorAdapter
import app.iot.adapter.ViewPagerAdapter
import app.iot.common.util.ImeUtil
import app.iot.library.log.db.DaoManager
import app.iot.ui.fragment.BaseFragment
import app.iot.ui.fragment.OperateRecordFragment
import app.iot.viewmodel.OperateRecordViewModel
import app.iot.widget.tab.ViewPagerHelper
import app.iot.widget.tab.buildins.commonnavigator.CommonNavigator
import app.iot.widget.tab.buildins.commonnavigator.titles.CommonPagerTitleView
import kotlinx.android.synthetic.main.activity_operate_record.*
import kotlinx.android.synthetic.main.activity_operate_record.view.*

/**
 * 日志记录
 */
class OperateRecordActivity : BaseActivity<OperateRecordViewModel>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_operate_record
    }

    private val titles: ArrayList<String> = arrayListOf()
    val fragments = arrayListOf<BaseFragment<*>>()

    override fun initView() {
        mBinding.setVariable(BR.data, mViewModel?.titleViewModel)
        mViewModel?.titleViewModel?.context = mBinding.root.context

        //删除超过7天的历史记录
        val allDeleted = DaoManager.instance().query(null, true)
        for (delete in allDeleted) {
            if ((System.currentTimeMillis() - delete.deleteTime) > 7 * 24 * 60 * 60 * 1000) {//超过七天
                DaoManager.instance().delete(delete)
            }
        }

        //添加推荐
        titles.add("操作记录")
        titles.add("历史记录")
        fragments.add(OperateRecordFragment(false))
        fragments.add(OperateRecordFragment(true))

        //adapter
        mBinding.root.view_pager.let {
            it.adapter = ViewPagerAdapter(supportFragmentManager, fragments)

            it.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {
                }

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                }

                override fun onPageSelected(position: Int) {
                    mViewModel?.titleViewModel?.menu?.value = if (position == 0) {
                        "清空"
                    } else {
                        ""
                    }
                }

            })

            //navigator
            val navigator = CommonNavigator(this)
            navigator.isAdjustMode = true
            navigator.adapter = HomeNavigatorAdapter(it, titles)

            mBinding.root.indicator.navigator = navigator

            //bind
            ViewPagerHelper.bind(mBinding.root.indicator, it)
        }
    }

    override fun onBackPressed() {
        finish()
    }

}

