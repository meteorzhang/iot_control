package app.iot.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import app.iot.common.BaseView
import app.iot.common.util.VMUtil
import app.iot.viewmodel.BaseViewModel

abstract class BaseFragment<VM : BaseViewModel> : Fragment(), BaseView {

    open lateinit var mBinding: ViewDataBinding
    private var rootView: View? = null// 缓存Fragment view
    open var mViewModel: VM? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Obtain ViewModel from ViewModelProviders
        val vm = VMUtil.getVM<VM>(this)
        vm?.let {
            mViewModel = ViewModelProviders.of(this).get(vm::class.java)
        }

        //bind context
        mViewModel!!.context = activity
        if (rootView == null) {
            // Obtain binding
            mBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
            rootView = mBinding.root

            mBinding.lifecycleOwner = this
            // mBinding.executePendingBindings()
            mBinding.setVariable(getBR(), mViewModel)

            // 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
            val parent = rootView?.parent
            if (parent != null) {
                (parent as ViewGroup).removeView(rootView)
            }
        }
        initView()

        return mBinding.root
    }

    open fun refreshData(message:String?) {

    }

    protected var isVisibleToUser = false
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        this.isVisibleToUser = isVisibleToUser
    }

    fun isInitialized(): Boolean = ::mBinding.isInitialized
}