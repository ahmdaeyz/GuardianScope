package dev.ahmdaeyz.guardianscope.data.model.theguardian;

public class ArticleWithBody extends Article implements Bookmarkable {
    private final FieldsWithBody fields;

    public ArticleWithBody(String id, String sectionName, String webPublicationDate, String webTitle, String webUrl, String apiUrl, FieldsWithBody fields) {
        super(id, sectionName, webPublicationDate, webTitle, webUrl, apiUrl, fields);
        this.fields = fields;
    }

    @Override
    public FieldsWithBody getFields() {
        return fields;
    }

    @Override
    public Article bookmark() {
        return new BookmarkedArticle(getId(), getSectionName(), getWebPublicationDate(), getWebTitle(), getWebUrl(), getApiUrl(), getFields());
    }

    @Override
    public Article unBookmark() {
        return this;
    }
}
