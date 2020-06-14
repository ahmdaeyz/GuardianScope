package dev.ahmdaeyz.guardianscope.data.network.interceptors;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import dev.ahmdaeyz.guardianscope.util.date.DateMethods;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static dev.ahmdaeyz.guardianscope.data.network.TheGuardianAPIBuilder.API_KEY;

public class CommonQueriesInterceptor implements Interceptor {
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