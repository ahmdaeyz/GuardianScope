package dev.ahmdaeyz.guardianscope.ui.discover.trending;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import dev.ahmdaeyz.guardianscope.data.repository.ArticlesRepository;

class TrendingViewModelFactory implements ViewModelProvider.Factory {
    final private ArticlesRepository articlesRepository;

    TrendingViewModelFactory(ArticlesRepository articlesRepository) {
        this.articlesRepository = articlesRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new TrendingViewModel(articlesRepository);
    }
}
