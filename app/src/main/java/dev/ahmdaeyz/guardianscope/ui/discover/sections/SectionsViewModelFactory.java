package dev.ahmdaeyz.guardianscope.ui.discover.sections;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import dev.ahmdaeyz.guardianscope.data.repository.ArticlesRepository;

class SectionsViewModelFactory implements ViewModelProvider.Factory {
    final private ArticlesRepository articlesRepository;

    SectionsViewModelFactory(ArticlesRepository articlesRepository) {
        this.articlesRepository = articlesRepository;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SectionsViewModel(articlesRepository);
    }
}
