package dev.ahmdaeyz.guardianscope.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

import dev.ahmdaeyz.guardianscope.R;
import dev.ahmdaeyz.guardianscope.data.repository.ArticlesRepositoryImpl;
import dev.ahmdaeyz.guardianscope.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements Preferences {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        if (isNightModePreferred()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        if (!shouldKeepBookmarks()) {
            toggleDeletingBookmarks();
        }
        setSyncPeriod(getPreferredSyncPeriod());
    }

    public void updateStatusBarColor(int colorResId) {// Color must be in hexadecimal fromat
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(colorResId));
    }

    private boolean isNightModePreferred() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getBoolean(getString(R.string.dark_mode_key), false);
    }

    private boolean shouldKeepBookmarks() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getBoolean(getString(R.string.keep_bookmarks_key), true);
    }

    private int getPreferredSyncPeriod() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return Integer.parseInt(sharedPreferences.getString(getString(R.string.sync_period_key), "30"));
    }

    @Override
    public void setSyncPeriod(int period) {
        ArticlesRepositoryImpl repository = ArticlesRepositoryImpl.getInstance();
        repository.setSyncPeriod(period);
    }

    @Override
    public void toggleDeletingBookmarks() {
        ArticlesRepositoryImpl repository = ArticlesRepositoryImpl.getInstance();
        repository.toggleKeepBookmarks();
    }
}
