package dev.ahmdaeyz.guardianscope.data.network.networkresponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dev.ahmdaeyz.guardianscope.data.entities.Article;
import dev.ahmdaeyz.guardianscope.data.entities.Fields;

public class JSONParser {
    public JSONParser() {

    }

    public List<Article> parse(String json) throws JSONException {
        List<Article> articles = new ArrayList<>();
        if (!json.isEmpty()) {
            JSONObject jsonObject = new JSONObject(json);
            if (!jsonObject.isNull("response")) {
                JSONObject response = jsonObject.getJSONObject("response");
                JSONArray results = response.getJSONArray("results");
                if (results.length() > 0) {
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject singleItem = results.getJSONObject(i);
                        String id, sectionName, webPublicationDate, webTitle, webUrl;
                        Fields fields;
                        id = singleItem.getString("id");
                        sectionName = singleItem.getString("sectionName");
                        webPublicationDate = singleItem.getString("webPublicationDate");
                        webTitle = singleItem.getString("webTitle");
                        webUrl = singleItem.getString("webUrl");
                        fields = parseFieldsFrom(singleItem.getJSONObject("fields"));
                        articles.add(
                                new Article(id, sectionName, webPublicationDate, webTitle, webUrl, fields)
                        );
                    }
                } else {
                    return articles;
                }
            } else {
                throw new JSONException("empty json response");
            }
        } else {
            throw new JSONException("empty json response");
        }
        return articles;
    }

    private Fields parseFieldsFrom(JSONObject fields) throws JSONException {
        String headline, author, body, thumbnail;
        Integer wordCount;
        boolean isLive;
        headline = fields.optString("headline");
        author = fields.optString("byline");
        body = fields.optString("body");
        thumbnail = fields.optString("thumbnail");
        wordCount = fields.optInt("wordcount", 0);
        isLive = fields.optBoolean("isLive", false);
        return new Fields(headline, author, body, wordCount, thumbnail, isLive);
    }
}
