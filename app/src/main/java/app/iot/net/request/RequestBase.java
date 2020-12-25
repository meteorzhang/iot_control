package app.iot.net.request;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.SortedMap;
import java.util.TreeMap;

import okhttp3.RequestBody;

/**
 * Created by danbo on 16/11/21.
 */
public class RequestBase implements RequestBuilder {

    protected SortedMap<String, Object> params = new TreeMap<>();
    private Gson gson = new Gson();

    @Override
    public SortedMap<String, Object> getRequestMap() {
        return params;
    }

    @NotNull
    @Override
    public RequestBody getRequestBody() {
        String strEntity = gson.toJson(params);
        return RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);
    }
}

