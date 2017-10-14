package br.com.sandclan.retrocollection.ui;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.View;

import br.com.sandclan.retrocollection.R;
import br.com.sandclan.retrocollection.databasegame.GameDBUtil;


public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.general);
    }


    public void importGamesFromServer(View view){
        GameDBUtil gameDBUtil = new GameDBUtil(this);
        gameDBUtil.getGamesFromServer();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        return true;
    }


}