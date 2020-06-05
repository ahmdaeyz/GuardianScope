package dev.ahmdaeyz.guardianscope.ui.reader;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import dev.ahmdaeyz.guardianscope.data.model.theguardian.Article;
import dev.ahmdaeyz.guardianscope.data.repository.ArticlesRepository;
import io.reactivex.disposables.CompositeDisposable;

class ReaderViewModel extends ViewModel {
    final ArticlesRepository repository;
    final CompositeDisposable disposables = new CompositeDisposable();

    public ReaderViewModel(ArticlesRepository repository) {
        this.repository = repository;
    }

    public void favouriteArticle(Article article) {
        disposables.add(
                repository.favouriteArticle(article)
                        .subscribe(() -> {
                        }, (throwable) -> {
                            Log.e("FavouriteArticle", "Failed");
                        }));
    }

    public void bookmarkArticle(Article article) {
        disposables.add(
                repository.bookMarkArticle(article)
                        .subscribe(() -> {
                        }, (throwable) -> {
                            Log.e("BookmarkArticle", "Failed");
                        }));
    }


}
