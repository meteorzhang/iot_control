package app.iot.net

import android.os.Build
import app.iot.common.util.AppInfoUtils
import app.iot.common.util.Utils
import app.iot.common.util.fitTLS.HttpsUtils
import app.iot.common.util.fitTLS.Tls12SocketFactory
import app.iot.net.RetrofitProvider.SingletonHolder.auth
import app.iot.net.RetrofitProvider.SingletonHolder.client
import app.iot.net.factory.ToStringConverterFactory
import app.iot.token
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.File
import java.io.IOException
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import kotlin.jvm.Throws


/**
 * Created by danbo on 2017/11/30.
 */
class StringRetrofitProvider {
    private var service: ApiService

    private constructor() {
        var sslContext: SSLContext? = null
        try {
            sslContext = SSLContext.getInstance("TLS")
            try {
                sslContext!!.init(null, null, null)
            } catch (e: KeyManagementException) {
                e.printStackTrace()
            }

        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        val socketFactory = Tls12SocketFactory(sslContext!!.socketFactory)
        //   日志拦截器
        val cacheFile = File(Utils.getContext()?.cacheDir, "cache")
        val cache = Cache(cacheFile, 1024 * 1024 * 100) //100Mb

        val okHttpClientBuilder = OkHttpClient.Builder().cache(cache)
            .readTimeout(ApiService.DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
            .writeTimeout(ApiService.DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
            .connectTimeout(ApiService.DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
            .addInterceptor(CommonHeadInterceptor())
            .retryOnConnectionFailure(true)

        //忽略ssl证书,android10及以上的版本就不用了
        if (Build.VERSION.SDK_INT < 29) {
            okHttpClientBuilder.sslSocketFactory(socketFactory, HttpsUtils.UnSafeTrustManager())
        }

        okHttpClientBuilder.hostnameVerifier(HostnameVerifier { _, _ -> true })

        val okHttpClient = okHttpClientBuilder.build()

        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(ToStringConverterFactory())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(ApiService.BASE_URL)
            .build()
        service = retrofit.create(ApiService::class.java)
    }

    //  创建单例
    object SingletonHolder {
        val INSTANCE = StringRetrofitProvider()
        const val client = "android"
        const val auth = "Authorization"
    }

    companion object {
        fun getApiService(): ApiService {

            return SingletonHolder.INSTANCE.service
        }
    }

    internal class CommonHeadInterceptor : Interceptor {

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {

            var request = chain.request()

            val reqId = System.currentTimeMillis()
            val deviceId = AppInfoUtils.getDeviceId()
            //val signature = getSignature(request, reqId, deviceId)
            val authToken = if (token.isNullOrEmpty()) {
                ""
            } else {
                "Bearer $token"
            }
            request = request.newBuilder().apply {
                addHeader("deviceId", deviceId)
                addHeader("platform", client)
                addHeader("rid", "" + reqId)
                //addHeader("sign", signature)
                addHeader(auth, authToken)
                addHeader("version", "" + AppInfoUtils.getVersionCode()!!)
                addHeader("model", Build.BRAND + " " + Build.MODEL)
                addHeader("sdkVersion", "" + Build.VERSION.SDK_INT)
            }.build()
            return chain.proceed(request)
        }
    }
}