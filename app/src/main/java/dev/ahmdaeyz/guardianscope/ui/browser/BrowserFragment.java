package dev.ahmdaeyz.guardianscope.ui.browser;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dev.ahmdaeyz.guardianscope.R;
import dev.ahmdaeyz.guardianscope.databinding.FragmentBrowserBinding;
import dev.ahmdaeyz.guardianscope.navigation.NavigateFrom;
import dev.ahmdaeyz.guardianscope.ui.NoConnectionFragmentDirections;
import dev.ahmdaeyz.guardianscope.ui.browser.bookmarks.BookmarksFragmentDirections;
import dev.ahmdaeyz.guardianscope.ui.browser.discover.DiscoverFragmentDirections;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.BehaviorSubject;

import static android.net.ConnectivityManager.NetworkCallback;
import static dev.ahmdaeyz.guardianscope.ui.browser.common.FragmentsLabels.BOOKMARKS_FRAGMENT;
import static dev.ahmdaeyz.guardianscope.ui.browser.common.FragmentsLabels.DISCOVER_FRAGMENT;
import static dev.ahmdaeyz.guardianscope.ui.browser.common.FragmentsLabels.NO_CONNECTION_FRAGMENT;

public class BrowserFragment extends Fragment implements NavigateFrom.Browsers {
    private FragmentBrowserBinding binding;
    private BehaviorSubject<Boolean> isNetworkConnected = BehaviorSubject.createDefault(false);
    boolean isConnected = false;
    NavController navController;
    BehaviorSubject<List<String>> currentDestination = BehaviorSubject.createDefault(new ArrayList<>());
    NavController.OnDestinationChangedListener destinationChangedListener;
    NetworkCallback networkCallback;
    private CompositeDisposable disposables;

