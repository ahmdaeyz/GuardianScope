package dev.ahmdaeyz.guardianscope.data.repository;

import java.util.List;

import dev.ahmdaeyz.guardianscope.data.model.theguardian.Article;
import io.reactivex.Completable;
import io.reactivex.Observable;

public interface ArticlesRepository {
    Observable<List<Article>> getTrendingArticles();

    Observable<List<Article>> getSectionsArticles(List<String> sections);

    Completable bookMarkArticle(Article article);

    Observable<List<Article>> getBookmarks();

    Completable favouriteArticle(Article article);

    Observable<List<Article>> getFavourites();

    Observable<List<Article>> search(String keyword);
}
