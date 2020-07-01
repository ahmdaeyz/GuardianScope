package dev.ahmdaeyz.guardianscope.data.repository;

import android.util.Log;

import org.threeten.bp.LocalDate;

import java.util.List;
import java.util.concurrent.TimeUnit;

import dev.ahmdaeyz.guardianscope.data.db.daos.BookmarkedArticlesDao;
import dev.ahmdaeyz.guardianscope.data.model.theguardian.Article;
import dev.ahmdaeyz.guardianscope.data.model.theguardian.ArticleWithBody;
import dev.ahmdaeyz.guardianscope.data.network.NetworkService;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ArticlesRepositoryImpl {
    private final NetworkService networkService;
    private final BookmarkedArticlesDao bookmarkedArticlesDao;
    private static ArticlesRepositoryImpl INSTANCE;
    private int syncPeriod = 30;
    private boolean shouldKeepBookmarks = true;

    private ArticlesRepositoryImpl(NetworkService networkService, BookmarkedArticlesDao bookmarkedArticlesDao) {
        this.networkService = networkService;
        this.bookmarkedArticlesDao = bookmarkedArticlesDao;
        if (!shouldKeepBookmarks) {
            deleteWeekAgoBookmarks(
                    LocalDate
                            .now()
                            .minusWeeks(1)
                            .toEpochDay()).subscribe();
        }
    }

    public static ArticlesRepositoryImpl initRepository(NetworkService networkService, BookmarkedArticlesDao bookmarkedArticlesDao) {
        if (INSTANCE != null) {
            throw new RuntimeException("you can't reinitialize ArticlesRepository, Already is.");
        } else {
            INSTANCE = new ArticlesRepositoryImpl(networkService, bookmarkedArticlesDao);
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

    public Observable<List<Article>> getTrendingArticles() {
        return Observable.interval(0, syncPeriod, TimeUnit.MINUTES)
                .flatMap((time) -> networkService
                        .getHeadlineArticles()
                        .toObservable());
    }

    public Observable<List<Article>> getSectionsArticles(List<String> sections) {
        return Observable.interval(0, 30, TimeUnit.MINUTES)
                .flatMap((time) -> networkService
                        .getSectionsArticles(sections)
                        .toObservable())
                .doOnNext((articles -> Log.d("Repository", articles.toString())));
    }

    public Completable bookMarkArticle(ArticleWithBody article) {
        return bookmarkedArticlesDao.bookmarkArticle(article).subscribeOn(Schedulers.io());
    }

    public Completable unBookmarkArticle(ArticleWithBody article) {
        return bookmarkedArticlesDao.unBookmarkArticle(article).subscribeOn(Schedulers.io());
    }

    public Observable<List<? extends ArticleWithBody>> getBookmarks() {
        return bookmarkedArticlesDao.getBookmarked()
                .flatMap((articleWithBodies -> Observable.fromIterable(articleWithBodies)
                        .toList()
                        .toObservable().subscribeOn(Schedulers.io())));
    }

    public Observable<List<ArticleWithBody>> search(String keyword) {
        return bookmarkedArticlesDao.search("%" + keyword + "%")
                .subscribeOn(Schedulers.io());
    }

    public Single<ArticleWithBody> getArticle(String apiUrl) {
        return bookmarkedArticlesDao.getBookmarkedArticle(apiUrl)
                .map((article -> {
                    article.setBookmarked(true);
                    return article;
                }))
                .onErrorResumeNext(networkService.getArticle(apiUrl))
                .cache()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Completable deleteWeekAgoBookmarks(long aWeekAgo) {
        return bookmarkedArticlesDao.deleteWeekAgoBookmarks(aWeekAgo)
                .subscribeOn(Schedulers.io());
    }

    public void setSyncPeriod(int syncPeriod) {
        this.syncPeriod = syncPeriod;
    }

    public void toggleKeepBookmarks() {
        this.shouldKeepBookmarks = !shouldKeepBookmarks;
    }
}
