package dev.ahmdaeyz.guardianscope.ui.discover.sections;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import java.util.Arrays;

import dev.ahmdaeyz.guardianscope.R;
import dev.ahmdaeyz.guardianscope.data.repository.ArticlesRepository;
import dev.ahmdaeyz.guardianscope.data.repository.ArticlesRepositoryImpl;
import dev.ahmdaeyz.guardianscope.databinding.FragmentSectionsBinding;
import io.reactivex.disposables.CompositeDisposable;


public class SectionsFragment extends Fragment {
    private FragmentSectionsBinding binding;
    private SectionsArticlesAdapter adapter;
    private SectionsViewModel viewModel;
    private CompositeDisposable disposable = new CompositeDisposable();

    public SectionsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArticlesRepository articlesRepository = ArticlesRepositoryImpl.getInstance();
        SectionsViewModelFactory factory = new SectionsViewModelFactory(articlesRepository);
        viewModel = new ViewModelProvider(this, factory).get(SectionsViewModel.class);
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
        viewModel.setSections(Arrays.asList("politics", "fashion", "environment", "education", "media", "food"));
        viewModel.articles.observe(getViewLifecycleOwner(), (articles) -> {
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

    private void setTextAppearanceOfCurrentSectionChip() {
        binding.sectionsChips.setOnCheckedChangeListener((group, checkedId) -> {
            Chip[] chips = {binding.educationChip, binding.environmentChip, binding.fashionChip, binding.mediaChip, binding.politicsChip, binding.foodChip};
            Chip checkedChip = binding.getRoot().findViewById(checkedId);
            if (checkedChip != null) {
                viewModel.currentSection.onNext(checkedChip.getText().toString());
                Log.d("SelectedChipText", checkedChip.getText().toString());
                checkedChip.setTextAppearance(R.style.TextAppearance_MaterialComponents_Body1);
                for (Chip chip : chips) {
                    if (chip.getId() != checkedId) {
                        chip.setTextAppearance(R.style.TextAppearance_MaterialComponents_Body2);
                    }
                }
            } else {
                for (Chip chip : chips) {
                    if (chip.getText().toString().equals(viewModel.currentSection.getValue())) {
                        chip.setChecked(true);
                    }
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        disposable.clear();
        super.onDestroyView();

    }
}
