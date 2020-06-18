package dev.ahmdaeyz.guardianscope.data.repository;

import android.util.Log;

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

public class ArticlesRepositoryImpl implements ArticlesRepository {
    private final NetworkService networkService;
    private final BookmarkedArticlesDao bookmarkedArticlesDao;
    private static ArticlesRepositoryImpl INSTANCE;

    private ArticlesRepositoryImpl(NetworkService networkService, BookmarkedArticlesDao bookmarkedArticlesDao) {
        this.networkService = networkService;
        this.bookmarkedArticlesDao = bookmarkedArticlesDao;
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
    public Completable bookMarkArticle(ArticleWithBody article) {
        return bookmarkedArticlesDao.bookmarkArticle(article).subscribeOn(Schedulers.io());
    }

    @Override
    public Completable unBookmarkArticle(ArticleWithBody article) {
        return bookmarkedArticlesDao.unBookmarkArticle(article).subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<List<? extends ArticleWithBody>> getBookmarks() {
        return bookmarkedArticlesDao.getBookmarked()
                .flatMap((articleWithBodies -> Observable.fromIterable(articleWithBodies)
                        .toList()
                        .toObservable().subscribeOn(Schedulers.io())));
    }

    @Override
    public Observable<List<ArticleWithBody>> search(String keyword) {
        return bookmarkedArticlesDao.search("%" + keyword + "%")
                .subscribeOn(Schedulers.io());
    }

    @Override
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

}
