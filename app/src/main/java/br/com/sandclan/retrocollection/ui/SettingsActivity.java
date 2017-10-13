package br.com.sandclan.retrocollection.ui;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.View;

import br.com.sandclan.retrocollection.R;
import br.com.sandclan.retrocollection.databasegame.GameDBUtil;


public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.general);
    }

    /**
     * Attaches a listener so the summary is always updated with the preference value.
     * Also fires the listener once, to initialize the summary (so it shows up before the value
     * is changed.)
     */
    private void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(this);

        // Trigger the listener immediately with the preference's
        // current value.
        onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    public void importGamesFromServer(View view){
        GameDBUtil gameDBUtil = new GameDBUtil(this);
        gameDBUtil.getGamesFromServer();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
//        String stringValue = value.toString();
//
//        if (preference instanceof ListPreference) {
//            ListPreference listPreference = (ListPreference) preference;
//            int prefIndex = listPreference.findIndexOfValue(stringValue);
//            if (prefIndex >= 0) {
//                preference.setSummary(listPreference.getEntries()[prefIndex]);
//            }
//        } else {
//            preference.setSummary(stringValue);
//        }
        return true;
    }


}