package app.iot.net.factory

import app.iot.common.util.LogUtils
import app.iot.net.response.BasicResponse
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.ByteArrayInputStream
import java.io.InputStreamReader
import java.lang.reflect.Type
import java.nio.charset.StandardCharsets.UTF_8

/**
 * Created by danbo on 2019/1/8.
 */
class BaseConverterFactory(private val gson: Gson) : Converter.Factory() {

    override fun responseBodyConverter(
        type: Type?,
        annotations: Array<Annotation>?,
        retrofit: Retrofit?
    ): Converter<ResponseBody, *>? {
        val adapter = gson.getAdapter(TypeToken.get(type!!))
        return BaseResponseBodyConverter(gson, adapter as TypeAdapter<*>)
    }

}

class BaseResponseBodyConverter<T>(private val gson: Gson, private val adapter: TypeAdapter<T>) : Converter<ResponseBody, T> {
    override fun convert(responseBody: ResponseBody): T? {
        val response = responseBody.string()
        LogUtils.i("------RESPONSE------", response)

        val basicResponse = gson.fromJson(response, BasicResponse::class.java)
        if (basicResponse.data != null && basicResponse.data!!.toString().isEmpty()) {
            //如果resp是空字符串则置为null
            basicResponse.data = null
        }

        val contentType = responseBody.contentType()
        val charset = if (contentType != null) contentType.charset(UTF_8) else UTF_8
        val inputStream = ByteArrayInputStream(gson.toJson(basicResponse).toByteArray())
        val reader = InputStreamReader(inputStream, charset!!)
        val jsonReader = gson.newJsonReader(reader)

        responseBody.use {
            return adapter.read(jsonReader)
        }
    }
}
