package dev.ahmdaeyz.guardianscope.data.model.theguardian;


import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel
public class Fields {
    private String headline;
    private String author;
    private Integer wordCount;
    private String thumbnail;
    private Boolean isLive;

    @ParcelConstructor
    public Fields(String headline, String author, Integer wordCount, String thumbnail, Boolean isLive) {
        this.headline = headline;
        this.author = author;
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
        return "{headline: " + headline +
                " , author: " + author +
                " , wordCount: " + wordCount +
                " , thumbnail: " + thumbnail +
                " , isLive: " + isLive + "}";
    }
}
