package demo.acube.application.healthcare.service;

import android.content.Context;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import demo.acube.application.healthcare.Config;
import demo.acube.application.healthcare.helper.utilities.GlobalApplication;

/**
 * Created by Anns on 26/04/17.
 */

public class APIClient {
    private static Retrofit retrofit=null;

    public static Retrofit getClient(Context context)
    {
        final GlobalApplication globalApplication = (GlobalApplication)context.getApplicationContext();
        final String authorization = globalApplication.getAccessTokenType()+" "+globalApplication.getAccessToken();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpCLient = new OkHttpClient.Builder();
        httpCLient.readTimeout(60, TimeUnit.SECONDS);
        httpCLient.connectTimeout(60,TimeUnit.SECONDS);
        httpCLient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .header("Authorization",authorization)
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });
        OkHttpClient client = httpCLient.build();
        retrofit = new Retrofit.Builder()
                .baseUrl(Config.API_BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return retrofit;
    }

}
