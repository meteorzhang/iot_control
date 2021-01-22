package app.iot.net

import app.iot.model.*
import app.iot.net.response.BasicResponse
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * Created by danbo on 2018/11/29.
 */
interface
ApiService {
    companion object {
        val BASE_URL = app.iot.AppConfig.BASE_URL!!
        const val DEFAULT_TIMEOUT = 10000L
    }

    @POST("sso/syncToken")
    fun refreshToken(): Observable<BasicResponse<AuthModel>>//仅返回token

    @POST("auth.html")
    fun auth(@Body body: RequestBody?): Observable<BasicResponse<AuthModel>>

    @POST("logout.html")
    fun logout(): Observable<BasicResponse<Any>>

    //数据文件最新版本
    @GET("dataFile/version.html")
    fun latestVersion(): Observable<BasicResponse<DataVersion>>

    //数据文件更新接口
    @POST("client/file/download/{version}.html")
    fun latestData(@Path("version") version: String?): Observable<String>

    @POST("device/bind.html")
    fun bind(@Body body: RequestBody?): Observable<BasicResponse<Any>>

    @GET("device/bind/detail.html")
    fun bindDetail(
        @Query("equipmentNo") equipmentNo: String?,
        @Query("deviceNo") deviceNo: String?
    ): Observable<BasicResponse<BindDetail>>

    @POST("device/exchange.html")
    fun exchange(@Body body: RequestBody?): Observable<BasicResponse<Any>>

    @POST("device/bind/out.html")
    fun out(@Body body: RequestBody?): Observable<BasicResponse<Any>>

}