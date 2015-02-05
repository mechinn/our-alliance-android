package com.mechinn.android.ouralliance;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.PackageManager.NameNotFoundException;
import android.preference.PreferenceManager;
import com.mechinn.android.ouralliance.data.Competition;
import com.mechinn.android.ouralliance.data.CompetitionTeam;
import com.mechinn.android.ouralliance.data.Match;

import java.util.Map;

public class Prefs {
    public static final String TAG = "Prefs";
	private SharedPreferences prefs;
	private int currentVersion;
	private String dbSetupPref;
	private String dbSetupDefault;
	private String compPref;
	private String compDefault;
	private String measurePref;
	private String measureDefault;
	private String versionPref;
	private String versionDefault;
	private String practicePref;
	private String practiceDefault;
    private String adsPref;
    private String adsDefault;
	private String yearPref;
	private String yearDefault;
    private Editor editor;
	
	public Prefs(Context context) {
		this.dbSetupPref = context.getString(R.string.pref_resetDB);
		this.dbSetupDefault = context.getString(R.string.pref_resetDB_default);
		this.compPref = context.getString(R.string.pref_comp);
		this.compDefault = context.getString(R.string.pref_comp_default);
		this.measurePref = context.getString(R.string.pref_measure);
		this.measureDefault = context.getString(R.string.pref_measure_default);
		this.versionPref = context.getString(R.string.pref_about);
		this.versionDefault = context.getString(R.string.pref_about_default);
		this.practicePref = context.getString(R.string.pref_practice);
		this.practiceDefault = context.getString(R.string.pref_practice_default);
        this.adsPref = context.getString(R.string.pref_ads);
        this.adsDefault = context.getString(R.string.pref_ads_default);
		this.yearPref = context.getString(R.string.pref_year);
		this.yearDefault = context.getString(R.string.pref_year_default);
		try {
			this.currentVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			this.currentVersion = 0;
		}
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
        editor = prefs.edit();
	}
	public int getCurrentVersion() {
		return currentVersion;
	}
	public void clear() {
		editor.clear();
		editor.apply();
	}
	public void setDbSetup(Boolean setup) {
		editor.putString(dbSetupPref, setup?"true":"false");
		editor.apply();
	}
	public boolean getDbSetup() {
		return Boolean.parseBoolean(prefs.getString(dbSetupPref, dbSetupDefault));
	}
    public void setCompetitionsDownloaded(Boolean downloaded) {
        editor.putString(Competition.TAG+"_"+getYear(), downloaded?"true":"false");
        editor.apply();
    }
    public boolean isCompetitionsDownloaded() {
        return Boolean.parseBoolean(prefs.getString(Competition.TAG+"_"+getYear(), "false"));
    }
    public void clearCompetitionsDownloaded() {
        final Map<String, ?> all = prefs.getAll();
        for(Map.Entry<String,?> entry : all.entrySet()) {
            if(entry.getKey().startsWith(Competition.TAG+"_")) {
                editor.remove(entry.getKey());
            }
        }
        editor.apply();
    }
    public void setCompetitionTeamsDownloaded(Boolean downloaded) {
        editor.putString(CompetitionTeam.TAG+"_"+getYear()+"_"+getComp(), downloaded?"true":"false");
        editor.apply();
    }
    public boolean isCompetitionTeamsDownloaded() {
        return Boolean.parseBoolean(prefs.getString(CompetitionTeam.TAG+"_"+getYear()+"_"+getComp(), "false"));
    }
    public void clearCompetitionTeamsDownloaded() {
        final Map<String, ?> all = prefs.getAll();
        for(Map.Entry<String,?> entry : all.entrySet()) {
            if(entry.getKey().startsWith(CompetitionTeam.TAG+"_")) {
                editor.remove(entry.getKey());
            }
        }
        editor.apply();
    }
    public void setMatchesDownloaded(Boolean downloaded) {
        editor.putString(Match.TAG+"_"+getYear()+"_"+getComp(), downloaded?"true":"false");
        editor.apply();
    }
    public boolean isMatchesDownloaded() {
        return Boolean.parseBoolean(prefs.getString(Match.TAG+"_"+getYear()+"_"+getComp(), "false"));
    }
    public void clearMatchesDownloaded() {
        final Map<String, ?> all = prefs.getAll();
        for(Map.Entry<String,?> entry : all.entrySet()) {
            if(entry.getKey().startsWith(Match.TAG+"_")) {
                editor.remove(entry.getKey());
            }
        }
        editor.apply();
    }
	public void setYear(String year) {
		editor.putString(yearPref, year);
		editor.apply();
	}
	public int getYear() {
		return Integer.parseInt(prefs.getString(yearPref, yearDefault));
	}
	public long getComp() {
		return Long.parseLong(prefs.getString(compPref, compDefault));
	}
	public String getMeasure() {
		return prefs.getString(measurePref, measureDefault);
	}
	public boolean isInches() {
		return getMeasure().equals(measureDefault);
	}
	public boolean isPractice() {
		return prefs.getBoolean(practicePref, Boolean.parseBoolean(practiceDefault));
	}
	public void setPractice(boolean practice) {
		editor.putBoolean(practicePref, practice);
		editor.apply();
	}
    public boolean isAdsDisabled() {
        return prefs.getBoolean(adsPref, Boolean.parseBoolean(adsDefault));
    }
    public void setAdsDisabled(boolean adsDisabled) {
        editor.putBoolean(adsPref, adsDisabled);
        editor.apply();
    }
	public int getVersion() {
		return Integer.parseInt(prefs.getString(versionPref, versionDefault));
	}
	public void setVersion(int version) {
		editor.putString(versionPref, Integer.toString(version));
		editor.apply();
	}
	public void setChangeListener(OnSharedPreferenceChangeListener listener) {
		prefs.registerOnSharedPreferenceChangeListener(listener);
	}
	public void unsetChangeListener(OnSharedPreferenceChangeListener listener) {
		prefs.unregisterOnSharedPreferenceChangeListener(listener);
	}
}
