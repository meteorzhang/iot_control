package app.iot.common


/**
 * Created by danbo on 2018/11/30.
 */
interface BaseView {

    //获取布局文件
    fun getLayoutId(): Int

    //页面初始化操作
    fun initView()

    //获取 BR 参数
    fun getBR(): Int

}