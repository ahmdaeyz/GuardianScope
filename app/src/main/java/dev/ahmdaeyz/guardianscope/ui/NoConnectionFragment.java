package dev.ahmdaeyz.guardianscope.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dev.ahmdaeyz.guardianscope.R;
import dev.ahmdaeyz.guardianscope.databinding.FragmentNoConnectionBinding;


public class NoConnectionFragment extends Fragment {
    private FragmentNoConnectionBinding binding;
    public NoConnectionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNoConnectionBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }
}