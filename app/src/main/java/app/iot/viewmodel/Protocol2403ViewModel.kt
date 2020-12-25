package app.iot.viewmodel

import androidx.lifecycle.MutableLiveData
import app.iot.protocol.Protocol2401Parser
import app.iot.protocol.Protocol2403Parser
import app.iot.protocol.Protocol240Parser
import com.inuker.bluetooth.library.utils.ByteUtils

class Protocol2403ViewModel : BaseViewModel() {
    val protocolStr = MutableLiveData<String>()

    var protocol = MutableLiveData<Protocol2403Parser.Protocol2403>()

    fun init(protocol: ByteArray) {
        protocolStr.value = ByteUtils.byteToString(protocol)


        val protocol2403 = Protocol2403Parser.get2403Protocol(protocol)
        this.protocol.value = protocol2403
    }

}