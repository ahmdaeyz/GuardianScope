package dev.ahmdaeyz.guardianscope.ui.reader;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import dev.ahmdaeyz.guardianscope.data.model.theguardian.Article;
import dev.ahmdaeyz.guardianscope.data.repository.ArticlesRepository;
import io.reactivex.disposables.CompositeDisposable;

class ReaderViewModel extends ViewModel {
    final ArticlesRepository repository;
    final CompositeDisposable disposables = new CompositeDisposable();
    private MutableLiveData<Article> _article = new MutableLiveData<>();
    LiveData<Article> article = _article;

    public ReaderViewModel(ArticlesRepository repository) {
        this.repository = repository;
    }

    public void favouriteArticle() {
        disposables.add(
                repository.favouriteArticle(article.getValue())
                        .subscribe(() -> {
                        }, (throwable) -> {
                            Log.e("FavouriteArticle", "Failed");
                        }));
    }

    public void bookmarkArticle() {
        disposables.add(
                repository.bookMarkArticle(article.getValue())
                        .subscribe(() -> {
                        }, (throwable) -> {
                            Log.e("BookmarkArticle", "Failed");
                        }));
    }

    public void apiUrlIs(String apiUrl) {
        disposables.add(
                repository.getArticle(apiUrl)
                        .subscribe(
                                (article) -> {
                                    _article.postValue(article);
                                },
                                (throwable) -> {
                                    // handle error
                                }
                        )
        );
    }

}
