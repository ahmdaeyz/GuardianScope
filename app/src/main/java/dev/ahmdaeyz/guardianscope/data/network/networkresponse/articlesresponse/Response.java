package dev.ahmdaeyz.guardianscope.data.network.networkresponse.articlesresponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Response {

    @SerializedName("response")
    private Response response;

    @SerializedName("pages")
    private int pages;

    @SerializedName("currentPage")
    private int currentPage;

    @SerializedName("results")
    private List<ResultsItem> results;

    @SerializedName("status")
    private String status;

    public Response getResponse() {
        return response;
    }

    public int getPages() {
        return pages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public List<ResultsItem> getResults() {
        return results;
    }

    public String getStatus() {
        return status;
    }
}