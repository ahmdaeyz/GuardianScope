package dev.ahmdaeyz.guardianscope.data.network.interceptors;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Response;

public class CacheInterceptor implements Interceptor {
    private File cacheDirectory;

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        CacheControl cacheControl = new CacheControl.Builder()
                .maxAge(30, TimeUnit.MINUTES)
                .maxStale(31, TimeUnit.MINUTES)
                .build();
        return response.newBuilder()
                .removeHeader("Pragma")
                .removeHeader("Cache-Control")
                .header("Cache-Control", cacheControl.toString())
                .build();
    }

    public File getCacheDirectory() {
        return cacheDirectory;
    }

    public void setCacheDirectory(File cacheDirectory) {
        this.cacheDirectory = cacheDirectory;
    }
}
