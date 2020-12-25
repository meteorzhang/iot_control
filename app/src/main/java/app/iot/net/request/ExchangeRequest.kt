package app.iot.net.request

/**
 * Created by danbo on 2019-11-11.
 */
class ExchangeRequest(
    newDeviceNo: String?,
    deviceBindDetailId: Int?,
) :
    RequestBase() {

    init {
        params["newDeviceNo"] = newDeviceNo
        params["deviceBindDetailId"] = deviceBindDetailId
    }
}