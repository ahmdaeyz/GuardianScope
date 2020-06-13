package dev.ahmdaeyz.guardianscope.data.network.networkresponse.articleresponse;

import com.google.gson.annotations.SerializedName;

public class Response {

    @SerializedName("content")
    private Content content;

    @SerializedName("status")
    private String status;

    public Content getContent() {
        return content;
    }

    public String getStatus() {
        return status;
    }

}