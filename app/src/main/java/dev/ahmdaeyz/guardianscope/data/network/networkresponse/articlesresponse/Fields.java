package dev.ahmdaeyz.guardianscope.data.network.networkresponse.articlesresponse;

import com.google.gson.annotations.SerializedName;

public class Fields {

    @SerializedName("wordcount")
    private int wordcount;

    @SerializedName("body")
    private String body;

    @SerializedName("isLive")
    private boolean isLive;

    @SerializedName("headline")
    private String headline;

    @SerializedName("byline")
    private String byline;

    @SerializedName("thumbnail")
    private String thumbnail;

    public int getWordcount() {
        return wordcount;
    }

    public String getBody() {
        return body;
    }

    public boolean getIsLive() {
        return isLive;
    }

    public String getHeadline() {
        return headline;
    }

    public String getByline() {
        return byline;
    }

    public String getThumbnail() {
        return thumbnail;
    }

}