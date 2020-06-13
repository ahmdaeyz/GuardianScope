package dev.ahmdaeyz.guardianscope.data.network.interceptors;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import dev.ahmdaeyz.guardianscope.util.exceptions.NoConnectivityException;
import dev.ahmdaeyz.guardianscope.util.exceptions.NoInternetException;
import okhttp3.Interceptor;
import okhttp3.Response;

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

    private boolean isInternetAvailable() {
        try {
            int timeoutMs = 1500;
            Socket sock = new Socket();
            SocketAddress sockAddress = new InetSocketAddress("8.8.8.8", 53);
            sock.connect(sockAddress, timeoutMs);
            sock.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