    public BrowserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        disposables = new CompositeDisposable();
        networkCallback = new ConnectivityManager.NetworkCallback() {
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

        };
        registerNetworkCallback();
    }

    @Override
    public void onResume() {
        super.onResume();
        destinationChangedListener = (controller, destination, arguments) -> {
            currentDestination.getValue().add(Objects.requireNonNull(
                    destination
                            .getLabel())
                    .toString());
            currentDestination.onNext(currentDestination.getValue());
            Log.d("DestinationHistory", currentDestination.getValue().toString());
        };
        navController.addOnDestinationChangedListener(destinationChangedListener);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBrowserBinding.inflate(inflater, container, false);
        disposables.add(isNetworkConnected
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::changeColorBasedOnConnectivity
                ));
        disposables.add(
                currentDestination
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                (currentHistory) -> {
                                    if (currentHistory.size() > 1) {
                                        String currentDestination = currentHistory
                                                .get(currentHistory
                                                        .size() - 1);
                                        switch (currentHistory
                                                .get(currentHistory
                                                        .size() - 2)) {
                                            case BOOKMARKS_FRAGMENT:
                                                if (currentDestination
                                                        .equals(BOOKMARKS_FRAGMENT)) {
                                                    binding.appBarChildren.transitionToEnd();
                                                } else {
                                                    binding.appBarChildren.transitionToStart();
                                                }
                                                break;
                                            case DISCOVER_FRAGMENT:
                                                if (currentDestination
                                                        .equals(NO_CONNECTION_FRAGMENT)) {
                                                    binding.appBarChildren.transitionToStart();
                                                } else if (!currentDestination
                                                        .equals(DISCOVER_FRAGMENT)) {
                                                    binding.appBarChildren.transitionToEnd();
                                                }

                                            case NO_CONNECTION_FRAGMENT:
                                                if (currentDestination
                                                        .equals(DISCOVER_FRAGMENT)) {
                                                    binding.appBarChildren.transitionToStart();
                                                } else if (currentDestination.equals(BOOKMARKS_FRAGMENT)) {
                                                    binding.appBarChildren.transitionToEnd();
                                                }
                                        }
                                    }
                                }
                        ));

        binding.discoverButton.setOnClickListener((view) -> {
            if (!isConnected) {
                if (currentDestination
                        .getValue()
                        .get(currentDestination.
                                getValue()
                                .size() - 1).equals(BOOKMARKS_FRAGMENT)) {
                    navController
                            .navigate(BookmarksFragmentDirections
                                    .actionBookmarksFragmentToNoConnectionFragment());
                } else {
                    navController
                            .navigate(NoConnectionFragmentDirections
                                    .actionNoConnectionFragmentSelf());
                }
            }

            navigateToDestinationWhenCurrentIs(BOOKMARKS_FRAGMENT, BookmarksFragmentDirections
                    .actionBookmarksFragmentToDiscoverFragment());
        });
        binding.bookmarksButton.setOnClickListener((view) -> {
            switch (currentDestination
                    .getValue()
                    .get(currentDestination.
                            getValue()
                            .size() - 1)) {
                case NO_CONNECTION_FRAGMENT:
                    navController
                            .navigate(NoConnectionFragmentDirections
                                    .actionNoConnectionFragmentToBookmarksFragment());
                    break;
                case DISCOVER_FRAGMENT:
                    navController
                            .navigate(DiscoverFragmentDirections
                                    .actionDiscoverFragmentToBookmarksFragment());
                    break;
            }
        });
        return binding.getRoot();
    }

    private void changeColorBasedOnConnectivity(Boolean isConnected) {
        if (!isConnected) {
            binding.movingDot.setBackground(
                    getResources()
                            .getDrawable(R.drawable.dot_background_grey,
                                    getResources()
                                            .newTheme()));
        } else {
            binding.movingDot.setBackground(
                    getResources()
                            .getDrawable(R.drawable.dot_background_red,
                                    getResources().newTheme()
                            )
            );
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        disposables.add(
                isNetworkConnected
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(connected -> {
                            isConnected = connected;
                            if (!connected) {
                                navigateToDestinationWhenCurrentIs(DISCOVER_FRAGMENT,
                                        DiscoverFragmentDirections
                                                .actionDiscoverFragmentToNoConnectionFragment());
                            } else {
                                navigateToDestinationWhenCurrentIs(NO_CONNECTION_FRAGMENT,
                                        NoConnectionFragmentDirections
                                                .actionNoConnectionFragmentToDiscoverFragment());
                            }
                        }));
    }

    private void navigateToDestinationWhenCurrentIs(
            String currentFragment,
            NavDirections destination) {
        if (currentDestination
                .getValue()
                .get(currentDestination
                        .getValue()
                        .size() - 1)
                .equals(currentFragment)) {
            navController
                    .navigate(destination);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = NavHostFragment
                .findNavController(Objects.requireNonNull(getChildFragmentManager()
                        .findFragmentById(R.id.browser_nav_host_fragment)));
        navController.restoreState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        navController.saveState();
    }

    public void registerNetworkCallback() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) requireActivity()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            Objects.requireNonNull(connectivityManager)
                    .registerDefaultNetworkCallback(networkCallback);
        } catch (Exception e) {
            isNetworkConnected.onNext(false);
        }
    }

    @Override
    public void toReaderFromDiscover(String apiUrl) {
        Navigation
                .findNavController(requireActivity(), R.id.nav_host_fragment)
                .navigate(BrowserFragmentDirections
                        .actionBrowserFragmentToReaderFragment(apiUrl));
    }

    @Override
    public void toReaderFromBookmarks(String apiUrl) {
        Navigation
                .findNavController(requireActivity(), R.id.nav_host_fragment)
                .navigate(BrowserFragmentDirections
                        .actionBrowserFragmentToReaderFragment(apiUrl));
    }

    @Override
    public void onPause() {
        navController
                .removeOnDestinationChangedListener(destinationChangedListener);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        disposables.clear();
        ConnectivityManager connectivityManager = (ConnectivityManager) requireActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        Objects.requireNonNull(connectivityManager)
                .unregisterNetworkCallback(networkCallback);
        super.onDestroy();
    }
}
