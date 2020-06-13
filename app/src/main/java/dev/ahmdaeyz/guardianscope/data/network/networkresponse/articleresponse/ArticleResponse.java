package dev.ahmdaeyz.guardianscope.data.network.networkresponse.articleresponse;

import com.google.gson.annotations.SerializedName;

public class ArticleResponse {

    @SerializedName("response")
    private Response response;

    public Response getResponse() {
        return response;
    }

}