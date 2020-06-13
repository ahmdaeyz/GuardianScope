package dev.ahmdaeyz.guardianscope.data.model.theguardian;

public class FieldsWithBody extends Fields {
    private final String body;

    public FieldsWithBody(String headline, String author, Integer wordCount, String thumbnail, Boolean isLive, String body) {
        super(headline, author, wordCount, thumbnail, isLive);
        this.body = body;
    }

    public String getBody() {
        return body;
    }
}
