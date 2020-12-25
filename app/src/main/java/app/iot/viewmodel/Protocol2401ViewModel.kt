package app.iot.viewmodel

import androidx.lifecycle.MutableLiveData
import app.iot.protocol.Protocol2401Parser
import com.inuker.bluetooth.library.utils.ByteUtils

class Protocol2401ViewModel : BaseViewModel() {
    val protocolStr = MutableLiveData<String>()

    var protocol = MutableLiveData<Protocol2401Parser.Protocol2401>()

    fun init(protocol: ByteArray) {
        protocolStr.value = ByteUtils.byteToString(protocol)

        val protocol2401 = Protocol2401Parser.get2401Protocol(protocol)
        this.protocol.value = protocol2401
    }

}