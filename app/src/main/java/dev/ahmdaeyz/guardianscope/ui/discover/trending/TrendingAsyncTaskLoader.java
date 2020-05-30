package dev.ahmdaeyz.guardianscope.ui.discover.trending;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.io.IOException;
import java.util.List;

import dev.ahmdaeyz.guardianscope.data.entities.Article;
import dev.ahmdaeyz.guardianscope.data.network.NetworkService;

public class TrendingAsyncTaskLoader extends AsyncTaskLoader<List<Article>> {

    private final NetworkService service;

    public TrendingAsyncTaskLoader(@NonNull Context context, NetworkService service) {
        super(context);
        this.service = service;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public List<Article> loadInBackground() {
        List<Article> articles = null;
        try {
            articles = service.getHeadlineArticles();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return articles;
    }


}
