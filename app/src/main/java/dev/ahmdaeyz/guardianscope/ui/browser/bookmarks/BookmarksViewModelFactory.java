package dev.ahmdaeyz.guardianscope.ui.browser.bookmarks;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import dev.ahmdaeyz.guardianscope.data.repository.ArticlesRepository;

class BookmarksViewModelFactory implements ViewModelProvider.Factory {
    final ArticlesRepository repository;

    BookmarksViewModelFactory(ArticlesRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new BookmarksViewModel(repository);
    }
}
