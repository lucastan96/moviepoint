package com.fuego.moviepoint.Fragment;

import android.os.Bundle;
import android.widget.Toast;

import com.fuego.moviepoint.MovieViewModel;
import com.fuego.moviepoint.R;

import androidx.lifecycle.ViewModelProviders;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {
    private static final String TAG = SettingsFragment.class.getSimpleName();

    Preference clearCache;
    private MovieViewModel movieViewModel;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences_settings);

        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);

        clearCache = findPreference("deleteAll");

        clearCache.setOnPreferenceClickListener(preference -> {
            movieViewModel.deleteAllNotes();
            Toast.makeText(getActivity(), "Cleared all Movies", Toast.LENGTH_SHORT).show();
            return true;
        });

    }


}
