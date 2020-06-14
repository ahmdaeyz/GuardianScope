package dev.ahmdaeyz.guardianscope.data.db.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import dev.ahmdaeyz.guardianscope.data.model.theguardian.ArticleWithBody;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

@Dao
public interface BookmarkedArticlesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable bookmarkArticle(ArticleWithBody article);

    @Delete
    Completable unBookmarkArticle(ArticleWithBody article);

    @Query("SELECT * FROM bookmarked_articles WHERE apiUrl = :apiUrl")
    Single<ArticleWithBody> getBookmarkedArticle(String apiUrl);

    @Query("SELECT * FROM bookmarked_articles")
    Observable<List<ArticleWithBody>> getBookmarked();


}
