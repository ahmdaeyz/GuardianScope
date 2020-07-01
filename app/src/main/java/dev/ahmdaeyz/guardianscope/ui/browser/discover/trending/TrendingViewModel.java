package dev.ahmdaeyz.guardianscope.ui.browser.discover.trending;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import dev.ahmdaeyz.guardianscope.data.model.theguardian.Article;
import dev.ahmdaeyz.guardianscope.data.repository.ArticlesRepositoryImpl;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

class TrendingViewModel extends ViewModel {
    final private ArticlesRepositoryImpl repository;
    private CompositeDisposable disposables = new CompositeDisposable();
    private MutableLiveData<List<Article>> _articles = new MutableLiveData<>();
    public LiveData<List<Article>> articles = _articles;

    public TrendingViewModel(ArticlesRepositoryImpl articlesRepository) {
        this.repository = articlesRepository;
        getArticles();
    }

    private void getArticles() {
        disposables.add(
                repository.getTrendingArticles()
                        .cache()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe((articles) -> {
                            _articles.postValue(articles);
                        }, (throwable) -> {
                            Log.d("TrendingViewModel", throwable.toString());
                            throwable.printStackTrace();
                        })
        );
    }


}
