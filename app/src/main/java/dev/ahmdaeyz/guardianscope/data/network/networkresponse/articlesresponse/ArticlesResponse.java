package dev.ahmdaeyz.guardianscope.data.network.networkresponse.articlesresponse;

import com.google.gson.annotations.SerializedName;


public class ArticlesResponse {
    @SerializedName("response")
    private dev.ahmdaeyz.guardianscope.data.network.networkresponse.articlesresponse.Response response;

    public Response getResponse() {
        return response;
    }

}