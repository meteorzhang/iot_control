package app.iot.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import app.iot.protocol.BLEConstant
import app.iot.IOTApplication
import app.iot.common.util.LogUtils
import app.iot.protocol.ProtocolUtil
import app.iot.model.ProtocolDeviceType
import app.iot.protocol.BLEConstant.DATA_PREFIX
import app.iot.widget.dialog.SelectBLEDialog
import com.inuker.bluetooth.library.beacon.Beacon
import com.inuker.bluetooth.library.beacon.BeaconItem
import com.inuker.bluetooth.library.search.SearchResult
import com.inuker.bluetooth.library.utils.ByteUtils
import java.util.*

/**
 * Created by danbo on 2019-11-07.
 */
class SelectItemViewModel(
    val dialog: SelectBLEDialog,
    private val deviceResult: SearchResult
) :
    BaseViewModel() {

    val rssi = MutableLiveData<String>().apply {
        value = "${deviceResult.rssi}"
    }
    val deviceName = MutableLiveData<String>()
    val deviceMac = MutableLiveData<String>().apply {
        value = "(${deviceResult.address})"
    }
    val deviceNo = MutableLiveData<String>()

    //广播协议
    var beaconItem: BeaconItem? = null

    init {
        val beacon = Beacon(deviceResult.scanRecord)

        for (item in beacon.mItems) {

            if (item.len >= BLEConstant.BLE_MAX_LEN && ByteUtils.byteToString(item.bytes)
                    .contains(DATA_PREFIX)
            ) {

                val beaconString = ByteUtils.byteToString(item.bytes)
                if (!beaconString.toUpperCase(Locale.ROOT).contains(DATA_PREFIX)) {
                    continue
                }

                //过滤协议头DATA_PREFIX
                val splits = beaconString.toUpperCase(Locale.ROOT).split(DATA_PREFIX)
                val realBeaconString = splits[1]

                beaconItem = item

                beaconItem?.bytes = ByteUtils.stringToBytes(DATA_PREFIX + realBeaconString)

                val protocolDeviceType = ProtocolDeviceType.valueOf(
                    "${BLEConstant.PROTOCOL_PREFIX}${ProtocolUtil.getDeviceType(item.bytes)}"
                )
                deviceName.value = protocolDeviceType.title
                deviceNo.value = ProtocolUtil.getDeviceNo(item.bytes)

                LogUtils.i(
                    protocolDeviceType.title + ":" +
                            deviceResult.address +
                            "---优化协议数据>>>" + realBeaconString
                )
            }
        }
    }

    fun select(view: View) {
        if (dialog.listener == null) {
            return
        }
        IOTApplication.getApplication().btClient?.stopSearch()

        dialog.listener.onSelect(beaconItem)
        dialog.dismiss()
    }

}