package dev.ahmdaeyz.guardianscope.data.repository;

import java.util.List;
import java.util.concurrent.TimeUnit;

import dev.ahmdaeyz.guardianscope.data.model.theguardian.Article;
import dev.ahmdaeyz.guardianscope.data.network.NetworkService;
import io.reactivex.Completable;
import io.reactivex.Observable;

class ArticlesRepositoryImpl implements ArticlesRepository {
    private final NetworkService networkService;

    ArticlesRepositoryImpl(NetworkService networkService) {
        this.networkService = networkService;
    }

    @Override
    public Observable<List<Article>> getTrendingArticles() {
        return Observable.interval(30, TimeUnit.MINUTES)
                .flatMap((time) -> networkService
                        .getHeadlineArticles()
                        .toObservable());
    }

    @Override
    public Observable<List<Article>> getSectionsArticles(String... sections) {
        return Observable.interval(30, TimeUnit.MINUTES)
                .flatMap((time) -> networkService
                        .getSectionsArticles(sections)
                        .toObservable());
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
