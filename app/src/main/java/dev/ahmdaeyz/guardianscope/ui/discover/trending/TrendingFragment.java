package dev.ahmdaeyz.guardianscope.ui.discover.trending;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import dev.ahmdaeyz.guardianscope.R;
import dev.ahmdaeyz.guardianscope.data.repository.ArticlesRepository;
import dev.ahmdaeyz.guardianscope.data.repository.ArticlesRepositoryImpl;
import dev.ahmdaeyz.guardianscope.databinding.FragmentTrendingBinding;

public class TrendingFragment extends Fragment {
    private FragmentTrendingBinding binding;
    private TrendingArticlesAdapter adapter;
    private TrendingViewModel viewModel;

    public TrendingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArticlesRepository articlesRepository = ArticlesRepositoryImpl.getInstance();
        TrendingViewModelFactory factory = new TrendingViewModelFactory(articlesRepository);
        viewModel = new ViewModelProvider(this, factory).get(TrendingViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTrendingBinding.inflate(inflater, container, false);
        adapter = new TrendingArticlesAdapter();
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
            Intent openUrlInABrowser = new Intent(Intent.ACTION_VIEW);
            openUrlInABrowser.setData(Uri.parse(article.getWebUrl()));
            if (openUrlInABrowser.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(openUrlInABrowser);
            }
        });
        return binding.getRoot();
    }


}