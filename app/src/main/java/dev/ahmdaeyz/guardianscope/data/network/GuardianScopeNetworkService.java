package dev.ahmdaeyz.guardianscope.data.network;

import java.util.List;
import java.util.stream.Collectors;

import dev.ahmdaeyz.guardianscope.data.model.theguardian.Article;
import dev.ahmdaeyz.guardianscope.data.model.theguardian.ArticleWithBody;
import dev.ahmdaeyz.guardianscope.data.network.interceptors.CacheInterceptor;
import dev.ahmdaeyz.guardianscope.data.network.networkresponse.articleresponse.Content;
import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.Interceptor;

import static dev.ahmdaeyz.guardianscope.util.date.DateMethods.dateToFromEpoch;

public class GuardianScopeNetworkService implements NetworkService {
    private TheGuardianAPIBuilder theGuardianAPIBuilder;
    private TheGuardianAPI networkAPI;

    public GuardianScopeNetworkService(Interceptor connectivityInterceptor, CacheInterceptor cacheInterceptor) {
        this.theGuardianAPIBuilder = new TheGuardianAPIBuilder(connectivityInterceptor, cacheInterceptor);
        this.networkAPI = theGuardianAPIBuilder.create(TheGuardianAPI.class);
    }

    @Override
    public Single<List<Article>> getHeadlineArticles() {
        return networkAPI.getHeadlineArticles("headline",
                20,
                "headline,thumbnail,byline,wordcount")
                .flatMap((articlesResponse) -> Observable.fromIterable(articlesResponse.getResponse().getResults())
                        .map((resultsItem -> new Article(resultsItem.getId(),
                                resultsItem.getSectionName(),
                                dateToFromEpoch(resultsItem.getWebPublicationDate()),
                                resultsItem.getWebTitle(),
                                resultsItem.getWebUrl(),
                                resultsItem.getApiUrl(),
                                resultsItem.getFields().getHeadline(),
                                resultsItem.getFields().getByline(),
                                resultsItem.getFields().getWordcount(),
                                resultsItem.getFields().getThumbnail(),
                                true
                        ))).toList());

    }

    @Override
    public Single<List<Article>> getSectionsArticles(List<String> sections) {
        return networkAPI.getSectionsArticles(
                sections.stream().collect(Collectors.joining("|")),
                30,
                "headline,thumbnail,byline,wordcount"
        )
                .flatMap((articlesResponse) -> Observable.fromIterable(articlesResponse.getResponse().getResults())
                        .map((resultsItem -> new Article(resultsItem.getId(),
                                resultsItem.getSectionName(),
                                dateToFromEpoch(resultsItem.getWebPublicationDate()),
                                resultsItem.getWebTitle(),
                                resultsItem.getWebUrl(),
                                resultsItem.getApiUrl(),
                                resultsItem.getFields().getHeadline(),
                                resultsItem.getFields().getByline(),
                                resultsItem.getFields().getWordcount(),
                                resultsItem.getFields().getThumbnail(),
                                true
                        ))).toList());
    }

    @Override
    public Single<ArticleWithBody> getArticle(String apiUrl) {
        return networkAPI.getArticle(
                apiUrl,
                "headline,thumbnail,byline,wordcount,body"
        ).flatMap((articleResponse) -> {
            Content articleContent = articleResponse.getResponse().getContent();
            return Single.just(new ArticleWithBody(
                    articleContent.getId(),
                    articleContent.getSectionName(),
                    dateToFromEpoch(articleContent.getWebPublicationDate()),
                    articleContent.getWebTitle(),
                    articleContent.getWebUrl(),
                    articleContent.getApiUrl(),
                    articleContent.getFields().getHeadline(),
                    articleContent.getFields().getByline(),
                    articleContent.getFields().getWordcount(),
                    articleContent.getFields().getThumbnail(),
                    articleContent.getFields().getIsLive(),
                    articleContent.getFields().getBody()
            ));
        });

    }
}
