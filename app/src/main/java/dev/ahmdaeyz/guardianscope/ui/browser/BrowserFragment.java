package dev.ahmdaeyz.guardianscope.ui.browser;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import dev.ahmdaeyz.guardianscope.R;
import dev.ahmdaeyz.guardianscope.databinding.FragmentBrowserBinding;
import dev.ahmdaeyz.guardianscope.ui.NoConnectionFragment;
import dev.ahmdaeyz.guardianscope.ui.browser.discover.DiscoverFragment;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.BehaviorSubject;

public class BrowserFragment extends Fragment {
    DiscoverFragment discoverFragment;
    NoConnectionFragment noConnectionFragment = new NoConnectionFragment();
    private FragmentBrowserBinding binding;
    private FragmentManager fragmentManager;
    private BehaviorSubject<Boolean> isNetworkConnected = BehaviorSubject.createDefault(false);
    private CompositeDisposable disposables = new CompositeDisposable();

    public BrowserFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerNetworkCallback();
        fragmentManager = getChildFragmentManager();
        if (savedInstanceState != null) {
            discoverFragment = (DiscoverFragment) fragmentManager.getFragment(savedInstanceState, "DiscoverFragment");
        } else {
            discoverFragment = new DiscoverFragment();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBrowserBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        disposables.add(
                isNetworkConnected
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(isConnected -> {
                            if (isConnected) {
                                fragmentManager.beginTransaction()
                                        .replace(R.id.fragment_container, discoverFragment, "discover_fragment")
                                        .commit();
                            } else {
                                fragmentManager.beginTransaction()
                                        .replace(R.id.fragment_container, noConnectionFragment, "no_connection_fragment")
                                        .commit();
                            }
                        }));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        fragmentManager.putFragment(outState, "DiscoverFragment", fragmentManager.findFragmentByTag("discover_fragment"));
    }

    public void registerNetworkCallback() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            connectivityManager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {
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
    public void onDestroyView() {
        super.onDestroyView();
        disposables.clear();
    }
}
