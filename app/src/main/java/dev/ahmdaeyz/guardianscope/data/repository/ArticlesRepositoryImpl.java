package dev.ahmdaeyz.guardianscope.data.repository;

import android.util.Log;

import java.util.List;
import java.util.concurrent.TimeUnit;

import dev.ahmdaeyz.guardianscope.data.model.theguardian.Article;
import dev.ahmdaeyz.guardianscope.data.network.NetworkService;
import io.reactivex.Completable;
import io.reactivex.Observable;

public class ArticlesRepositoryImpl implements ArticlesRepository {
    private final NetworkService networkService;
    private static ArticlesRepositoryImpl INSTANCE;

    private ArticlesRepositoryImpl(NetworkService networkService) {
        this.networkService = networkService;
    }

    public static ArticlesRepositoryImpl initRepository(NetworkService networkService) {
        if (INSTANCE != null) {
            throw new RuntimeException("you can't reinitialize ArticlesRepository, Already is.");
        } else {
            INSTANCE = new ArticlesRepositoryImpl(networkService);
            return INSTANCE;
        }
    }

    public static ArticlesRepositoryImpl getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        } else {
            throw new RuntimeException("you have to initialize the repository first.");
        }
    }

    @Override
    public Observable<List<Article>> getTrendingArticles() {
        return Observable.interval(0, 30, TimeUnit.MINUTES)
                .flatMap((time) -> networkService
                        .getHeadlineArticles()
                        .toObservable());
    }

    @Override
    public Observable<List<Article>> getSectionsArticles(List<String> sections) {
        return Observable.interval(0, 30, TimeUnit.MINUTES)
                .flatMap((time) -> networkService
                        .getSectionsArticles(sections)
                        .toObservable())
                .doOnNext((articles -> Log.d("Repository", articles.toString())));
    }

    @Override
    public Completable bookMarkArticle(Article article) {
        return null;
    }

    @Override
    public Observable<List<Article>> getBookmarks() {
        return null;
    }

    @Override
    public Completable favouriteArticle(Article article) {
        return null;
    }

    @Override
    public Observable<List<Article>> getFavourites() {
        return null;
    }

    @Override
    public Observable<List<Article>> search(String keyword) {
        return null;
    }
}
