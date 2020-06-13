package dev.ahmdaeyz.guardianscope.data.model.theguardian;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel
public class Article {
    private String id;
    private String sectionName;
    private String webPublicationDate;
    private String webTitle;
    private String webUrl;
    private String apiUrl;
    private Fields fields;

    @ParcelConstructor
    public Article(String id, String sectionName, String webPublicationDate, String webTitle, String webUrl, String apiUrl, Fields fields) {
        this.id = id;
        this.sectionName = sectionName;
        this.webPublicationDate = webPublicationDate;
        this.webTitle = webTitle;
        this.webUrl = webUrl;
        this.fields = fields;
        this.apiUrl = apiUrl;
    }

    public String getId() {
        return id;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getWebPublicationDate() {
        return webPublicationDate;
    }

    public String getWebTitle() {
        return webTitle;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public Fields getFields() {
        return fields;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    @Override
    public String toString() {
        return "{id: " + id +
                " , sectionName: " + sectionName +
                " , webPublicationDate: " + webPublicationDate +
                " , webTitle: " + webTitle +
                " , webUrl: " + webUrl +
                " , Fields: " + fields.toString() + "}";
    }


}
