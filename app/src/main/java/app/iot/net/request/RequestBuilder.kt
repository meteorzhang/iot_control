package app.iot.net.request

import okhttp3.RequestBody
import java.util.*

/**
 * Created by wanxin on 16/11/21.
 */

interface RequestBuilder {
    fun getRequestMap(): SortedMap<String, Any>
    fun getRequestBody(): RequestBody
}