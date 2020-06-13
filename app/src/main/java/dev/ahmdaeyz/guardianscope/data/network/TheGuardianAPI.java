package dev.ahmdaeyz.guardianscope.data.network;


import dev.ahmdaeyz.guardianscope.data.network.networkresponse.articleresponse.ArticleResponse;
import dev.ahmdaeyz.guardianscope.data.network.networkresponse.articlesresponse.ArticlesResponse;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

interface TheGuardianAPI {
    @GET("search")
    Single<ArticlesResponse> getHeadlineArticles(@Query("tags") String tags, @Query("page-size") int pageSize, @Query("show-fields") String fieldsToShow);

    @GET("search")
    Single<ArticlesResponse> getSectionsArticles(@Query("section") String sections, @Query("page-size") int pageSize, @Query("show-fields") String fieldsToShow);

    @GET
    Single<ArticleResponse> getArticle(@Url String apiUrl, @Query("show-fields") String fieldsToShow);
}
