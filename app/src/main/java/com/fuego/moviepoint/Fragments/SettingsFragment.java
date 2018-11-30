package com.fuego.moviepoint.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.fuego.moviepoint.Activities.AboutActivity;
import com.fuego.moviepoint.Activities.MainActivity;
import com.fuego.moviepoint.Movies.MovieViewModel;
import com.fuego.moviepoint.R;

import androidx.lifecycle.ViewModelProviders;
import androidx.preference.CheckBoxPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {

    private MovieViewModel movieViewModel;
    private ListPreference region;
    private androidx.preference.CheckBoxPreference notifications;
    private SharedPreferences mPrefs;
    private String defaultValue;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences_settings);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        defaultValue = getResources().getString(R.string.region_default);
        String savedRegion = mPrefs.getString(getString(R.string.region), defaultValue);

        Preference clearCache = findPreference("deleteAll");
        Preference aboutActivity = findPreference("about");

        notifications = (CheckBoxPreference) findPreference("notifications");
        notifications.setDefaultValue(true);
        notifications.setOnPreferenceChangeListener((preference, newValue) -> {
            mPrefs.edit()
                    .putBoolean(getString(R.string.notifications), Boolean.valueOf(newValue.toString()))
                    .apply();
            return true;
        });

        clearCache.setOnPreferenceClickListener(preference -> {
            movieViewModel.deleteAllMovies();
            Toast.makeText(getActivity(), "Cleared all cached data", Toast.LENGTH_SHORT).show();
            return true;
        });

        region = (ListPreference) findPreference("region");
        region.setSummary(savedRegion);
        region.setDefaultValue(1);
        region.setOnPreferenceChangeListener((preference, newRegion) -> {
            mPrefs.edit()
                    .putString(getString(R.string.region), newRegion.toString())
                    .apply();
            region.setSummary(mPrefs.getString(getString(R.string.region), defaultValue));
            MainActivity.main.finish();
            return true;
        });

        aboutActivity.setOnPreferenceClickListener(preference -> {
            Intent intent = new Intent(getActivity(), AboutActivity.class);
            startActivity(intent);
            return true;
        });
    }
}
