package dev.ahmdaeyz.guardianscope.data.model.theguardian;

public class BookmarkedArticle extends ArticleWithBody {
    public BookmarkedArticle(String id, String sectionName, String webPublicationDate, String webTitle, String webUrl, String apiUrl, FieldsWithBody fields) {
        super(id, sectionName, webPublicationDate, webTitle, webUrl, apiUrl, fields);
    }

    @Override
    public Article bookmark() {
        return this;
    }

    @Override
    public Article unBookmark() {
        return new Article(getId(),
                getSectionName(),
                getWebPublicationDate(),
                getWebTitle(),
                getWebUrl(),
                getApiUrl(),
                getFields());
    }

}
