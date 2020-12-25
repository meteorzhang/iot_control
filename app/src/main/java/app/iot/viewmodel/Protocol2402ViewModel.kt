package app.iot.viewmodel

import androidx.lifecycle.MutableLiveData
import app.iot.protocol.Protocol2402Parser
import com.inuker.bluetooth.library.utils.ByteUtils

class Protocol2402ViewModel : BaseViewModel() {
    val protocolStr = MutableLiveData<String>()

    var protocol = MutableLiveData<Protocol2402Parser.Protocol2402>()

    fun init(protocol: ByteArray) {
        protocolStr.value = ByteUtils.byteToString(protocol)

        val protocol2402 = Protocol2402Parser.get2402Protocol(protocol)
        this.protocol.value = protocol2402
    }

}