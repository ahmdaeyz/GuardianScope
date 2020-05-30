package dev.ahmdaeyz.guardianscope.ui.discover.trending;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import dev.ahmdaeyz.guardianscope.R;
import dev.ahmdaeyz.guardianscope.data.entities.Article;
import dev.ahmdaeyz.guardianscope.data.network.NetworkService;
import dev.ahmdaeyz.guardianscope.databinding.FragmentTrendingBinding;
import dev.ahmdaeyz.guardianscope.ui.discover.common.ArticlesAdapter;

public class TrendingFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Article>> {
    private FragmentTrendingBinding binding;
    private NetworkService service;
    private TrendingArticlesAdapter adapter;

    public TrendingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = new NetworkService();
        getLoaderManager().initLoader(0, savedInstanceState, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTrendingBinding.inflate(inflater, container, false);
        adapter = new TrendingArticlesAdapter();
        binding.trendingList.setAdapter(adapter);

        adapter.setOnItemClickListener(new ArticlesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull View view, Article article) {
                Intent openUrlInABrowser = new Intent(Intent.ACTION_VIEW);
                openUrlInABrowser.setData(Uri.parse(article.getWebUrl()));
                if (openUrlInABrowser.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(openUrlInABrowser);
                }
            }
        });
        return binding.getRoot();
    }


    @NonNull
    @Override
    public TrendingAsyncTaskLoader onCreateLoader(int id, @Nullable Bundle args) {
        return new TrendingAsyncTaskLoader(requireContext(), service);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Article>> loader, List<Article> data) {
        binding.spinKit.setVisibility(View.GONE);
        if (data != null) {
            if (data.isEmpty()) {
                binding.infoText.setText(R.string.no_articles_message);
            } else {
                adapter.clear();
                adapter.addAll(data);
            }
        } else {
            binding.infoText.setText(R.string.error_message);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {
        adapter.clear();
    }
}
