package app.iot.net.response

/**
 * Created by danbo on 2017/11/30.
 */
class BasicResponse<T> {
    // code 为返回的状态码, message 为返回的消息
    var statusCode = 200
    var message = ""
    //用来模仿Data
    var data: T? = null
}