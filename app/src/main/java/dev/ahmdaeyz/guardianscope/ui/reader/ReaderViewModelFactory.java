package dev.ahmdaeyz.guardianscope.ui.reader;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import dev.ahmdaeyz.guardianscope.data.repository.ArticlesRepository;

class ReaderViewModelFactory implements ViewModelProvider.Factory {
    private final ArticlesRepository articlesRepository;

    ReaderViewModelFactory(ArticlesRepository articlesRepository) {
        this.articlesRepository = articlesRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ReaderViewModel(articlesRepository);
    }
}
