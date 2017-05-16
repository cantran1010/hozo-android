package vn.tonish.hozo.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by LongBui on 09/05/2017.
 */
public class ApiClient {

    public static final String BASE_URL = "https://private-anon-f117a0bf31-hozodev.apiary-mock.com/v1/";
//    public static final String BASE_URL = "http://104.198.92.15:8080/v1/";

    private static Retrofit retrofit = null;

    public static ApiInterface getApiService() {
        if (retrofit == null) {
            OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
            builder.readTimeout(15, TimeUnit.SECONDS);
            builder.connectTimeout(15, TimeUnit.SECONDS);
            builder.writeTimeout(15, TimeUnit.SECONDS);
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            builder.interceptors().add(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    Request original = chain.request();

                    // Request customization: add request headers
                    Request.Builder requestBuilder = original.newBuilder();
                    requestBuilder.removeHeader("Content-type");
                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
            retrofit = new Retrofit.Builder()
                    .client(builder.build())
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit.create(ApiInterface.class);
    }
}
