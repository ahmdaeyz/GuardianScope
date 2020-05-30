package dev.ahmdaeyz.guardianscope.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Bundle;
import android.view.LayoutInflater;


import dev.ahmdaeyz.guardianscope.R;
import dev.ahmdaeyz.guardianscope.databinding.ActivityMainBinding;
import dev.ahmdaeyz.guardianscope.ui.discover.DiscoverFragment;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.BehaviorSubject;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    FragmentManager fragmentManager;
    private BehaviorSubject<Boolean> isNetworkConnected = BehaviorSubject.createDefault(false);
    private CompositeDisposable disposables = new CompositeDisposable();
    DiscoverFragment discoverFragment;
    NoConnectionFragment noConnectionFragment = new NoConnectionFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        fragmentManager = getSupportFragmentManager();
        registerNetworkCallback();

        if (savedInstanceState!=null){
            discoverFragment = (DiscoverFragment) fragmentManager.getFragment(savedInstanceState,"DiscoverFragment");
        }else{
            discoverFragment = new DiscoverFragment();
        }
        disposables.add(
        isNetworkConnected
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean isConnected) throws Exception {
                        if (isConnected){
                            fragmentManager.beginTransaction()
                                    .replace(R.id.fragment_container, discoverFragment, "discover_fragment")
                                    .commit();
                        }else{
                            fragmentManager.beginTransaction()
                                    .replace(R.id.fragment_container,noConnectionFragment, "no_connection_fragment")
                                    .commit();
                        }
                    }
                }));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        fragmentManager.putFragment(outState,"DiscoverFragment",fragmentManager.findFragmentByTag("discover_fragment"));
    }

    public void registerNetworkCallback() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
            connectivityManager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback(){
                @Override
                public void onLost(@NonNull Network network) {
                    isNetworkConnected.onNext(false);
                }

                @Override
                public void onUnavailable() {
                    isNetworkConnected.onNext(false);
                }

                @Override
                public void onAvailable(@NonNull Network network) {
                    isNetworkConnected.onNext(true);
                }

            });
        } catch (Exception e) {
            isNetworkConnected.onNext(false);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }
}
