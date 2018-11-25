package com.fuego.moviepoint.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.fuego.moviepoint.Activities.AboutActivity;
import com.fuego.moviepoint.MovieViewModel;
import com.fuego.moviepoint.R;

import androidx.lifecycle.ViewModelProviders;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {
    private static final String TAG = SettingsFragment.class.getSimpleName();

    Preference clearCache, aboutActivity;
    private MovieViewModel movieViewModel;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences_settings);

        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);

        clearCache = findPreference("deleteAll");
        aboutActivity = findPreference("about");

        clearCache.setOnPreferenceClickListener(preference -> {
            movieViewModel.deleteAllNotes();
            Toast.makeText(getActivity(), "Cleared all Movies", Toast.LENGTH_SHORT).show();
            return true;
        });

        aboutActivity.setOnPreferenceClickListener(preference -> {
            Intent intent = new Intent(getActivity(), AboutActivity.class);
            startActivity(intent);
            return true;
        });

    }


}
