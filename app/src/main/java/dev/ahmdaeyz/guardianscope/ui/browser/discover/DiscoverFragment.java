package dev.ahmdaeyz.guardianscope.ui.browser.discover;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import dev.ahmdaeyz.guardianscope.R;
import dev.ahmdaeyz.guardianscope.navigation.NavigateFrom;


public class DiscoverFragment extends Fragment implements DelegateToBrowser {
    private static final String TAG = "DiscoverFragment";
    private NavigateFrom.Browsers navigateFromDiscover;

    public void attachingToParentFragment(Fragment fragment) {
        try {
            navigateFromDiscover = (NavigateFrom.Browsers) fragment;
        } catch (ClassCastException e) {
            Log.e(TAG, "attachingToParentFragment: ", e);
        }
    }

    public DiscoverFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        attachingToParentFragment(getParentFragment().getParentFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_discover, container, false);
    }

    @Override
    public void delegate(String apiUrl) {
        navigateFromDiscover.toReaderFromDiscover(apiUrl);
    }
}
