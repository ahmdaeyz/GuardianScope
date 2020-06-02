package dev.ahmdaeyz.guardianscope;

import android.app.Application;

import com.jakewharton.threetenabp.AndroidThreeTen;

import dev.ahmdaeyz.guardianscope.data.network.BasicNetworkService;
import dev.ahmdaeyz.guardianscope.data.network.NetworkService;
import dev.ahmdaeyz.guardianscope.data.repository.ArticlesRepositoryImpl;

public class GuardianScopeApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
        initializeSingletons();
    }

    void initializeSingletons() {
        NetworkService networkService = new BasicNetworkService();
        ArticlesRepositoryImpl.initRepository(networkService);
    }
}
