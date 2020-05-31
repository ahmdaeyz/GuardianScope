package dev.ahmdaeyz.guardianscope.data.network;

import java.util.List;

import dev.ahmdaeyz.guardianscope.data.model.theguardian.Article;
import io.reactivex.Single;

public interface NetworkService {
    Single<List<Article>> getHeadlineArticles();

    Single<List<Article>> getSectionsArticles(String... sections);
}
