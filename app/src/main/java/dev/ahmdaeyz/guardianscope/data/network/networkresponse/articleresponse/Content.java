package dev.ahmdaeyz.guardianscope.data.network.networkresponse.articleresponse;

import com.google.gson.annotations.SerializedName;

public class Content {

    @SerializedName("sectionName")
    private String sectionName;

    @SerializedName("webPublicationDate")
    private String webPublicationDate;

    @SerializedName("apiUrl")
    private String apiUrl;

    @SerializedName("webUrl")
    private String webUrl;

    @SerializedName("webTitle")
    private String webTitle;

    @SerializedName("id")
    private String id;

    @SerializedName("fields")
    private Fields fields;

    public String getSectionName() {
        return sectionName;
    }

    public String getWebPublicationDate() {
        return webPublicationDate;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getWebTitle() {
        return webTitle;
    }

    public String getId() {
        return id;
    }

    public Fields getFields() {
        return fields;
    }

}