package app.iot.entity

import android.annotation.SuppressLint
import android.os.Parcelable
import app.iot.library.log.entity.OperateRecord
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class RecordData(
    var account: String = "",
    var info: String = "",
    var item: OperateRecord,
) : Parcelable