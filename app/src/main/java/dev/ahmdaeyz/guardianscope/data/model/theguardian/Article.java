package dev.ahmdaeyz.guardianscope.data.model.theguardian;

import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel
public class Article {
    private String id;
    private String sectionName;
    private long webPublicationDate;
    private String webTitle;
    private String webUrl;
    @PrimaryKey
    @NonNull
    private String apiUrl;
    private String headline;
    private String author;
    private int wordCount;
    private String thumbnail;
    private Boolean isLive;

    @ParcelConstructor
    public Article(String id, String sectionName, long webPublicationDate, String webTitle, String webUrl, String apiUrl, String headline, String author, Integer wordCount, String thumbnail, Boolean isLive) {
        this.id = id;
        this.sectionName = sectionName;
        this.webPublicationDate = webPublicationDate;
        this.webTitle = webTitle;
        this.webUrl = webUrl;
        this.apiUrl = apiUrl;
        this.headline = headline;
        this.author = author;
        this.wordCount = wordCount;
        this.thumbnail = thumbnail;
        this.isLive = isLive;
    }

    public String getId() {
        return id;
    }

    public String getSectionName() {
        return sectionName;
    }

    public long getWebPublicationDate() {
        return webPublicationDate;
    }

    public String getWebTitle() {
        return webTitle;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getApiUrl() {
        return apiUrl;
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

}
