package dev.ahmdaeyz.guardianscope.ui.discover.sections;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;
import java.util.stream.Collectors;

import dev.ahmdaeyz.guardianscope.R;
import dev.ahmdaeyz.guardianscope.data.entities.Article;
import dev.ahmdaeyz.guardianscope.data.network.NetworkService;
import dev.ahmdaeyz.guardianscope.databinding.FragmentSectionsBinding;
import dev.ahmdaeyz.guardianscope.ui.discover.common.ArticlesAdapter;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.BehaviorSubject;

public class SectionsFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Article>> {
    private FragmentSectionsBinding binding;
    private NetworkService service;
    private List<Article> articles;
    private SectionsArticlesAdapter adapter;
    private CompositeDisposable disposables = new CompositeDisposable();
    BehaviorSubject<String> currentSection = BehaviorSubject.createDefault("Politics");

    public SectionsFragment() {
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
        binding = FragmentSectionsBinding.inflate(inflater, container, false);
        adapter = new SectionsArticlesAdapter();
        binding.articlesList.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.articlesList.setLayoutManager(layoutManager);
        setTextAppearanceOfCurrentSectionChip();
        disposables.add(
                currentSection
                        .skip(1)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                adapter.clear();
                                adapter.addAll(currentSectionArticles());
                            }
                        }));
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

    private void setTextAppearanceOfCurrentSectionChip() {
        binding.sectionsChips.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                Chip[] chips = {binding.educationChip, binding.environmentChip, binding.fashionChip, binding.mediaChip, binding.politicsChip, binding.foodChip};
                Chip checkedChip = binding.getRoot().findViewById(checkedId);
                if (checkedChip != null) {
                    currentSection.onNext(checkedChip.getText().toString());
                    Log.d("SelectedChipText", checkedChip.getText().toString());
                    checkedChip.setTextAppearance(R.style.TextAppearance_MaterialComponents_Body1);
                    for (Chip chip : chips) {
                        if (chip.getId() != checkedId) {
                            chip.setTextAppearance(R.style.TextAppearance_MaterialComponents_Body2);
                        }
                    }
                }
            }
        });
    }

    @NonNull
    @Override
    public Loader<List<Article>> onCreateLoader(int id, @Nullable Bundle args) {
        return new SectionsArticlesAsyncTaskLoader(requireContext(), service);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Article>> loader, List<Article> data) {
        binding.spinKit.setVisibility(View.GONE);
        articles = data;
        if (articles.isEmpty()) {
            binding.infoText.setText(R.string.no_articles_message);
        } else {
            adapter.clear();
            List<Article> currentSectionArticles = currentSectionArticles();
            adapter.addAll(currentSectionArticles);
        }
    }

    private List<Article> currentSectionArticles() {
        return articles.stream()
                .map(Article.class::cast)
                .filter((article -> article.getSectionName().equals(currentSection.getValue())))
                .collect(Collectors.toList());
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Article>> loader) {
        adapter.clear();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposables.clear();
    }
}
