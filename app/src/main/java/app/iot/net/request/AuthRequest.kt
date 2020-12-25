package app.iot.net.request

/**
 * Created by danbo on 2019-11-11.
 */
class AuthRequest(
    username: String?,
    password: String?
) :
    RequestBase() {

    init {
        params["username"] = username
        params["password"] = password
    }
}