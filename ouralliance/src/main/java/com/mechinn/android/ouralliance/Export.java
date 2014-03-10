package com.mechinn.android.ouralliance;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLConnection;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.mechinn.android.ouralliance.data.Competition;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.frc2014.MatchScouting2014;
import com.mechinn.android.ouralliance.data.frc2014.TeamScouting2014;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;
import se.emilsjolander.sprinkles.CursorList;
import se.emilsjolander.sprinkles.Query;

import com.mechinn.android.ouralliance.data.CompetitionTeam;

public class Export extends BackgroundProgress {
    public static final String TAG = "Export";
    private static final String CSV = ".csv";
	private String directory;
    private String filename;
	private Context context;
    private Types type;

    public enum Types {TEAMSCOUTING2014, MATCHSCOUTING2014};
	
	private Prefs prefs;
	public Export(Activity activity, Types type) {
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
        CsvBeanWriter beanWriter = null;
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
                try {
                    beanWriter = new CsvBeanWriter(new FileWriter(filename), CsvPreference.EXCEL_PREFERENCE);

                    // write the header
                    beanWriter.writeHeader(TeamScouting2014.FIELD_MAPPING);

                    // write the beans
                    for( final TeamScouting2014 team : teams ) {
                        Log.d(TAG,"writing: "+team.toString());
                        beanWriter.write(team, TeamScouting2014.FIELD_MAPPING, TeamScouting2014.writeProcessor);
                    }
                } catch (IOException e) {
                    Log.e(TAG,e.toString());
                    setStatus("Error writing to file: "+filename);
                    return false;
                } finally {
                    if( beanWriter != null ) {
                        try {
                            beanWriter.close();
                        } catch (IOException e) {
                            Log.e(TAG,e.toString());
                            setStatus("Error closing writer");
                            return false;
                        }
                    }
                }
                break;
            case MATCHSCOUTING2014:
                filename = directory+"matchScouting";
                new File(filename).mkdirs();
                filename += File.separator+competition.getCode()+CSV;
                CursorList<MatchScouting2014> matches = Query.many(MatchScouting2014.class,
                        "SELECT "+MatchScouting2014.TAG+".*" +
                                " FROM " + MatchScouting2014.TAG +
                                " INNER JOIN " + CompetitionTeam.TAG+
                                " ON " + MatchScouting2014.TAG+"."+MatchScouting2014.TEAM+"="+CompetitionTeam.TAG+"."+CompetitionTeam.TEAM+
                                " AND "+CompetitionTeam.COMPETITION+"="+prefs.getComp()).get();

                try {
                    beanWriter = new CsvBeanWriter(new FileWriter(filename), CsvPreference.EXCEL_PREFERENCE);

                    // write the header
                    beanWriter.writeHeader(TeamScouting2014.FIELD_MAPPING);

                    // write the beans
                    for( final MatchScouting2014 team : matches ) {
                        Log.d(TAG,"writing: "+team.toString());
                        beanWriter.write(team, TeamScouting2014.FIELD_MAPPING, TeamScouting2014.writeProcessor);
                    }
                } catch (IOException e) {
                    Log.e(TAG,e.toString());
                    setStatus("Error writing to file: "+filename);
                    return false;
                } finally {
                    if( beanWriter != null ) {
                        try {
                            beanWriter.close();
                        } catch (IOException e) {
                            Log.e(TAG,e.toString());
                            setStatus("Error closing writer");
                            return false;
                        }
                    }
                }
                break;
        }



        setStatus("Finished");
		return true;
	}

	@Override
    protected void onPostExecute(Boolean result) {
		getDialog().dismiss();
        if(result) {
            Log.d(TAG, filename);
            String type = URLConnection.guessContentTypeFromName(filename);
            Log.d(TAG, type);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse("file://"+filename), type);
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, "No known viewer for this file type", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context,getDialog().getProgressStatus(),Toast.LENGTH_SHORT).show();
        }
    }
}
