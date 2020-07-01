package dev.ahmdaeyz.guardianscope.ui.browser.discover.trending;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import dev.ahmdaeyz.guardianscope.R;
import dev.ahmdaeyz.guardianscope.data.repository.ArticlesRepositoryImpl;
import dev.ahmdaeyz.guardianscope.databinding.FragmentTrendingBinding;
import dev.ahmdaeyz.guardianscope.ui.browser.discover.DelegateToBrowser;

public class TrendingFragment extends Fragment {
    private static final String TAG = "TrendingFragment";
    private TrendingViewModel viewModel;
    private DelegateToBrowser delegateToBrowser;

    public TrendingFragment() {
        // Required empty public constructor
    }

    public void attachingToParentFragment(Fragment fragment) {
        try {
            delegateToBrowser = (DelegateToBrowser) fragment;
        } catch (ClassCastException e) {
            Log.e(TAG, "attachingToParentFragment: ", e);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArticlesRepositoryImpl articlesRepository = ArticlesRepositoryImpl.getInstance();
        TrendingViewModelFactory factory = new TrendingViewModelFactory(articlesRepository);
        viewModel = new ViewModelProvider(this, factory).get(TrendingViewModel.class);
        attachingToParentFragment(getParentFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentTrendingBinding binding = FragmentTrendingBinding.inflate(inflater, container, false);
        TrendingArticlesAdapter adapter = new TrendingArticlesAdapter();
        binding.trendingList.setAdapter(adapter);
        viewModel.articles.observe(getViewLifecycleOwner(), articles -> {
            binding.spinKit.setVisibility(View.GONE);
            if (!articles.isEmpty()) {
                adapter.clear();
                adapter.addAll(articles);
            } else {
                binding.infoText.setText(getString(R.string.no_articles_message));
            }
        });

        adapter.setOnItemClickListener((view, article) -> {
            delegateToBrowser.delegate(article.getApiUrl());
        });
        return binding.getRoot();
    }

}