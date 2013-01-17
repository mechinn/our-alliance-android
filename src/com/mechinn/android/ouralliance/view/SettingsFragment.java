package com.mechinn.android.ouralliance.view;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;

import java.util.List;

import com.mechinn.android.ouralliance.Database;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.SeasonDataSource;
import com.mechinn.android.ouralliance.TeamDataSource;
import com.mechinn.android.ouralliance.data.Season;
import com.mechinn.android.ouralliance.error.OurAllianceException;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on devdeloping a Settings UI.
 */
public class SettingsFragment extends PreferenceFragment {
	SharedPreferences prefs;
	SQLiteDatabase db;
	SeasonDataSource seasonData;
	ListPreference season;
	Preference resetDB;
	Preference changelog;
	Preference about;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        //builds list from DB
        db = new Database(this.getActivity()).getWritableDatabase();
        seasonData = new SeasonDataSource(this.getActivity());
        seasonData.open();
		try {
			CharSequence[] entries = seasonData.getAllSeasonsViews();
			CharSequence[] entryValues = seasonData.getAllSeasonsYears();
		       
	        season = (ListPreference) getPreferenceScreen().findPreference(this.getString(R.string.pref_season));
	        season.setEntries(entries);
	        season.setEntryValues(entryValues);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (OurAllianceException e) {
			e.printStackTrace();
		}
        
        resetDB = (Preference) getPreferenceScreen().findPreference(this.getString(R.string.pref_resetDB));
        resetDB.setOnPreferenceClickListener(new OnPreferenceClickListener() {
				public boolean onPreferenceClick(Preference preference) {
					// dialog code here
					return true;
				}
			});
        changelog = (Preference) getPreferenceScreen().findPreference(this.getString(R.string.pref_changeLog));
        changelog.setOnPreferenceClickListener(new OnPreferenceClickListener() {
				public boolean onPreferenceClick(Preference preference) {
					// dialog code here
					return true;
				}
			});
        about = (Preference) getPreferenceScreen().findPreference(this.getString(R.string.pref_about));
        about.setOnPreferenceClickListener(new OnPreferenceClickListener() {
				public boolean onPreferenceClick(Preference preference) {
					// dialog code here
					return true;
				}
			});
    }
}
