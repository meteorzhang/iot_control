package app.iot.net.request

/**
 * Created by danbo on 2019-11-11.
 */
class BindRequest(
    equipmentType: String?,
    equipmentModelId: Int?,
    equipmentNo: String?,
    description: String?,
    deviceType: String?,
    deviceModelId: Int?,
    deviceModelDetail: List<DeviceModelDetail>?
) :
    RequestBase() {

    init {
        params["equipmentType"] = equipmentType
        params["equipmentModelId"] = equipmentModelId
        params["equipmentNo"] = equipmentNo
        params["status"] = "bindOn"
        params["description"] = description
        params["deviceType"] = deviceType
        params["deviceModelId"] = deviceModelId
        params["deviceModelDetail"] = deviceModelDetail
    }
}

//上报绑定的设备型号详情
data class DeviceModelDetail(
    val id: Int?,
    val deviceNo: String?
)