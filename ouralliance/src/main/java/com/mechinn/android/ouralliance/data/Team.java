package com.mechinn.android.ouralliance.data;

import android.util.Log;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.supercsv.cellprocessor.FmtBool;
import org.supercsv.cellprocessor.FmtDate;
import org.supercsv.cellprocessor.HashMapper;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.*;
import org.supercsv.cellprocessor.ift.CellProcessor;
import se.emilsjolander.sprinkles.Query;
import se.emilsjolander.sprinkles.annotations.*;
import se.emilsjolander.sprinkles.annotations.NotNull;

@JsonIgnoreProperties({"id","mod"})
@Table(Team.TAG)
@UniqueComboConflictClause(ConflictClause.IGNORE)
public class Team extends AOurAllianceData implements Comparable<Team> {
	public static final String TAG = "Team";
	private static final long serialVersionUID = 6981108401294045422L;
	public static final String NUMBER = "teamNumber";
	public static final String NAME = "name";

    public static final String[] FIELD_MAPPING = new String[] {
            MODIFIED
            ,NUMBER
            ,NAME
    };

    public static final CellProcessor[] writeProcessor = new CellProcessor[] {
            new FmtDate("yyyy.MM.dd.HH.mm.ss")                      //MODIFIED
            ,new Optional()                     //NUMBER
            ,new Optional()    //NAME
    };

    public static final CellProcessor[] readProcessor = new CellProcessor[] {

    };

    @Column(NUMBER)
    @UniqueCombo()
    @Check("teamNumber > 0")
	private int teamNumber;
    @Column(NAME)
	private String name;

	public Team() {
		super();
	}
    public Team(long id) {
        super(id);
    }
	public Team(int number) {
		this.setTeamNumber(number);
	}
	public Team(int number, String name) {
        this.setTeamNumber(number);
        this.setName(name);
	}
	public int getTeamNumber() {
		return teamNumber;
	}
	public void setTeamNumber(int teamNumber) {
		this.teamNumber = teamNumber;
	}
	public String getName() {
		if(null==name) {
			return "";
		}
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    public void setName(CharSequence name) {
        if(null==name) {
            setName("");
        } else {
            setName(name.toString());
        }
    }
	public String toString() {
		return this.getTeamNumber()+": "+this.getName();
	}
	public int compareTo(Team another) {
		return this.getTeamNumber() - another.getTeamNumber();
	}
	public boolean equals(Team data) {
		return super.equals(data) &&
				getTeamNumber()==data.getTeamNumber() &&
				getName().equals(getName());
	}

    public boolean isValid() {
        Log.d(TAG, "id: " + getId());
        Team item = Query.one(Team.class, "SELECT * FROM " + TAG + " WHERE " + NUMBER + "=? LIMIT 1", getTeamNumber()).get();
        if(null!=item) {
            this.setId(item.getId());
            Log.d(TAG, "item: "+item+" is empty: "+item.empty()+" is equal: "+this.equals(item));
            Log.d(TAG, "import mod: " + item.getModified()+" sql mod: "+this.getModified()+" after: "+this.getModified().before(item.getModified()));
            if((this.getModified().before(item.getModified()) && !item.empty()) || this.equals(item)) {
                return false;
            }
        }
        return true;
    }
    public boolean empty() {
        return getTeamNumber()==0
                && getName()=="";
    }
}
