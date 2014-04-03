package com.mechinn.android.ouralliance.data;

import android.util.Log;

import com.google.gson.annotations.SerializedName;
import org.supercsv.cellprocessor.FmtDate;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import se.emilsjolander.sprinkles.Query;
import se.emilsjolander.sprinkles.annotations.*;

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
    @Check(NUMBER+" > 0")
    @SerializedName("team_number")
	private int teamNumber;
    @Column(NAME)
	private String nickname;

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
        this.setNickName(name);
	}
	public int getTeamNumber() {
		return teamNumber;
	}
	public void setTeamNumber(int teamNumber) {
		this.teamNumber = teamNumber;
	}
	public String getNickName() {
		if(null==nickname) {
			return "";
		}
		return nickname;
	}
	public void setNickName(String nickname) {
		this.nickname = nickname;
	}
    public void setNickName(CharSequence nickname) {
        if(null==nickname) {
            setNickName("");
        } else {
            setNickName(nickname.toString());
        }
    }
	public String toString() {
		return this.getTeamNumber()+": "+this.getNickName();
	}
	public int compareTo(Team another) {
		return this.getTeamNumber() - another.getTeamNumber();
	}
	public boolean equals(Object data) {
        if(!(data instanceof Team)) {
            return false;
        }
		return  getTeamNumber()==((Team) data).getTeamNumber()
                && getNickName().equals(((Team)data).getNickName());
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
        return getNickName()=="";
    }
}
