package com.fuego.moviepoint.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.fuego.moviepoint.Activities.AboutActivity;
import com.fuego.moviepoint.Movies.MovieViewModel;
import com.fuego.moviepoint.R;

import androidx.lifecycle.ViewModelProviders;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {

    private static final String TAG = SettingsFragment.class.getSimpleName();
    private MovieViewModel movieViewModel;
    private Preference clearCache, aboutActivity;
    private ListPreference region;
    private SharedPreferences mPrefs;
    private String savedRegion, defaultValue;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences_settings);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        defaultValue = getResources().getString(R.string.default_region);

        clearCache = findPreference("deleteAll");
        region = (ListPreference) findPreference("region");
        aboutActivity = findPreference("about");

        savedRegion = mPrefs.getString(getString(R.string.region), defaultValue);
        region.setSummary(savedRegion);

        clearCache.setOnPreferenceClickListener(preference -> {
            movieViewModel.deleteAllMovies();
            Toast.makeText(getActivity(), "Cleared all Movies", Toast.LENGTH_SHORT).show();
            return true;
        });

        region.setOnPreferenceChangeListener((preference, newRegion) -> {
            mPrefs.edit()
                    .putString(getString(R.string.region), newRegion.toString())
                    .apply();
            region.setSummary(mPrefs.getString(getString(R.string.region), defaultValue));
            return true;
        });

        aboutActivity.setOnPreferenceClickListener(preference -> {
            Intent intent = new Intent(getActivity(), AboutActivity.class);
            startActivity(intent);
            return true;
        });

    }


}
