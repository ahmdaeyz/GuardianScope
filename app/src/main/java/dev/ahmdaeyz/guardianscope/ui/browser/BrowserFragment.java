package dev.ahmdaeyz.guardianscope.ui.browser;

import android.annotation.SuppressLint;
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
import androidx.fragment.app.FragmentTransaction;

import com.mikhaellopez.rxanimation.RxAnimation;
import com.mikhaellopez.rxanimation.RxAnimationExtensionKt;

import dev.ahmdaeyz.guardianscope.R;
import dev.ahmdaeyz.guardianscope.databinding.FragmentBrowserBinding;
import dev.ahmdaeyz.guardianscope.ui.NoConnectionFragment;
import dev.ahmdaeyz.guardianscope.ui.browser.bookmarks.BookmarksFragment;
import dev.ahmdaeyz.guardianscope.ui.browser.discover.DiscoverFragment;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.BehaviorSubject;

public class BrowserFragment extends Fragment {
    public static final String BOOKMARKS_TAG = "bookmarks_fragment";
    public static final String DISCOVER_TAG = "discover_fragment";
    Fragment currentFragment;
    NoConnectionFragment noConnectionFragment = new NoConnectionFragment();
    private FragmentBrowserBinding binding;
    private FragmentManager fragmentManager;
    private BehaviorSubject<Boolean> isNetworkConnected = BehaviorSubject.createDefault(false);
    private CompositeDisposable disposables = new CompositeDisposable();
    Completable fromDiscoverToBookmarksAnimation;
    Completable fromBookmarksToDiscoverAnimation;
    private String fragmentBeforeInternetLost = DISCOVER_TAG;

    public BrowserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerNetworkCallback();
        fragmentManager = getChildFragmentManager();
        if (savedInstanceState != null) {
            if (fragmentManager.getFragment(savedInstanceState, "BookmarksFragment") == null) {
                currentFragment = fragmentManager.getFragment(savedInstanceState, "DiscoverFragment");
            } else {
                currentFragment = fragmentManager.getFragment(savedInstanceState, "BookmarksFragment");
            }
        } else {
            currentFragment = new DiscoverFragment();
        }

    }

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBrowserBinding.inflate(inflater, container, false);

        fromBookmarksToDiscoverAnimation = RxAnimation.INSTANCE.sequentially(
                Completable.fromObservable(RxAnimationExtensionKt.alpha(RxAnimation.INSTANCE.from(binding.movingDotRight), 0, 50L, null, null, false)),
                Completable.fromObservable(RxAnimationExtensionKt.rangeFloat(RxAnimation.INSTANCE.from(binding.bookmarksButton), 20, 12, 50L, null, false, (size) -> {
                    binding.bookmarksButton.setTextSize(size);
                    return null;
                })),
                Completable.fromObservable(RxAnimationExtensionKt.alpha(RxAnimation.INSTANCE.from(binding.movingDot), 1, 100L, null, null, false)),
                Completable.fromObservable(RxAnimationExtensionKt.rangeFloat(RxAnimation.INSTANCE.from(binding.discoverButton), 12, 20, 50L, null, false, (size) -> {
                    binding.discoverButton.setTextSize(size);
                    return null;
                }))
        );
        fromDiscoverToBookmarksAnimation = RxAnimation.INSTANCE.sequentially(
                Completable.fromObservable(RxAnimationExtensionKt.alpha(RxAnimation.INSTANCE.from(binding.movingDot), 0, 50L, null, null, false)),
                Completable.fromObservable(RxAnimationExtensionKt.rangeFloat(RxAnimation.INSTANCE.from(binding.discoverButton), 20, 12, 50L, null, false, (size) -> {
                    binding.discoverButton.setTextSize(size);
                    return null;
                })),
                Completable.fromObservable(RxAnimationExtensionKt.alpha(RxAnimation.INSTANCE.from(binding.movingDotRight), 1, 100L, null, null, false)),
                Completable.fromObservable(RxAnimationExtensionKt.rangeFloat(RxAnimation.INSTANCE.from(binding.bookmarksButton), 12, 20, 50L, null, false, (size) -> {
                    binding.bookmarksButton.setTextSize(size);
                    return null;
                }))
        );

        binding.discoverButton.setOnClickListener((view) -> {
            if (!currentFragment.getTag().equals(DISCOVER_TAG)) {
                fromBookmarksToDiscoverAnimation
                        .subscribe();
                currentFragment = new DiscoverFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, currentFragment, DISCOVER_TAG)
                        .addToBackStack(null)
                        .commit();

            }
        });
        binding.bookmarksButton.setOnClickListener((view) -> {
            if (!currentFragment.getTag().equals(BOOKMARKS_TAG)) {
                fromDiscoverToBookmarksAnimation
                        .subscribe();
                currentFragment = new BookmarksFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, currentFragment, BOOKMARKS_TAG)
                        .addToBackStack(null)
                        .commit();
            }
        });
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
                                switch (fragmentBeforeInternetLost) {
                                    case DISCOVER_TAG:
                                        if (currentFragment instanceof DiscoverFragment) {
                                            fragmentManager.beginTransaction()
                                                    .replace(R.id.fragment_container, currentFragment, DISCOVER_TAG)
                                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                                    .addToBackStack(null)
                                                    .commit();
                                        } else {
                                            currentFragment = new DiscoverFragment();
                                            fragmentManager.beginTransaction()
                                                    .replace(R.id.fragment_container, currentFragment, DISCOVER_TAG)
                                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                                    .addToBackStack(null)
                                                    .commit();
                                        }
                                        break;
                                    case BOOKMARKS_TAG:
                                        if (currentFragment instanceof BookmarksFragment) {
                                            fragmentManager.beginTransaction()
                                                    .replace(R.id.fragment_container, currentFragment, BOOKMARKS_TAG)
                                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                                    .addToBackStack(null)
                                                    .commit();
                                        } else {
                                            currentFragment = new BookmarksFragment();
                                            fragmentManager.beginTransaction()
                                                    .replace(R.id.fragment_container, currentFragment, BOOKMARKS_TAG)
                                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                                    .addToBackStack(null)
                                                    .commit();
                                        }
                                        break;
                                }

                            } else {
                                fragmentBeforeInternetLost = currentFragment.getTag();
                                // dispatch a notification here to encourage checking bookmarks
                                fragmentManager.beginTransaction()
                                        .replace(R.id.fragment_container, noConnectionFragment, "no_connection_fragment")
                                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                        .commit();
                            }
                        }));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (currentFragment.getTag().equals(DISCOVER_TAG)) {
            fragmentManager.putFragment(outState, "DiscoverFragment", currentFragment);
        } else {
            fragmentManager.putFragment(outState, "BookmarksFragment", currentFragment);
        }
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
