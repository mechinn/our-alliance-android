package com.mechinn.android.ouralliance.view;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

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
public class SettingsFragment extends PreferenceFragment implements LoaderCallbacks<Cursor> {
	private static final String TAG = "SettingsFragment";
	private static final int LOADER_SEASON = 0;
	private static final int LOADER_COMPETITION = 1;
	private static final int LOADER_SEASON_SUMMARY = 2;
	private static final int LOADER_COMPETITION_SUMMARY = 3;
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
        //builds list from DB
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
            	SettingsFragment.this.getLoaderManager().restartLoader(LOADER_COMPETITION, null, SettingsFragment.this);
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
        resetDB = (Preference) getPreferenceScreen().findPreference(this.getString(R.string.pref_resetDB));
        resetDB.setOnPreferenceClickListener(new OnPreferenceClickListener() {
				public boolean onPreferenceClick(Preference preference) {
					//setup.reset();
	            	SettingsFragment.this.getLoaderManager().restartLoader(LOADER_SEASON, null, SettingsFragment.this);
	            	SettingsFragment.this.getLoaderManager().restartLoader(LOADER_COMPETITION, null, SettingsFragment.this);
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
        this.getLoaderManager().initLoader(LOADER_SEASON, null, this);
        this.getLoaderManager().initLoader(LOADER_COMPETITION, null, this);
    }

	public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
		long seasonID = Long.parseLong(season.getValue());
    	Log.d(TAG,"season: "+seasonID);
		long compID = Long.parseLong(season.getValue());
     	Log.d(TAG,"season: "+compID);
		switch(id) {
			case LOADER_SEASON:
				return seasonData.getAll();
			case LOADER_COMPETITION:
				return competitionData.getAllCompetitions(seasonID);
			case LOADER_SEASON_SUMMARY:
				return seasonData.get(seasonID);
			case LOADER_COMPETITION_SUMMARY:
				return competitionData.get(compID);
			default:
				return null;
		}
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		switch(loader.getId()) {
			case LOADER_SEASON:
				setSeasonList(cursor);
			case LOADER_COMPETITION:
				setCompList(cursor);
			case LOADER_SEASON_SUMMARY:
				setSeasonSummary(cursor);
			case LOADER_COMPETITION_SUMMARY:
				setCompSummary(cursor);
		}
	}

	public void onLoaderReset(Loader<Cursor> loader) {
		switch(loader.getId()) {
			case LOADER_SEASON:
				setSeasonList(null);
			case LOADER_COMPETITION:
				setSeasonList(null);
			case LOADER_SEASON_SUMMARY:
				setSeasonSummary(null);
			case LOADER_COMPETITION_SUMMARY:
				setCompSummary(null);
		}
	}
	
	private void setSeasonList(Cursor cursor) {
		try {
			List<Season> seasons = SeasonDataSource.getSeasons(cursor);
			CharSequence[] seasonsViews = new CharSequence[seasons.size()];
			CharSequence[] seasonsIds = new CharSequence[seasons.size()];
			for(int i=0;i<seasons.size();++i) {
				seasonsViews[i] = seasons.get(i).toString();
				seasonsIds[i] = Long.toString(seasons.get(i).getId());
			}
	        season.setEntries(seasonsViews);
	        season.setEntryValues(seasonsIds);
        	this.getLoaderManager().restartLoader(LOADER_SEASON_SUMMARY, null, this);
		} catch (Exception e) {
			e.printStackTrace();
	        season.setEntries(new CharSequence[]{"Loading"});
	        season.setEntryValues(new CharSequence[]{"0"});
			season.setSummary(getActivity().getString(R.string.pref_season_summary));
			comp.setEnabled(false);
		}
	}
	
	private void setCompList(Cursor cursor) {
		try {  
			List<Competition> comps = CompetitionDataSource.getCompetitions(cursor);
			CharSequence[] compsViews = new CharSequence[comps.size()];
			CharSequence[] compsIds = new CharSequence[comps.size()];
			for(int i=0;i<comps.size();++i) {
				compsViews[i] = comps.get(i).toString();
				compsIds[i] = Long.toString(comps.get(i).getId());
			}
	        comp.setEntries(compsViews);
	        comp.setEntryValues(compsIds);
        	this.getLoaderManager().restartLoader(LOADER_COMPETITION_SUMMARY, null, this);
		} catch (Exception e) {
			e.printStackTrace();
			comp.setEntries(new CharSequence[]{"Loading"});
	        comp.setEntryValues(new CharSequence[]{"0"});
	        comp.setSummary(getActivity().getString(R.string.pref_comp_summary));
		}
	}
	
	private void setSeasonSummary(Cursor cursor) {
		try {
			Season thisSeason = SeasonDataSource.getSeason(cursor);
        	season.setSummary(thisSeason.toString());
        	comp.setEnabled(true);
        } catch (Exception e) {
        	e.printStackTrace();
			season.setSummary(getActivity().getString(R.string.pref_season_summary));
			comp.setEnabled(false);
        }
	}
	
	private void setCompSummary(Cursor cursor) {
		try {
        	Competition thisComp = CompetitionDataSource.getCompetition(cursor);
        	comp.setSummary(thisComp.toString());
        } catch (Exception e) {
        	e.printStackTrace();
	        comp.setSummary(getActivity().getString(R.string.pref_comp_summary));
        }
	}
}
