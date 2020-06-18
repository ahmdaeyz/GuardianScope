package dev.ahmdaeyz.guardianscope.ui.browser.bookmarks;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import dev.ahmdaeyz.guardianscope.data.repository.ArticlesRepository;
import dev.ahmdaeyz.guardianscope.data.repository.ArticlesRepositoryImpl;
import dev.ahmdaeyz.guardianscope.databinding.FragmentBookmarksBinding;
import dev.ahmdaeyz.guardianscope.navigation.NavigateFrom;
import tyrantgit.explosionfield.ExplosionField;

public class BookmarksFragment extends Fragment {
    private FragmentBookmarksBinding binding;
    private NavigateFrom.Browsers navigateFromBookmarks;
    private BookmarksViewModel viewModel;

    public BookmarksFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            navigateFromBookmarks = (NavigateFrom.Browsers) context;
        } catch (ClassCastException e) {
            Log.e("BookmarksFragment", "Activity have to implement NavigateFrom.Browsers.");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArticlesRepository repository = ArticlesRepositoryImpl.getInstance();
        BookmarksViewModelFactory factory = new BookmarksViewModelFactory(repository);
        viewModel = new ViewModelProvider(this, factory).get(BookmarksViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBookmarksBinding.inflate(inflater, container, false);
        ExplosionField explosionField = ExplosionField.attach2Window(this.getActivity());
        BookmarksAdapter adapter = new BookmarksAdapter();

        viewModel.bookmarks.observe(getViewLifecycleOwner(), articleWithBodies -> {
            if (!articleWithBodies.isEmpty()) {
                binding.infoText.setVisibility(View.GONE);
                adapter.clear();
                adapter.addAll(articleWithBodies);
            } else {
                binding.infoText.setVisibility(View.VISIBLE);
            }

        });
        adapter.setOnItemClickListener((view, article) -> {
            navigateFromBookmarks.toReader(article.getApiUrl());
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        binding.bookmarkedArticlesList.setAdapter(adapter);
        binding.bookmarkedArticlesList.setLayoutManager(layoutManager);

        adapter.setOnDestroyItemListener(((view, article) -> {
            explosionField.explode((View) view.getParent());
            viewModel.unBookmarkArticle(article);
            adapter.removeItem(article);
        }));
        return binding.getRoot();
    }
}