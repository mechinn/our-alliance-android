package com.mechinn.android.ouralliance.data;

import java.util.ArrayList;

public class GetTeams {
	public static final String TAG = GetTeams.class.getSimpleName();
	private boolean result;
	private ArrayList<Team> data;
	public GetTeams() {
		this.data = new ArrayList<Team>();
	}
	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public ArrayList<Team> getData() {
		return data;
	}
	public void setData(ArrayList<Team> data) {
		this.data = data;
	}
	public void addTeam(Team team) {
		this.data.add(team);
	}
	public String toString() {
		String newString = "result: "+result;
		for(Team each : data) {
			newString += "\nTeam: \n"+each;
		}
		return newString;
	}
}
