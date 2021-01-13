package app.iot.viewmodel

import android.view.View
import androidx.databinding.ObservableField
import app.iot.protocol.BLEConstant
import app.iot.IOTApplication
import app.iot.R
import app.iot.common.DefaultPreferences
import app.iot.common.util.LogUtils
import app.iot.protocol.ProtocolUtil
import app.iot.widget.dialog.SelectBLEDialog
import com.inuker.bluetooth.library.beacon.Beacon
import com.inuker.bluetooth.library.search.SearchRequest
import com.inuker.bluetooth.library.search.SearchResult
import com.inuker.bluetooth.library.search.response.SearchResponse
import com.inuker.bluetooth.library.utils.ByteUtils
import java.util.*

class SelectBLEViewModel(private val dialog: SelectBLEDialog) : BaseViewModel(),
    LoadingViewModel.OnRetryListener,
    EmptyViewModel.OnClickListener {

    val loadingViewModel = LoadingViewModel(this)

    var recyclerViewViewModel: RecyclerViewViewModel<SelectItemViewModel> = RecyclerViewViewModel(
        null,
        R.layout.layout_select_item,
        RecyclerViewViewModel.VERTICAL_LINEARLAYOUTMANAGER, 1
    )
    val isEmpty = ObservableField<Boolean>()
    val emptyViewModel = EmptyViewModel("点击刷新", this)

    val isLoading = ObservableField<Boolean>()

    override fun getNetworkData() {
        super.getNetworkData()
        isEmpty.set(false)
        loadingViewModel.apply {
            if (isLoading.get()!!) {//加载中
                return
            }
            isLoading.set(true)
            loadingInfo.set("加载中...")
        }

        recyclerViewViewModel.item.clear()
        deviceAddressList.clear()
        deviceList.clear()

        loadData()
    }

    private fun loadData() {
        IOTApplication.getApplication().btClient?.stopSearch()

        val request = SearchRequest.Builder()
            .searchBluetoothLeDevice(
                DefaultPreferences.scanDuration(context),
                DefaultPreferences.scanTimes(context)
            ) // 先扫BLE设备2次，每次15s
            .build()

        IOTApplication.getApplication().btClient?.search(
            request,
            object : SearchResponse {
                override fun onSearchStarted() {
                    isLoading.set(true)
                }

                override fun onDeviceFounded(device: SearchResult) {
//                val beacon = Beacon(device.scanRecord)
//                LogUtils.d(
//                    String.format(
//                        "beacon for %s\n%s",
//                        device.address,
//                        beacon.toString()
//                    )
//                )
                    initData(device)
                }

                override fun onSearchStopped() {
                    LogUtils.d("蓝牙搜索停止")
                    refreshLayout?.isRefreshing = false
                    loadingViewModel.apply {
                        if (isLoading.get()!!) {
                            isLoading.set(false)
                            loadingInfo.set("")
                        }
                    }
                    isEmpty.set(recyclerViewViewModel.items.isEmpty())
                    isLoading.set(false)
                }

                override fun onSearchCanceled() {
                    isLoading.set(false)
                }
            })

    }

    private var deviceAddressList = arrayListOf<String>()
    private var deviceList = arrayListOf<SearchResult>()
    fun initData(device: SearchResult) {
        if (deviceAddressList.contains(device.address)) {
            return
        }

        //过滤蓝牙设备
        //协议规定蓝牙广播信息不超过22字节

        val beacon = Beacon(device.scanRecord)
        //TODO 协议待处理
        var isIot = false
        for (item in beacon.mItems) {//广播item长度待定义
            if (item.len >= BLEConstant.BLE_MAX_LEN && ByteUtils.byteToString(item.bytes)
                    .contains(BLEConstant.DATA_PREFIX)
            ) {
                isIot = true
            }
        }
        if (!isIot) {
            return
        }
        loadingViewModel.apply {
            if (isLoading.get()!!) {
                isLoading.set(false)
                loadingInfo.set("")
            }
        }

        //前缀过滤(筛选指定装备/设备)
        if (dialog.protocolPrefix != null) {
            for (item in beacon.mItems) {
                if (item.len >= BLEConstant.BLE_MAX_LEN && ByteUtils.byteToString(item.bytes)
                        .contains(BLEConstant.DATA_PREFIX)
                ) {

                    //过滤协议头DATA_PREFIX
                    val splits = ByteUtils.byteToString(item.bytes).toUpperCase(Locale.ROOT)
                        .split(BLEConstant.DATA_PREFIX)
                    val realBeaconString = splits[1]

                    val bytes = ByteUtils.stringToBytes(BLEConstant.DATA_PREFIX + realBeaconString)
                    val deviceType = "0x${ProtocolUtil.getDeviceType(bytes)}"

                    //println(dialog.protocolPrefix + "--过滤 --" + deviceType)
                    if (deviceType != dialog.protocolPrefix) {
                        return
                    }
                }
            }
        }
        deviceAddressList.add(device.address)
        deviceList.add(device)
        deviceList.sortByDescending { it.rssi }

        //clear后排序
        recyclerViewViewModel.item.clear()
        //加载
        for (deviceItem in deviceList) {
            val itemViewModel = SelectItemViewModel(dialog, deviceItem)
            itemViewModel.context = context

            recyclerViewViewModel.item.add(itemViewModel)
        }

        isEmpty.set(recyclerViewViewModel.items.isEmpty())
    }

    override fun onRefresh() {
        loadData()
    }

    override fun retry() {
        getNetworkData()
    }

    override fun click() {
        getNetworkData()
    }

    fun reload(view: View) {
        getNetworkData()
    }
}