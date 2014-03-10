package com.mechinn.android.ouralliance.data;

import se.emilsjolander.sprinkles.Query;
import se.emilsjolander.sprinkles.annotations.Check;
import se.emilsjolander.sprinkles.annotations.Column;
import se.emilsjolander.sprinkles.annotations.ConflictClause;
import se.emilsjolander.sprinkles.annotations.NotNull;
import se.emilsjolander.sprinkles.annotations.Table;
import se.emilsjolander.sprinkles.annotations.UniqueCombo;
import se.emilsjolander.sprinkles.annotations.UniqueComboConflictClause;

@Table(Season.TAG)
@UniqueComboConflictClause(ConflictClause.IGNORE)
public class Season extends AOurAllianceData implements Comparable<Season> {
	public static final String TAG = "Season";
	private static final long serialVersionUID = -7570760453585739510L;
	public static final String YEAR = "year";
	public static final String TITLE = "title";

    public static final String[] FIELD_MAPPING = new String[] {
            MODIFIED
            ,YEAR
            ,TITLE
    };

    @Column(YEAR)
    @UniqueCombo
    @Check("year > 0")
	private int year;
    @Column(TITLE)
    @NotNull
    @Check(TITLE+" != ''")
    private String title;

	public Season() {
		super();
	}
    public Season(long id) {
        super(id);
    }
	public Season(int year, String title) {
        this.setYear(year);
        this.setTitle(title);
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public String getTitle() {
		if(null==title) {
			return "";
		}
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
    public void setTitle(CharSequence title) {
        if(null==title) {
            setTitle("");
        } else {
            setTitle(title.toString());
        }
    }
	public String toString() {
		return getYear()+": "+getTitle();
	}
	public int compareTo(Season another) {
		int yearCompare = this.getYear() - another.getYear();
        if(0==yearCompare) {
            return this.getTitle().toString().compareToIgnoreCase(another.getTitle().toString());
        } else {
            return yearCompare;
        }
	}
	public boolean equals(Season data) {
		return super.equals(data) &&
				getYear()==data.getYear() &&
				getTitle().equals(data.getTitle());
	}

    public AOurAllianceData validate() {
        return Query.one(Season.class, "SELECT * FROM " + TAG + " WHERE " + YEAR + "=? LIMIT 1", getYear()).get();
    }
}
