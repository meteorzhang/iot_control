package app.iot.model

import java.io.Serializable

data class BindDetail(
    val id: Int,
    val equipmentTypeName: String?,
    val equipmentNo: String?,
    val equipmentModelCode: String?,
    val deviceTypeName: String?,
    val deviceModelCode: String?,
    val status: String?,
    val description: String?,
    val deviceBindDetailList: List<DeviceBindDetail>?
) : Serializable

data class DeviceBindDetail(
    val id: Int,
    val deviceName: String?,
    val deviceNo: String?,
    val description: String?,
    val componentCode: String?
) : Serializable