package com.mechinn.android.ouralliance.data;
import java.util.ArrayList;

public class JsonCompetition {
	public static final String TAG = JsonCompetition.class.getSimpleName();


    private String end_date;
    private String key;
    private String name;
    private boolean official;
    private String short_name;
    private String start_date;
    public JsonCompetition() {

    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOfficial() {
        return official;
    }

    public void setOfficial(boolean official) {
        this.official = official;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

	public String toString() {
        return "Key: "+getKey()+" "+
                "Name: "+getName()+" "+
                "Official: "+getName()+" "+
                "Short Name: "+getShort_name()+" "+
                "Start Date: "+getStart_date();
	}
}
