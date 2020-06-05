package dev.ahmdaeyz.guardianscope.ui.browser.discover;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import dev.ahmdaeyz.guardianscope.databinding.FragmentDiscoverBinding;


public class DiscoverFragment extends Fragment {
    private FragmentDiscoverBinding binding;

    public DiscoverFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDiscoverBinding.inflate(inflater,container,false);

        return binding.getRoot();
    }
}
