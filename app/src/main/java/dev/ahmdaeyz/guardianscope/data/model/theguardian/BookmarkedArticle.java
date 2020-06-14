package dev.ahmdaeyz.guardianscope.data.model.theguardian;

public class BookmarkedArticle extends ArticleWithBody {

    public BookmarkedArticle(String id, String sectionName, String webPublicationDate, String webTitle, String webUrl, String apiUrl, String headline, String author, Integer wordCount, String thumbnail, Boolean isLive, String body) {
        super(id, sectionName, webPublicationDate, webTitle, webUrl, apiUrl, headline, author, wordCount, thumbnail, isLive, body);
    }
}
