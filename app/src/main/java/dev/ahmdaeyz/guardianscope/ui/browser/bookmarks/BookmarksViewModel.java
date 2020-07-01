package dev.ahmdaeyz.guardianscope.ui.browser.bookmarks;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import dev.ahmdaeyz.guardianscope.data.model.theguardian.ArticleWithBody;
import dev.ahmdaeyz.guardianscope.data.repository.ArticlesRepositoryImpl;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

class BookmarksViewModel extends ViewModel {
    final ArticlesRepositoryImpl repository;
    CompositeDisposable disposables = new CompositeDisposable();
    private MutableLiveData<List<ArticleWithBody>> _bookmarks = new MutableLiveData<>();
    public LiveData<List<ArticleWithBody>> bookmarks = _bookmarks;

    public BookmarksViewModel(ArticlesRepositoryImpl repository) {

        this.repository = repository;
        initBookmarks();
    }

    private void initBookmarks() {
        disposables.add(
                repository.getBookmarks()
                        .subscribe(
                                (articles) -> {
                                    _bookmarks.postValue((List<ArticleWithBody>) articles);
                                },
                                (throwable -> {
                                    Log.e("BookmarksViewModel", throwable.toString());
                                })
                        )
        );
    }

    public void unBookmarkArticle(ArticleWithBody article) {
        disposables.add(
                repository.unBookmarkArticle(article)
                        .subscribe(
                                () -> {
                                    Log.i("BookmarksViewModel", "Unbookmarked Successfully");
                                },
                                (throwable) -> {
                                    Log.e("BookmarksViewModel", throwable.toString());
                                }
                        )
        );
    }

    public Observable<List<ArticleWithBody>> search(String keyword) {
        return repository.search(keyword);
    }
}
