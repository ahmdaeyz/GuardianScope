package dev.ahmdaeyz.guardianscope.data.network.interceptors;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import dev.ahmdaeyz.guardianscope.util.exceptions.NoConnectivityException;
import dev.ahmdaeyz.guardianscope.util.exceptions.NoInternetException;
import okhttp3.Interceptor;
import okhttp3.Response;

import static dev.ahmdaeyz.guardianscope.util.network.NetworkUtils.isInternetAvailable;

public class ConnectivityInterceptor implements Interceptor {
    private final Context context;

    public ConnectivityInterceptor(Context context) {
        this.context = context;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {

        if (!isConnectedToWIFIorCellularNetwork()) {
            throw new NoConnectivityException();
        } else if (!isInternetAvailable()) {
            throw new NoInternetException();
        }
        return chain.proceed(chain.request());
    }

    private boolean isConnectedToWIFIorCellularNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = connectivityManager.getActiveNetwork();
        NetworkCapabilities connection =
                connectivityManager.getNetworkCapabilities(network);

        return connection != null && (
                connection.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        connection.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
    }


}
