package dev.ahmdaeyz.guardianscope.ui.browser.bookmarks;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import dev.ahmdaeyz.guardianscope.data.repository.ArticlesRepositoryImpl;

class BookmarksViewModelFactory implements ViewModelProvider.Factory {
    final ArticlesRepositoryImpl repository;

    BookmarksViewModelFactory(ArticlesRepositoryImpl repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new BookmarksViewModel(repository);
    }
}
