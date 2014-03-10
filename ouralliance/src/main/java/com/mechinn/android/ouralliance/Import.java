package com.mechinn.android.ouralliance;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import com.mechinn.android.ouralliance.data.Competition;
import com.mechinn.android.ouralliance.data.CompetitionTeam;
import com.mechinn.android.ouralliance.data.frc2014.TeamScouting2014;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;
import se.emilsjolander.sprinkles.CursorList;
import se.emilsjolander.sprinkles.Query;

import java.io.*;
import java.net.URLConnection;
import java.util.Arrays;

public class Import extends BackgroundProgress {
    public static final String TAG = "Export";
    private static final String CSV = ".csv";
	private String directory;
    private String filename;
	private Context context;
    private Types type;

    public enum Types {TEAMSCOUTING2014, MATCHSCOUTING2014};

	private Prefs prefs;
	public Import(Activity activity, Types type) {
		super(activity, FLAG_EXPORT);
		context = activity;
		prefs = new Prefs(activity);
        this.type = type;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            directory = activity.getExternalFilesDir(null).getAbsolutePath()+File.separator+prefs.getYear()+File.separator;
        }
	}
	
	@Override
	protected void onPreExecute() {
		this.setTitle("Export data");
		if(null!=directory) {
			super.onPreExecute();
		} else {
			this.cancel(true);
		}
	}

	@Override
	protected Boolean doInBackground(Void... params) {
        Competition competition = Query.one(Competition.class, "SELECT * FROM "+Competition.TAG+" WHERE "+Competition._ID+"=?",prefs.getComp()).get();

        switch(type) {
            case TEAMSCOUTING2014:
                filename = directory+"teamScouting";
                new File(filename).mkdirs();
                filename += File.separator+competition.getCode()+CSV;
                CursorList<TeamScouting2014> teams = Query.many(TeamScouting2014.class,
                        "SELECT "+TeamScouting2014.TAG+".*" +
                                " FROM " + TeamScouting2014.TAG +
                                " INNER JOIN " + CompetitionTeam.TAG+
                                " ON " + TeamScouting2014.TAG+"."+TeamScouting2014.TEAM+"="+CompetitionTeam.TAG+"."+CompetitionTeam.TEAM+
                                " AND "+CompetitionTeam.COMPETITION+"="+prefs.getComp()).get();

                CsvBeanReader beanReader = null;
                try {
                    beanReader = new CsvBeanReader(new FileReader(filename), CsvPreference.EXCEL_PREFERENCE);

                    // write the header
                    final String[] header = beanReader.getHeader(true);

                    if(Arrays.equals(header,TeamScouting2014.FIELD_MAPPING)) {
                        // write the beans
                        TeamScouting2014 team;
                        while( (team = beanReader.read(TeamScouting2014.class, header, TeamScouting2014.readProcessor)) != null ) {
                            Log.d(TAG,"lineNo="+beanReader.getLineNumber()+", rowNo="+beanReader.getRowNumber()+", team="+team);
                            team.save();
                            //create competition teams for whatever competition the user is on right now
                            new CompetitionTeam(new Competition(prefs.getComp()),team.getTeam()).save();
                        }
                    } else {
                        Log.w(TAG, "Invalid file columns: "+filename);
                        setStatus("Invalid file columns: "+filename);
                        return false;
                    }
                } catch (FileNotFoundException e) {
                    Log.e(TAG, e.toString());
                    setStatus("File does not exist: "+filename);
                    return false;
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                    setStatus("Error reading file: "+filename);
                    return false;
                } finally {
                    if( beanReader != null ) {
                        try {
                            beanReader.close();
                        } catch (IOException e) {
                            Log.e(TAG,e.toString());
                            setStatus("Error closing reader");
                            return false;
                        }
                    }
                }
                break;
            case MATCHSCOUTING2014:

                break;
        }



        setStatus("Finished");
		return true;
	}

	@Override
    protected void onPostExecute(Boolean result) {
		getDialog().dismiss();
        if(!result) {
            Toast.makeText(context,getDialog().getProgressStatus(),Toast.LENGTH_SHORT).show();
        }
    }
}
