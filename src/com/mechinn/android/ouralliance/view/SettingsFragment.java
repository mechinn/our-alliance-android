package com.mechinn.android.ouralliance.view;

import android.app.DialogFragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.DialogInterface;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.List;

import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.Setup;
import com.mechinn.android.ouralliance.data.Competition;
import com.mechinn.android.ouralliance.data.CompetitionTeam;
import com.mechinn.android.ouralliance.data.Season;
import com.mechinn.android.ouralliance.data.frc2013.TeamScouting2013;
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
public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener, LoaderCallbacks<Cursor>, GenericDialogFragment.Listener {
	public static final String TAG = SettingsFragment.class.getSimpleName();
	public static final int LOADER_SEASON = 0;
	public static final int LOADER_COMPETITION = 1;
	public static final int LOADER_SEASON_SUMMARY = 2;
	public static final int LOADER_COMPETITION_SUMMARY = 3;
	public static final int DIALOG_RESET = 0;
	public static final int DIALOG_DELETECOMP = 1;
	private Prefs prefs;
	private SeasonDataSource seasonData;
	private CompetitionDataSource competitionData;
	private String seasonPrefString;
	private String compPrefString;
	private String measurePrefString;
	private String resetDBPrefString;
	private ListPreference season;
	private ListPreference comp;
	private ListPreference measure;
	private Preference resetDB;
	private Preference changelog;
	private Preference about;
	private Cursor seasonCursor;
	private Cursor compCursor;
	private Cursor seasonSummCursor;
	private Cursor compSummCursor;
	private Season selectedSeason;
	private Competition selectedComp;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        prefs = new Prefs(this.getActivity());
        seasonData = new SeasonDataSource(this.getActivity());
        competitionData = new CompetitionDataSource(this.getActivity());
        seasonPrefString = this.getString(R.string.pref_season);
        compPrefString = this.getString(R.string.pref_comp);
        measurePrefString = this.getString(R.string.pref_measure);
        resetDBPrefString = this.getString(R.string.pref_resetDB);
        season = (ListPreference) getPreferenceScreen().findPreference(seasonPrefString);
        comp = (ListPreference) getPreferenceScreen().findPreference(compPrefString);
        measure = (ListPreference) getPreferenceScreen().findPreference(measurePrefString);
		setMeasureSummary();
        resetDB = (Preference) getPreferenceScreen().findPreference(resetDBPrefString);
        resetDB.setOnPreferenceClickListener(new OnPreferenceClickListener() {
				public boolean onPreferenceClick(Preference preference) {
					Log.d(TAG, "resetDB");
					DialogFragment dialog = new GenericDialogFragment();
					Bundle dialogArgs = new Bundle();
					dialogArgs.putInt(GenericDialogFragment.FLAG, DIALOG_RESET);
					dialogArgs.putInt(GenericDialogFragment.MESSAGE, R.string.confirmReset);
					dialog.setArguments(dialogArgs);
		            dialog.show(SettingsFragment.this.getFragmentManager(), "Reset Data? This is not reversable!");
					return true;
				}
			});
        changelog = (Preference) getPreferenceScreen().findPreference(this.getString(R.string.pref_changeLog));
        changelog.setOnPreferenceClickListener(new OnPreferenceClickListener() {
				public boolean onPreferenceClick(Preference preference) {
					DialogFragment dialog = new HtmlDialogFragment();
		            Bundle htmlArgs = new Bundle();
		            htmlArgs.putString(HtmlDialogFragment.HTMLFILE, "file:///android_asset/changelog.html");
		            dialog.setArguments(htmlArgs);
		            dialog.show(SettingsFragment.this.getFragmentManager(), "Change Log");
		            Log.d(TAG, "count: "+SettingsFragment.this.getFragmentManager().getBackStackEntryCount());
					return true;
				}
			});
        about = (Preference) getPreferenceScreen().findPreference(this.getString(R.string.pref_about));
        about.setOnPreferenceClickListener(new OnPreferenceClickListener() {
				public boolean onPreferenceClick(Preference preference) {
					DialogFragment dialog = new HtmlDialogFragment();
		            Bundle htmlArgs = new Bundle();
		            htmlArgs.putString(HtmlDialogFragment.HTMLFILE, "file:///android_asset/about.html");
		            dialog.setArguments(htmlArgs);
		            dialog.show(SettingsFragment.this.getFragmentManager(), "Change Log");
					return true;
				}
			});
    }
	
	@Override
	public void onResume() {
	    super.onResume();
	    // Set up a listener whenever a key changes
        season.setValue(Long.toString(prefs.getSeason()));
        comp.setValue(Long.toString(prefs.getComp()));
	    prefs.setChangeListener(this);
        this.getLoaderManager().restartLoader(LOADER_SEASON, null, this);
    	this.getLoaderManager().restartLoader(LOADER_COMPETITION, null, this);
	}

	@Override
	public void onPause() {
	    super.onPause();
	    // Unregister the listener whenever a key changes
	    prefs.unsetChangeListener(this);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.settings, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.newComp:
	        	if(null!=selectedSeason) {
		            DialogFragment dialog = new InsertCompDialogFragment();
					Bundle dialogArgs = new Bundle();
					dialogArgs.putSerializable(InsertCompDialogFragment.SEASON_ARG, selectedSeason);
					dialog.setArguments(dialogArgs);
					dialog.show(this.getFragmentManager(), "Add Competition");
	        	} else {
	        		noSeason();
	        	}
	            return true;
	        case R.id.deleteComp:
	        	if(null!=selectedComp) {
					DialogFragment dialog = new GenericDialogFragment();
					Bundle dialogArgs = new Bundle();
					dialogArgs.putInt(GenericDialogFragment.FLAG, DIALOG_DELETECOMP);
					dialogArgs.putInt(GenericDialogFragment.MESSAGE, R.string.deleteComp);
					dialog.setArguments(dialogArgs);
		            dialog.show(SettingsFragment.this.getFragmentManager(), "Delete selected compeittion?");
	        	} else {
	        		noCompetition();
	        	}
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	public void onGenericDialogPositiveClick(int flag, DialogInterface dialog, int id) {
		switch(flag) {
			case DIALOG_RESET:
				resetData();
				break;
			case DIALOG_DELETECOMP:
				deleteCompetition();
				break;
		}
	}

	public void onGenericDialogNegativeClick(int flag, DialogInterface dialog, int id) {
		switch(flag) {
			default:
				dialog.dismiss();
		}
	}
	
	private void noSeason() {
		String message = "Select a season first";
		Log.i(TAG, message);
		Toast.makeText(this.getActivity(), message, Toast.LENGTH_SHORT).show();
	}
	
	private void noCompetition() {
		String message = "Select a competition first";
		Log.i(TAG, message);
		Toast.makeText(this.getActivity(), message, Toast.LENGTH_SHORT).show();
	}

	public void insertCompetition(Competition competition) {
		Log.d(TAG, "id: "+competition);
		//try inserting the team first in case it doesnt exist
		try {
			competition = competitionData.insert(competition);
			return;
		} catch (OurAllianceException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Toast.makeText(this.getActivity(), "Competition already exists", Toast.LENGTH_SHORT).show();
	}

	public void deleteCompetition() {
		Log.d(TAG, "id: "+selectedComp);
		//try inserting the team first in case it doesnt exist
		try {
			int count = competitionData.delete(selectedComp);
			Log.d(TAG, "deleted: "+count);
			return;
		} catch (OurAllianceException e) {
			e.printStackTrace();
		}
		Toast.makeText(this.getActivity(), "Competition could not be deleted", Toast.LENGTH_SHORT).show();
	}

	public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
		long seasonID = Long.parseLong(season.getValue());
    	Log.d(TAG,"season: "+seasonID);
		long compID = Long.parseLong(comp.getValue());
     	Log.d(TAG,"comp: "+compID);
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
				break;
			case LOADER_COMPETITION:
				setCompList(cursor);
				break;
			case LOADER_SEASON_SUMMARY:
				setSeasonSummary(cursor);
				break;
			case LOADER_COMPETITION_SUMMARY:
				setCompSummary(cursor);
				break;
		}
	}

	public void onLoaderReset(Loader<Cursor> loader) {
		switch(loader.getId()) {
			case LOADER_SEASON:
				setSeasonList(null);
				break;
			case LOADER_COMPETITION:
				setSeasonList(null);
				break;
			case LOADER_SEASON_SUMMARY:
				setSeasonSummary(null);
				break;
			case LOADER_COMPETITION_SUMMARY:
				setCompSummary(null);
				break;
		}
	}
	
	private void setSeasonList(Cursor cursor) {
		if(seasonCursor!=null) {
			seasonCursor.close();
		}
		seasonCursor = cursor;
		try {
			List<Season> seasons = SeasonDataSource.getList(seasonCursor);
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
		if(compCursor!=null) {
			compCursor.close();
		}
		compCursor = cursor;
		try {  
			List<Competition> comps = CompetitionDataSource.getList(compCursor);
			CharSequence[] compsViews = new CharSequence[comps.size()];
			CharSequence[] compsIds = new CharSequence[comps.size()];
			for(int i=0;i<comps.size();++i) {
				compsViews[i] = comps.get(i).getName().toString();
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
		if(seasonSummCursor!=null) {
			seasonSummCursor.close();
		}
		seasonSummCursor = cursor;
		try {
			selectedSeason = SeasonDataSource.getSingle(seasonSummCursor);
        	season.setSummary(selectedSeason.toString());
        	comp.setEnabled(true);
    		setHasOptionsMenu(true);
        } catch (Exception e) {
        	e.printStackTrace();
			season.setSummary(getActivity().getString(R.string.pref_season_summary));
			comp.setEnabled(false);
    		setHasOptionsMenu(false);
        }
	}
	
	private void setCompSummary(Cursor cursor) {
		if(compSummCursor!=null) {
			compSummCursor.close();
		}
		compSummCursor = cursor;
		try {
        	selectedComp = CompetitionDataSource.getSingle(compSummCursor);
        	comp.setSummary(selectedComp.toString());
        } catch (Exception e) {
        	e.printStackTrace();
	        comp.setSummary(getActivity().getString(R.string.pref_comp_summary));
        }
	}
	
	private void setMeasureSummary() {
//        int measurePosition;
//        String[] measureListVals = this.getResources().getStringArray(R.array.list_measurements);
//        String measureVal = prefs.getMeasure();
//        for(measurePosition=0;measurePosition<measureListVals.length;++measurePosition) {
//        	if(measureVal.equals(measureListVals[measurePosition])) {
//        		break;
//        	}
//        }
//        String[] measureList = this.getResources().getStringArray(R.array.list_measurements_display);
//        measure.setSummary(measureList[measurePosition]);
	}

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		Log.d(TAG, key);
		if(key.equals(seasonPrefString)) {
			comp.setValue("0");
        	this.getLoaderManager().restartLoader(LOADER_SEASON_SUMMARY, null, this);
        	this.getLoaderManager().restartLoader(LOADER_COMPETITION, null, this);
		} else if(key.equals(compPrefString)) {
        	this.getLoaderManager().restartLoader(LOADER_COMPETITION_SUMMARY, null, this);
		} else if(key.equals(measurePrefString)) {
			setMeasureSummary();
		}
	}
	
	public void resetData() {
		if(null!=seasonCursor) {
			seasonCursor.close();
		}
		if(null!=compCursor) {
			compCursor.close();
		}
		if(null!=seasonSummCursor) {
			seasonSummCursor.close();
		}
		if(null!=compSummCursor) {
			compSummCursor.close();
		}
	    prefs.unsetChangeListener(this);
    	SettingsFragment.this.getLoaderManager().destroyLoader(LOADER_SEASON_SUMMARY);
    	SettingsFragment.this.getLoaderManager().destroyLoader(LOADER_SEASON);
    	SettingsFragment.this.getLoaderManager().destroyLoader(LOADER_COMPETITION_SUMMARY);
    	SettingsFragment.this.getLoaderManager().destroyLoader(LOADER_COMPETITION);
        new Setup(this.getActivity(), true).execute();
	}
}
