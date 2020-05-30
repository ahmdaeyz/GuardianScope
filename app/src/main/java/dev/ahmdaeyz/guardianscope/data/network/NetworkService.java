package dev.ahmdaeyz.guardianscope.data.network;

import android.net.Uri;

import org.threeten.bp.Instant;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.format.DateTimeFormatter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import dev.ahmdaeyz.guardianscope.data.entities.Article;
import dev.ahmdaeyz.guardianscope.data.network.networkresponse.JSONParser;

public class NetworkService {
    private final String PAGE_SIZE = "50";
    private final String API_KEY = "abbbdc43-bfd3-498c-afbf-41e9f87bbbb8";
    private final String BASE_URL = "https://content.guardianapis.com/search";

    public NetworkService() {

    }

    public List<Article> getSectionsArticles(String... sections) throws IOException {
        URL queryURL = querySections(sections);
        return getArticles(queryURL);
    }

    public List<Article> getHeadlineArticles() throws IOException {
        URL queryURL = queryHeadlines();
        return getArticles(queryURL);
    }

    private List<Article> getArticles(URL url) throws IOException {
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        List<Article> articles = new ArrayList<>();
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(30000);
            urlConnection.connect();
            if (urlConnection.getResponseCode() != 200) {
                throw new Exception("Couldn't get Articles with" + url.toString());
            } else {
                inputStream = urlConnection.getInputStream();
                JSONParser parser = new JSONParser();
                String textResponse = stringifyInputStream(inputStream);
                articles = parser.parse(textResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return articles;
    }

    private URL querySections(String... sections) {
        Uri baseUri = getUriWithBaseQueries();
        URL url = null;
        try {
            url = new URL(
                    baseUri
                            .buildUpon().appendQueryParameter(
                            "section",
                            Arrays.stream(sections).collect(Collectors.joining("|"))
                    ).build().toString()
            );
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private URL queryHeadlines() {
        Uri baseUri = getUriWithBaseQueries();
        URL url = null;
        try {
            url = new URL(
                    baseUri
                            .buildUpon()
                            .appendQueryParameter("tags", "headline")
                            .build()
                            .toString()
            );
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private Uri getUriWithBaseQueries() {
        Uri uriToGet = Uri.parse(BASE_URL);
        ;
        uriToGet = uriToGet.buildUpon()
                .appendQueryParameter("type", "article|liveblog")
                .appendQueryParameter("from-date", prepareDate()[0])
                .appendQueryParameter("to-date", prepareDate()[1])
                .appendQueryParameter("show-fields", "all")
                .appendQueryParameter("page-size", PAGE_SIZE)
                .appendQueryParameter("api-key", API_KEY).build();
        return uriToGet;
    }

    private String[] prepareDate() {
        Instant today = Instant.now();
        Instant aDayAgo = today.minusSeconds(60 * 60 * 24);
        LocalDateTime dateToday = LocalDateTime.ofInstant(today, ZoneId.of("UTC"));
        LocalDateTime dateYesterday = LocalDateTime.ofInstant(aDayAgo, ZoneId.of("UTC"));
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return new String[]{dateTimeFormatter.format(dateYesterday), dateTimeFormatter.format(dateToday)};
    }

    private String stringifyInputStream(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line);
                line = bufferedReader.readLine();
            }
        }
        return stringBuilder.toString();
    }

}
