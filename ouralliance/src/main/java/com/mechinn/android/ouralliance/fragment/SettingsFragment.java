package com.mechinn.android.ouralliance.fragment;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.Setup;
import com.mechinn.android.ouralliance.Utility;
import com.mechinn.android.ouralliance.data.Competition;
import com.mechinn.android.ouralliance.data.Season;
import com.mechinn.android.ouralliance.widget.ListPreference;

import se.emilsjolander.sprinkles.CursorList;
import se.emilsjolander.sprinkles.ManyQuery;
import se.emilsjolander.sprinkles.ModelList;
import se.emilsjolander.sprinkles.Query;
import se.emilsjolander.sprinkles.SqlStatement;

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
public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener, GenericDialogFragment.Listener {
    public static final String TAG = "SettingsFragment";
	public static final int DIALOG_RESET = 0;
	public static final int DIALOG_DELETECOMP = 1;
	private Prefs prefs;
	private String seasonPrefString;
	private String compPrefString;
	private String measurePrefString;
	private String resetDBPrefString;
	private ListPreference<Season> season;
	private ListPreference<Competition> comp;
//	private ListPreference measure;
	private Preference resetDB;
	private Preference changelog;
	private Preference about;
    private Season selectedSeason;
    private Competition selectedComp;

    private ManyQuery.ResultHandler<Season> onSeasonsLoaded =
            new ManyQuery.ResultHandler<Season>() {

                @Override
                public boolean handleResult(CursorList<Season> result) {
                    if(result!=null && null!=result.getCursor() && !result.getCursor().isClosed()) {
                        ModelList<Season> seasons = ModelList.from(result);
                        result.close();
                        season.swapAdapter(seasons, prefs.getSeason());
                        return true;
                    } else {
                        season.setSummary(getActivity().getString(R.string.pref_season_summary));
                        comp.setEnabled(false);
                        setHasOptionsMenu(false);
                        return false;
                    }
                }
            };

    private ManyQuery.ResultHandler<Competition> onCompetitionsLoaded =
            new ManyQuery.ResultHandler<Competition>() {

                @Override
                public boolean handleResult(CursorList<Competition> result) {
                    if(result!=null && null!=result.getCursor() && !result.getCursor().isClosed()) {
                        ModelList<Competition> competitions = ModelList.from(result);
                        result.close();
                        comp.swapAdapter(competitions, prefs.getComp());
                        return true;
                    } else {
                        comp.setSummary(getActivity().getString(R.string.pref_comp_summary));
                        return false;
                    }
                }
            };
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        prefs = new Prefs(this.getActivity());
        seasonPrefString = this.getString(R.string.pref_season);
        compPrefString = this.getString(R.string.pref_comp);
        measurePrefString = this.getString(R.string.pref_measure);
        resetDBPrefString = this.getString(R.string.pref_resetDB);
        season = (ListPreference<Season>) getPreferenceScreen().findPreference(seasonPrefString);
        comp = (ListPreference<Competition>) getPreferenceScreen().findPreference(compPrefString);
//        measure = (ListPreference) getPreferenceScreen().findPreference(measurePrefString);
        resetDB = getPreferenceScreen().findPreference(resetDBPrefString);
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
        changelog = getPreferenceScreen().findPreference(this.getString(R.string.pref_changeLog));
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
        about = getPreferenceScreen().findPreference(this.getString(R.string.pref_about));
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
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Set up a listener whenever a key changes
        season.setValue(Long.toString(prefs.getSeason()));
        comp.setValue(Long.toString(prefs.getComp()));
        prefs.setChangeListener(this);
        Query.all(Season.class).getAsync(this.getLoaderManager(),onSeasonsLoaded);
        Query.many(Competition.class, "select * from competition where season=? ORDER BY name",Long.parseLong(season.getValue())).getAsync(this.getLoaderManager(), onCompetitionsLoaded);
    }

	@Override
	public void onPause() {
	    // Unregister the listener whenever a key changes
	    prefs.unsetChangeListener(this);
        super.onPause();
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.settings, menu);
	}

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.newComp).setVisible(null!=selectedSeason);
        menu.findItem(R.id.deleteComp).setVisible(null!=selectedComp);
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
        DialogFragment dialog;
        Bundle dialogArgs;
	    switch (item.getItemId()) {
	        case R.id.newComp:
                dialog = new InsertCompDialogFragment();
                dialogArgs = new Bundle();
                dialogArgs.putSerializable(InsertCompDialogFragment.SEASON_ARG, selectedSeason);
                dialog.setArguments(dialogArgs);
                dialog.show(this.getFragmentManager(), "Add Competition");
	            return true;
	        case R.id.deleteComp:
                dialog = new GenericDialogFragment();
                dialogArgs = new Bundle();
                dialogArgs.putInt(GenericDialogFragment.FLAG, DIALOG_DELETECOMP);
                dialogArgs.putInt(GenericDialogFragment.MESSAGE, R.string.deleteComp);
                dialog.setArguments(dialogArgs);
                dialog.show(SettingsFragment.this.getFragmentManager(), "Delete selected compeittion?");
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

	public void deleteCompetition() {
		Log.d(TAG, "id: "+selectedComp);
		//try inserting the team first in case it doesnt exist
		selectedComp.delete();
	}

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		Log.d(TAG, key);
		if(key.equals(seasonPrefString)) {
            selectedSeason = season.get();
            prefs.setYear(Integer.toString(selectedSeason.getYear()));
			comp.setValue("0");
            comp.setEnabled(true);
            Query.many(Competition.class, "select * from competition where season=? ORDER BY name",Long.parseLong(season.getValue())).getAsync(this.getLoaderManager(),onCompetitionsLoaded);
		} else if(key.equals(compPrefString)) {
            selectedComp = comp.get();
		} else if(key.equals(measurePrefString)) {

		}
	}
	
	public void resetData() {
	    prefs.unsetChangeListener(this);
        new Setup(this.getActivity(), true).execute();
	}
}
