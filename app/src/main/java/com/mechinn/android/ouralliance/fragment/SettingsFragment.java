package com.mechinn.android.ouralliance.fragment;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.Setup;
import com.mechinn.android.ouralliance.rest.thebluealliance.GetEvents;
import com.mechinn.android.ouralliance.widget.EventListPreference;

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
    private static final String EXECUTING_ASYNC = "executingAsync";
	public static final int DIALOG_RESET = 0;
	public static final int DIALOG_DELETECOMP = 1;
	private Prefs prefs;
	private String yearPrefString;
	private String compPrefString;
	private String measurePrefString;
	private String resetDBPrefString;
    private SparseArray<String> yearArray;
	private ListPreference year;
	private EventListPreference comp;
//	private CompetitionListPreference measure;
	private Preference resetDB;
	private Preference changelog;
	private Preference about;
    private Competition selectedComp;
    private GetEvents dialog;

    private ManyQuery.ResultHandler<Competition> onCompetitionsLoaded =
            new ManyQuery.ResultHandler<Competition>() {

                @Override
                public boolean handleResult(CursorList<Competition> result) {
                    if(result!=null && null!=result.getCursor() && !result.getCursor().isClosed()) {
                        ModelList<Competition> competitions = ModelList.from(result);
                        result.close();
                        comp.swapAdapter(competitions, prefs.getComp());
                        if(competitions.size()>0) {
                            comp.setEnabled(true);
                        } else {
                            comp.setEnabled(false);
                        }
                        if(prefs.getComp()>0) {
                            selectedComp = comp.get();
                        } else {
                            selectedComp = null;
                        }
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
        yearPrefString = this.getString(R.string.pref_year);
        compPrefString = this.getString(R.string.pref_comp);
        measurePrefString = this.getString(R.string.pref_measure);
        resetDBPrefString = this.getString(R.string.pref_resetDB);
        String[] yearNumberArray = this.getActivity().getResources().getStringArray(R.array.list_year);
        String[] yearSummaryArray = this.getActivity().getResources().getStringArray(R.array.list_year_display);
        yearArray = new SparseArray<String>(yearSummaryArray.length);
        for(int i=0;i<yearSummaryArray.length;++i) {
            yearArray.put(Integer.parseInt(yearNumberArray[i]),yearSummaryArray[i]);
        }
        year = (ListPreference) getPreferenceScreen().findPreference(yearPrefString);
        comp = (EventListPreference) getPreferenceScreen().findPreference(compPrefString);
//        measure = (CompetitionListPreference) getPreferenceScreen().findPreference(measurePrefString);
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
        if(prefs.getYear()>0 && !prefs.isCompetitionsDownloaded()) {
            getCompetitions();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        // Set up a listener whenever a key changes
        comp.setValue(Long.toString(prefs.getComp()));
        prefs.setChangeListener(this);
        if(prefs.getYear()>0) {
            year.setSummary(yearArray.get(prefs.getYear()));
        }
        if(prefs.getYear() > 0) {
            queryCompetitions();
        } else {
            comp.setEnabled(false);
        }
    }

    public void queryCompetitions() {
        Query.many(Competition.class, "select * from "+Competition.TAG+" where "+Competition.YEAR+"=? ORDER BY "+Competition.OFFICIAL+" DESC,"+Competition.NAME,prefs.getYear()).getAsync(this.getLoaderManager(), onCompetitionsLoaded);
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
        menu.findItem(R.id.newComp).setVisible(0!=prefs.getYear());
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
                dialogArgs.putInt(InsertCompDialogFragment.SEASON_ARG, prefs.getYear());
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
            case R.id.refreshCompetitions:
                getCompetitions();
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

    public void getCompetitions() {
        if(dialog==null || dialog.isComplete()) {
            dialog = new GetEvents(this.getActivity());
            dialog.execute();
        }
    }

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		Log.d(TAG, key);
		if(key.equals(yearPrefString)) {
            Log.d(TAG,"selected season");
            year.setSummary(yearArray.get(prefs.getYear()));
			comp.setValue("0");
            getActivity().invalidateOptionsMenu();
            queryCompetitions();
            if(!prefs.isCompetitionsDownloaded()) {
                getCompetitions();
            }
		} else if(key.equals(compPrefString)) {
            Log.d(TAG,"selected competition");
            selectedComp = comp.get();
		} else if(key.equals(measurePrefString)) {
            Log.d(TAG,"selected measure");

		}
	}
	
	public void resetData() {
	    prefs.unsetChangeListener(this);
        new Setup(this.getActivity(), true).execute();
	}
}
