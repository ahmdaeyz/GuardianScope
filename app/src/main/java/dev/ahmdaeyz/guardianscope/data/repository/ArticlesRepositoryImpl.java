package dev.ahmdaeyz.guardianscope.data.repository;

import android.util.Log;

import org.threeten.bp.LocalDateTime;

import java.util.List;
import java.util.concurrent.TimeUnit;

import dev.ahmdaeyz.guardianscope.data.model.theguardian.Article;
import dev.ahmdaeyz.guardianscope.data.model.theguardian.ArticleWithBody;
import dev.ahmdaeyz.guardianscope.data.model.theguardian.BookmarkedArticle;
import dev.ahmdaeyz.guardianscope.data.network.NetworkService;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ArticlesRepositoryImpl implements ArticlesRepository {
    private final NetworkService networkService;
    private static ArticlesRepositoryImpl INSTANCE;
    private LocalDateTime lastTimeUpdated;

    private ArticlesRepositoryImpl(NetworkService networkService) {
        this.networkService = networkService;
        this.lastTimeUpdated = LocalDateTime.of(1999, 4, 3, 4, 5);
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
                .flatMap((time) -> {
                    lastTimeUpdated = LocalDateTime.now();
                    return networkService
                            .getHeadlineArticles()
                            .toObservable();
                });
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
        return Completable.complete();
    }

    @Override
    public Observable<List<BookmarkedArticle>> getBookmarks() {
        return null;
    }

    @Override
    public Observable<List<Article>> search(String keyword) {
        return null;
    }

    @Override
    public LocalDateTime getLastTimeUpdated() {
        return lastTimeUpdated;
    }

    @Override
    public Single<ArticleWithBody> getArticle(String apiUrl) {
        return networkService.getArticle(apiUrl)
                .cache()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
