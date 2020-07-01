package dev.ahmdaeyz.guardianscope.ui.browser.discover.sections;

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

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import dev.ahmdaeyz.guardianscope.R;
import dev.ahmdaeyz.guardianscope.data.repository.ArticlesRepositoryImpl;
import dev.ahmdaeyz.guardianscope.databinding.FragmentSectionsBinding;
import dev.ahmdaeyz.guardianscope.ui.browser.discover.DelegateToBrowser;
import io.reactivex.disposables.CompositeDisposable;


public class SectionsFragment extends Fragment {
    private static final String TAG = "SectionsFragment";
    private SectionsViewModel viewModel;
    private CompositeDisposable disposable = new CompositeDisposable();
    private DelegateToBrowser delegateToBrowser;

    public SectionsFragment() {
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
        SectionsViewModelFactory factory = new SectionsViewModelFactory(articlesRepository);
        viewModel = new ViewModelProvider(this, factory).get(SectionsViewModel.class);
        attachingToParentFragment(getParentFragment());
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentSectionsBinding binding = FragmentSectionsBinding.inflate(inflater, container, false);
        SectionsArticlesAdapter adapter = new SectionsArticlesAdapter();
        binding.articlesList.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.articlesList.setLayoutManager(layoutManager);
        setTextAppearanceOfCurrentSectionChip(binding);
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
            delegateToBrowser.delegate(article.getApiUrl());
        });
        return binding.getRoot();
    }

    private void setTextAppearanceOfCurrentSectionChip(FragmentSectionsBinding binding) {
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
