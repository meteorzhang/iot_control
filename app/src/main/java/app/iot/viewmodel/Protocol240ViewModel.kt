package app.iot.viewmodel

import androidx.lifecycle.MutableLiveData
import app.iot.common.util.LogUtils
import app.iot.protocol.Protocol240Parser
import com.inuker.bluetooth.library.utils.ByteUtils

class Protocol240ViewModel : BaseViewModel() {
    val protocolStr = MutableLiveData<String>()

    var protocol = MutableLiveData<Protocol240Parser.Protocol240>()

    fun init(protocol: ByteArray) {


        protocolStr.value = ByteUtils.byteToString(protocol)

        val protocol240 = Protocol240Parser.get240Protocol(protocol)
        this.protocol.value = protocol240

    }

}