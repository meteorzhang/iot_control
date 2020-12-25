package app.iot.viewmodel

import android.content.Context
import androidx.databinding.ObservableField
import app.iot.R
import app.iot.model.Protocol
import app.iot.model.ProtocolDetail
import app.iot.ui.fragment.HostFragment

class ProtocolDetailItemViewModel(private val detail: ProtocolDetail) : BaseViewModel() {

    val name = ObservableField<String>().apply {
        set(detail.desc + ":" + detail.status)
    }

}