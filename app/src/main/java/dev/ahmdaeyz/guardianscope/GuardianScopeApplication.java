package dev.ahmdaeyz.guardianscope;

import android.app.Application;

import com.jakewharton.threetenabp.AndroidThreeTen;

import dev.ahmdaeyz.guardianscope.data.network.GuardianScopeNetworkService;
import dev.ahmdaeyz.guardianscope.data.network.NetworkService;
import dev.ahmdaeyz.guardianscope.data.network.interceptors.ConnectivityInterceptor;
import dev.ahmdaeyz.guardianscope.data.repository.ArticlesRepositoryImpl;
import okhttp3.Interceptor;

//import dev.ahmdaeyz.guardianscope.data.network.BasicNetworkService;

public class GuardianScopeApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
        initializeSingletons();
    }

    void initializeSingletons() {
        Interceptor connectivityInterceptor = new ConnectivityInterceptor(this);
        NetworkService networkService = new GuardianScopeNetworkService(connectivityInterceptor);
        ArticlesRepositoryImpl.initRepository(networkService);
    }
}
