package com.mechinn.android.ouralliance.fragment;

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

import com.activeandroid.query.Select;
import com.github.machinarius.preferencefragment.PreferenceFragment;
import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.Event;
import com.mechinn.android.ouralliance.rest.thebluealliance.GetEvents;
import com.mechinn.android.ouralliance.widget.EventListPreference;

import java.util.Collections;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.AsyncExecutor;

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
    public static final String TAG = "SettingsFragment";
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

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        prefs = new Prefs(this.getActivity());
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
                    new ResetDialogFragment().show(SettingsFragment.this.getFragmentManager(), "Reset Data? This is not reversable!");
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
        if(prefs.getYear()>0) {
            year.setSummary(yearArray.get(prefs.getYear()));
        }
        if(prefs.getYear() > 0) {
            load();
        } else {
            event.setEnabled(false);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
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
                dialog = new DeleteEventDialogFragment();
                dialogArgs = new Bundle();
                dialogArgs.putSerializable(DeleteEventDialogFragment.EVENT_ARG, selectedEvent);
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

    public void getCompetitions() {
        AsyncExecutor.create().execute(new GetEvents(this.getActivity()));
    }

	public void onEventMainThread(Prefs changedPrefs) {
        String key = changedPrefs.getKeyChanged();
		Log.d(TAG, key);
		if(key.equals(yearPrefString)) {
            Log.d(TAG,"selected season");
            year.setSummary(yearArray.get(prefs.getYear()));
			event.setValue("0");
            getActivity().invalidateOptionsMenu();
            if(!changedPrefs.isEventsDownloaded()) {
                getCompetitions();
            }
		} else if(key.equals(eventPrefString)) {
            Log.d(TAG,"selected competition");
            selectedEvent = event.get();
		} else if(key.equals(measurePrefString)) {
            Log.d(TAG,"selected measure");
		}
	}

    public void load() {
        AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
            @Override
            public void run() throws Exception {
                List<Event> events = new Select().from(Event.class).where(Event.YEAR + "=?", prefs.getYear()).execute();
                EventBus.getDefault().post(new LoadEvents(events));
            }
        });
    }

    public void onEventMainThread(Event eventsChanged) {
        load();
    }

    public void onEventMainThread(LoadEvents events) {
        List<Event> eventList = events.getEvents();
        Collections.sort(eventList);
        event.swapAdapter(eventList, prefs.getComp());
        if(eventList.size()>0) {
            event.setEnabled(true);
        } else {
            event.setEnabled(false);
        }
        if(prefs.getComp()>0) {
            selectedEvent = event.get();
        } else {
            selectedEvent = null;
        }
    }

    private class LoadEvents {
        List<Event> events;
        public LoadEvents(List<Event> events) {
            this.events = events;
        }
        public List<Event> getEvents() {
            return events;
        }
    }
}
