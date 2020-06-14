package dev.ahmdaeyz.guardianscope.data.model.theguardian;

import androidx.room.Entity;

@Entity(tableName = "bookmarked_articles")
public class ArticleWithBody extends Article implements Bookmarkable {

    private String body;
    private boolean isBookmarked;

    public ArticleWithBody(String id, String sectionName, String webPublicationDate, String webTitle, String webUrl, String apiUrl, String headline, String author, Integer wordCount, String thumbnail, Boolean isLive, String body) {
        super(id,
                sectionName,
                webPublicationDate,
                webTitle,
                webUrl,
                apiUrl,
                headline,
                author,
                wordCount,
                thumbnail,
                isLive);
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    @Override
    public Article bookmark() {
        return new BookmarkedArticle(getId(),
                getSectionName(),
                getWebPublicationDate(),
                getWebTitle(),
                getWebUrl(),
                getApiUrl(),
                getHeadline(),
                getAuthor(),
                getWordCount(),
                getThumbnail(),
                getIsLive(),
                getBody());
    }

    @Override
    public Article unBookmark() {
        return this;
    }

    public boolean isBookmarked() {
        return isBookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        isBookmarked = bookmarked;
    }
}
