package com.mechinn.android.ouralliance.fragment;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.github.machinarius.preferencefragment.PreferenceFragment;
import com.mechinn.android.ouralliance.OurAlliance;
import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.Setup;
import com.mechinn.android.ouralliance.greenDao.Event;
import com.mechinn.android.ouralliance.greenDao.dao.DaoSession;
import com.mechinn.android.ouralliance.greenDao.dao.EventDao;
import com.mechinn.android.ouralliance.rest.thebluealliance.GetEvents;
import com.mechinn.android.ouralliance.widget.EventListPreference;

import java.util.ArrayList;

import de.greenrobot.dao.async.AsyncOperation;
import de.greenrobot.dao.async.AsyncOperationListener;
import de.greenrobot.dao.async.AsyncSession;

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
public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener, GenericDialogFragment.Listener, AsyncOperationListener {
    public static final String TAG = "SettingsFragment";
    private static final String EXECUTING_ASYNC = "executingAsync";
	public static final int DIALOG_RESET = 0;
	public static final int DIALOG_DELETECOMP = 1;
	private Prefs prefs;
	private String yearPrefString;
	private String eventPrefString;
	private String measurePrefString;
	private String resetDBPrefString;
    private SparseArray<String> yearArray;
	private ListPreference year;
	private EventListPreference event;
//	private CompetitionListPreference measure;
	private Preference resetDB;
	private Preference changelog;
	private Preference about;
    private Event selectedEvent;
    private GetEvents dialog;
    private DaoSession daoSession;
    private AsyncSession async;
    private AsyncOperation onEventsLoaded;

    @Override
    public void onAsyncOperationCompleted(AsyncOperation operation) {
        if (onEventsLoaded == operation) {
            if (operation.isCompletedSucessfully()) {
                ArrayList<Event> result = (ArrayList<Event>) operation.getResult();
                event.swapAdapter(result, prefs.getComp());
                if(result.size()>0) {
                    event.setEnabled(true);
                } else {
                    event.setEnabled(false);
                }
                if(prefs.getComp()>0) {
                    selectedEvent = event.get();
                } else {
                    selectedEvent = null;
                }
            } else {
                event.setSummary(getActivity().getString(R.string.pref_comp_summary));
            }
        }
    }
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        prefs = new Prefs(this.getActivity());
        daoSession = ((OurAlliance) this.getActivity().getApplication()).getDaoSession();
        async = ((OurAlliance) this.getActivity().getApplication()).getAsyncSession();
        async.setListener(this);
        yearPrefString = this.getString(R.string.pref_year);
        eventPrefString = this.getString(R.string.pref_comp);
        measurePrefString = this.getString(R.string.pref_measure);
        resetDBPrefString = this.getString(R.string.pref_resetDB);
        String[] yearNumberArray = this.getActivity().getResources().getStringArray(R.array.list_year);
        String[] yearSummaryArray = this.getActivity().getResources().getStringArray(R.array.list_year_display);
        yearArray = new SparseArray<String>(yearSummaryArray.length);
        for(int i=0;i<yearSummaryArray.length;++i) {
            yearArray.put(Integer.parseInt(yearNumberArray[i]),yearSummaryArray[i]);
        }
        year = (ListPreference) getPreferenceScreen().findPreference(yearPrefString);
        event = (EventListPreference) getPreferenceScreen().findPreference(eventPrefString);
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
        if(prefs.getYear()>0 && !prefs.isEventsDownloaded()) {
            getCompetitions();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        // Set up a listener whenever a key changes
        event.setValue(Long.toString(prefs.getComp()));
        prefs.setChangeListener(this);
        if(prefs.getYear()>0) {
            year.setSummary(yearArray.get(prefs.getYear()));
        }
        if(prefs.getYear() > 0) {
            queryCompetitions();
        } else {
            event.setEnabled(false);
        }
    }

    public void queryCompetitions() {
        onEventsLoaded = async.queryList(daoSession.getEventDao().queryBuilder().where(EventDao.Properties.Year.eq(prefs.getYear())).build());
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
        menu.findItem(R.id.deleteComp).setVisible(null!=selectedEvent);
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
		Log.d(TAG, "id: "+selectedEvent);
		//try inserting the team first in case it doesnt exist
        selectedEvent.delete();
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
			event.setValue("0");
            getActivity().invalidateOptionsMenu();
            queryCompetitions();
            if(!prefs.isEventsDownloaded()) {
                getCompetitions();
            }
		} else if(key.equals(eventPrefString)) {
            Log.d(TAG,"selected competition");
            selectedEvent = event.get();
		} else if(key.equals(measurePrefString)) {
            Log.d(TAG,"selected measure");

		}
	}
	
	public void resetData() {
	    prefs.unsetChangeListener(this);
        new Setup(this.getActivity(), true).execute();
	}
}
