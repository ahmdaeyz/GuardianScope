package dev.ahmdaeyz.guardianscope.data.model.theguardian;

public class Fields {
    private String headline;
    private String author;
    private String body;
    private Integer wordCount;
    private String thumbnail;
    private Boolean isLive;

    public Fields(String headline, String author, String body, Integer wordCount, String thumbnail, Boolean isLive) {
        this.headline = headline;
        this.author = author;
        this.body = body;
        this.wordCount = wordCount;
        this.thumbnail = thumbnail;
        this.isLive = isLive;
    }

    public String getHeadline() {
        return headline;
    }

    public String getAuthor() {
        return author;
    }

    public String getBody() {
        return body;
    }

    public int getWordCount() {
        return wordCount;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public Boolean getIsLive() {
        return isLive;
    }

    @Override
    public String toString() {
        return "{headline: "+headline+
                " , author: "+author+
                " , body: "+ body.isEmpty()+
                " , wordCount: "+wordCount+
                " , thumbnail: "+thumbnail+
                " , isLive: "+isLive+"}";
    }
}
