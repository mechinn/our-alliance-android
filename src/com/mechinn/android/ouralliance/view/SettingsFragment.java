package com.mechinn.android.ouralliance.view;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.text.TextUtils;

import java.util.List;

import com.mechinn.android.ouralliance.Database;
import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.Setup;
import com.mechinn.android.ouralliance.data.Competition;
import com.mechinn.android.ouralliance.data.Season;
import com.mechinn.android.ouralliance.data.source.CompetitionDataSource;
import com.mechinn.android.ouralliance.data.source.SeasonDataSource;
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
	Prefs prefs;
	SQLiteDatabase db;
	SeasonDataSource seasonData;
	CompetitionDataSource competitionData;
	ListPreference season;
	ListPreference comp;
	Preference resetDB;
	Preference changelog;
	Preference about;
	private Setup setup;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
		setup = new Setup(this.getActivity());
        prefs = new Prefs(this.getActivity());
        //builds list from DB
        db = new Database(this.getActivity()).getWritableDatabase();
        seasonData = new SeasonDataSource(this.getActivity());
        competitionData = new CompetitionDataSource(this.getActivity());
        season = (ListPreference) getPreferenceScreen().findPreference(this.getString(R.string.pref_season));
        season.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
            	long id = Long.parseLong((String)newValue);
    	        try {
					season.setSummary(seasonData.get(id).toString());
					comp.setEnabled(true);
				} catch (Exception e) {
					season.setSummary(getActivity().getString(R.string.pref_season_summary));
					comp.setEnabled(false);
				}
            	setCompList(id);
            	return true;
            }
        });
        comp = (ListPreference) getPreferenceScreen().findPreference(this.getString(R.string.pref_comp));
        comp.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
            	long id = Long.parseLong((String)newValue);
    	        try {
    	        	comp.setSummary(competitionData.get(id).toString());
				} catch (Exception e) {
    	        	comp.setSummary(getActivity().getString(R.string.pref_comp_summary));
				}
            	return true;
            }
        });
        setSeasonList();
		setCompList(0);
        
        resetDB = (Preference) getPreferenceScreen().findPreference(this.getString(R.string.pref_resetDB));
        resetDB.setOnPreferenceClickListener(new OnPreferenceClickListener() {
				public boolean onPreferenceClick(Preference preference) {
					setup.reset();
			        setSeasonList();
					setCompList(0);
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
	
	private void setSeasonList() {
		try {
			List<Season> seasons = seasonData.getAll();
			CharSequence[] seasonsViews = new CharSequence[seasons.size()];
			CharSequence[] seasonsIds = new CharSequence[seasons.size()];
			for(int i=0;i<seasons.size();++i) {
				seasonsViews[i] = seasons.get(i).toString();
				seasonsIds[i] = Long.toString(seasons.get(i).getId());
			}
	        season.setEntries(seasonsViews);
	        season.setEntryValues(seasonsIds);
	        try {
	        	season.setSummary(seasonData.get(prefs.getSeason()).toString());
	        	comp.setEnabled(true);
	        } catch (Exception e) {
				season.setSummary(getActivity().getString(R.string.pref_season_summary));
				comp.setEnabled(false);
	        }
		} catch (Exception e) {
	        season.setEntries(new CharSequence[0]);
	        season.setEntryValues(new CharSequence[0]);
			season.setSummary(getActivity().getString(R.string.pref_season_summary));
			comp.setEnabled(false);
		}
	}
	
	private void setCompList(long newSeason) {
		if(newSeason<1) {
			newSeason = prefs.getSeason();
		}
		try {  
			List<Competition> comps;
			try {
				comps = competitionData.getAll(competitionData.get(newSeason));
			} catch (NumberFormatException e2) {
				comps = competitionData.getAll();
			}
			CharSequence[] compsViews = new CharSequence[comps.size()];
			CharSequence[] compsIds = new CharSequence[comps.size()];
			for(int i=0;i<comps.size();++i) {
				compsViews[i] = comps.get(i).toString();
				compsIds[i] = Long.toString(comps.get(i).getId());
			}
	        comp.setEntries(compsViews);
	        comp.setEntryValues(compsIds);
	        try {
	        	comp.setSummary(competitionData.get(prefs.getComp()).toString());
	        } catch (Exception e) {
		        comp.setSummary(getActivity().getString(R.string.pref_comp_summary));
	        }
		} catch (Exception e) {
	        comp.setEntries(new CharSequence[]{"Select a season first."});
	        comp.setEntryValues(new CharSequence[]{"0"});
	        comp.setSummary(getActivity().getString(R.string.pref_comp_summary));
		}
	}
}
