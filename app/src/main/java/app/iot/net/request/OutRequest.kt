package app.iot.net.request

/**
 * Created by danbo on 2019-11-11.
 */
class OutRequest(
    deviceBindId: Int?,
) :
    RequestBase() {

    init {
        params["deviceBindId"] = deviceBindId
    }
}