package dev.ahmdaeyz.guardianscope.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.DropDownPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import dev.ahmdaeyz.guardianscope.R;
import dev.ahmdaeyz.guardianscope.ui.Preferences;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        SwitchPreferenceCompat darkMode = findPreference(getString(R.string.dark_mode_key));
        SwitchPreferenceCompat keepBookmarks = findPreference(getString(R.string.keep_bookmarks_key));
        DropDownPreference syncPeriod = findPreference(getString(R.string.sync_period_key));
        darkMode.setOnPreferenceChangeListener((preference, isDarkMode) -> {
            if ((Boolean) isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            return true;
        });
        keepBookmarks.setOnPreferenceChangeListener(((preference, newValue) -> {
            Preferences preferences = (Preferences) requireActivity();
            preferences.toggleDeletingBookmarks();
            return true;
        }));
        syncPeriod.setOnPreferenceChangeListener(((preference, newValue) -> {
            Preferences preferences = (Preferences) requireActivity();
            preferences.setSyncPeriod(Integer.parseInt((String) newValue));
            return true;
        }));
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}