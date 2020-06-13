package dev.ahmdaeyz.guardianscope.data.repository;

import org.threeten.bp.LocalDateTime;

import java.util.List;

import dev.ahmdaeyz.guardianscope.data.model.theguardian.Article;
import dev.ahmdaeyz.guardianscope.data.model.theguardian.ArticleWithBody;
import dev.ahmdaeyz.guardianscope.data.model.theguardian.BookmarkedArticle;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface ArticlesRepository {
    Observable<List<Article>> getTrendingArticles();

    Observable<List<Article>> getSectionsArticles(List<String> sections);

    Completable bookMarkArticle(Article article);

    Observable<List<BookmarkedArticle>> getBookmarks();

    Observable<List<Article>> search(String keyword);

    LocalDateTime getLastTimeUpdated();

    Single<ArticleWithBody> getArticle(String apiUrl);
}
