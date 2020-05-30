package dev.ahmdaeyz.guardianscope.ui.discover.sections;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.io.IOException;
import java.util.List;

import dev.ahmdaeyz.guardianscope.data.entities.Article;
import dev.ahmdaeyz.guardianscope.data.network.NetworkService;

public class SectionsArticlesAsyncTaskLoader extends AsyncTaskLoader<List<Article>> {
    private final NetworkService service;
    String[] sections = {"education", "fashion", "food", "politics", "media", "environment"};

    public SectionsArticlesAsyncTaskLoader(@NonNull Context context, NetworkService service) {
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
            articles = service.getSectionsArticles(sections);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return articles;
    }
}
