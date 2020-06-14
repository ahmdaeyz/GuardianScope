package dev.ahmdaeyz.guardianscope.data.network;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;

import dev.ahmdaeyz.guardianscope.data.network.interceptors.CacheInterceptor;
import dev.ahmdaeyz.guardianscope.data.network.interceptors.CommonQueriesInterceptor;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TheGuardianAPIBuilder {
    public static final String API_KEY = "abbbdc43-bfd3-498c-afbf-41e9f87bbbb8";
    private static final String BASE_URL = "https://content.guardianapis.com/";
    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    private Interceptor connectivityInterceptor;
    private CacheInterceptor cacheInterceptor;

    TheGuardianAPIBuilder(Interceptor connectivityInterceptor, CacheInterceptor cacheInterceptor) {
        this.connectivityInterceptor = connectivityInterceptor;
        this.cacheInterceptor = cacheInterceptor;
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
    }


    private Retrofit buildRetrofit() {
        File httpCacheDirectory = new File(cacheInterceptor.getCacheDirectory(), "http-cache");
        int cacheSize = 5 * 1024 * 1024;
        Cache cache = new Cache(httpCacheDirectory, cacheSize);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(logging)
                .addNetworkInterceptor(cacheInterceptor)
                .addNetworkInterceptor(connectivityInterceptor)
                .addNetworkInterceptor(new CommonQueriesInterceptor())
                .cache(cache)
                .build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    public TheGuardianAPI create(Class<TheGuardianAPI> theGuardianAPIClass) {
        return buildRetrofit().create(theGuardianAPIClass);
    }

}
