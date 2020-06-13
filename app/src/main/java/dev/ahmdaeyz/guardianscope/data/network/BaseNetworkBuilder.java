package dev.ahmdaeyz.guardianscope.data.network;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import dev.ahmdaeyz.guardianscope.util.date.DateMethods;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class TheGuardianAPIBuilder {
    private static final String API_KEY = "abbbdc43-bfd3-498c-afbf-41e9f87bbbb8";
    private static final String BASE_URL = "https://content.guardianapis.com/";
    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    private Interceptor connectivityInterceptor;

    TheGuardianAPIBuilder(Interceptor connectivityInterceptor) {
        this.connectivityInterceptor = connectivityInterceptor;
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
    }


    private Retrofit buildRetrofit() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(logging)
                .addNetworkInterceptor(connectivityInterceptor)
                .addNetworkInterceptor(new CommonQueriesInterceptor())
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

    static class CommonQueriesInterceptor implements Interceptor {
        @NotNull
        @Override
        public Response intercept(@NotNull Chain chain) throws IOException {
            HttpUrl.Builder requestUrlBuilder = chain.request().url().newBuilder();
            if (chain.request().url().queryParameter("pageSize") != null) {
                requestUrlBuilder
                        .addQueryParameter("type", "article|liveblog")
                        .addQueryParameter("from-date", DateMethods.yesterdayAndToday()[0])
                        .addQueryParameter("to-date", DateMethods.yesterdayAndToday()[1]);
            }
            HttpUrl requestUrl = requestUrlBuilder.addQueryParameter("api-key", API_KEY)
                    .build();
            Request newRequest = chain.request().newBuilder().url(requestUrl).build();
            return chain.proceed(newRequest);
        }
    }
}
