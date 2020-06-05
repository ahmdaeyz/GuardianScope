package dev.ahmdaeyz.guardianscope.data.network;

import android.net.Uri;
import android.util.Log;

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
import java.util.List;
import java.util.stream.Collectors;

import dev.ahmdaeyz.guardianscope.data.model.theguardian.Article;
import dev.ahmdaeyz.guardianscope.data.network.networkresponse.JSONSerializer;
import io.reactivex.Single;
import io.reactivex.subjects.SingleSubject;

public class BasicNetworkService implements NetworkService {
    private final String PAGE_SIZE = "30";
    private final String API_KEY = "abbbdc43-bfd3-498c-afbf-41e9f87bbbb8";
    private final String BASE_URL = "https://content.guardianapis.com/search";

    public BasicNetworkService() {

    }

    public Single<List<Article>> getSectionsArticles(List<String> sections) {
        URL queryURL = querySections(sections, 30);
        return getArticles(queryURL);
    }

    public Single<List<Article>> getHeadlineArticles() {
        URL queryURL = queryHeadlines(20);
        return getArticles(queryURL);
    }

    private Single<List<Article>> getArticles(URL url) {
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        SingleSubject<List<Article>> articlesSubject = SingleSubject.create();
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(5000);
            urlConnection.setConnectTimeout(5000);
            urlConnection.connect();
            if (urlConnection.getResponseCode() != 200) {
                articlesSubject.onError(new Exception("Couldn't get Articles with" + url.toString()));
            } else {
                inputStream = urlConnection.getInputStream();
                JSONSerializer parser = new JSONSerializer();
                String textResponse = stringifyInputStream(inputStream);
                Log.d("NetworkResponse", textResponse);
                List<Article> _articles = parser.parse(textResponse);
                if (_articles != null) {
                    Log.d("Network", _articles.toString());
                    articlesSubject.onSuccess(_articles);
                } else {
                    articlesSubject.onError(new Exception("couldn't get response with url: " + url.toString()));
                }
            }
        } catch (Exception e) {
            articlesSubject.onError(e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return articlesSubject;
    }

    private URL querySections(List<String> sections, int pageSize) {
        Uri baseUri = getUriWithBaseQueries(pageSize);
        URL url = null;
        try {
            url = new URL(
                    baseUri
                            .buildUpon().appendQueryParameter(
                            "section",
                            sections.stream().collect(Collectors.joining("|"))
                    ).build().toString()
            );
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private URL queryHeadlines(int pageSize) {
        Uri baseUri = getUriWithBaseQueries(pageSize);
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

    private Uri getUriWithBaseQueries(int pageSize) {
        Uri uriToGet = Uri.parse(BASE_URL);
        uriToGet = uriToGet.buildUpon()
                .appendQueryParameter("type", "article|liveblog")
                .appendQueryParameter("from-date", prepareDate()[0])
                .appendQueryParameter("to-date", prepareDate()[1])
                .appendQueryParameter("show-fields", "all")
                .appendQueryParameter("page-size", String.valueOf(pageSize))
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
