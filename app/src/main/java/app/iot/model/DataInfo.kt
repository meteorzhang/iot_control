package app.iot.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by danbo on 2020/10/22.
 */
//数据文件版本
data class DataVersion(
    val version: String
) : Serializable

data class DataInfo(
    val dictionaryData: DictionaryData?,
    val bluetoothProtocolData: BluetoothProtocolData?
) : Serializable

data class DictionaryData(//字典数据
    val deviceType: List<DeviceType>,
    val equipmentType: List<EquipmentType>,
    val bindStatus: List<BindStatus>,
    val componentCode: List<ComponentCode>
) : Serializable

data class DeviceType(//设备类型
    val codeKeyName: String,//设备类型名称
    val codeKey: String,//设备类型键值
    val deviceModel: List<DeviceModel>?//设备型号
) : Serializable

data class DeviceModel(
    val id: Int,//设备型号ID
    val modelName: String,//设备型号名称
    val modelCode: String,//设备型号编码
    val modelDetail: List<ModelDetail>? //设备型号组件明细
) : Serializable

data class ModelDetail(
    val id: Int,//设备型号组件明细ID
    val defaultName: String,//设备型号组件默认名称
    val componentCode: String//设备代码
) : Serializable

data class EquipmentType(//装备类型
    val codeKeyName: String,//装备类型名称
    val codeKey: String,//装备类型键值
    val equipmentModel: List<EquipmentModel>?//装备型号
) : Serializable

data class EquipmentModel(//装备型号
    val id: Int,
    val modelName: String,
    val modelCode: String,
    val modelDetail: ModelDetail? //装备明细
) : Serializable

data class BindStatus(//绑定状态
    val codeKeyName: String,// 启用 | 禁用
    val codeKey: String// bindOn | bindOff
) : Serializable

data class ComponentCode(//绑定状态
    val name: String,//名称
    val code: String//编码
) : Serializable

/**********************蓝牙协议***************************/
data class BluetoothProtocolData(//蓝牙协议
    @SerializedName("240")//冷藏箱监控设备主机协议
    val P240: List<Protocol>,
    @SerializedName("242")
    val P242: List<Protocol>,
    @SerializedName("243")
    val P243: List<Protocol>,
    @SerializedName("244")
    val P244: List<Protocol>,

    @SerializedName("2401")//冷藏箱蓝牙基站协议
    val P2401: List<Protocol>,
    @SerializedName("2402") //冷箱手持终端协议
    val P2402: List<Protocol>,
    @SerializedName("2403")//冷箱门禁终端协议
    val P2403: List<Protocol>,
    @SerializedName("2404")
    val P2404: List<Protocol>
) : Serializable

data class Protocol(
    val number: Int,
    val dataType: String,//Reserved Header Data DeviceType DeviceNo UTCTime DeviceStatus CRC-8 CRC-16 DoorStatus ErrorStatus ReservedBit UsedBit
    val size: Int,
    val sizeUnit: String,
    val valueUnit: String,
    val valueType: String,
    val coefficient:Double,
    val desc: String?,
    val detail: List<ProtocolDetail>?,
    var value: Any?//设备值
) : Serializable

data class ProtocolDetail(
    val number: Int,
    val size: Int?,
    val sizeUnit: String,
    val desc: String,
    val unit: String?,
    val dataType: String,
    var status: Int?//设备状态描述(0:故障 1:正常)
) : Serializable
