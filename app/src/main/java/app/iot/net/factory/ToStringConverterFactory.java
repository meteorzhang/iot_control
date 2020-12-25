package app.iot.net.factory;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by danbo on 2020/10/22.
 */
public class ToStringConverterFactory extends Converter.Factory {

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(@NotNull Type type, @NotNull Annotation[] annotations, @NotNull Retrofit retrofit) {
        if (String.class.equals(type)) {
            return (Converter<ResponseBody, String>) ResponseBody::string;
        }
        return null;
    }
}