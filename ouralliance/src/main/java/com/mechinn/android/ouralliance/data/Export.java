package com.mechinn.android.ouralliance.data;

import java.io.*;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.mechinn.android.ouralliance.BackgroundProgress;
import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.data.*;
import com.mechinn.android.ouralliance.data.frc2014.MatchScouting2014;
import com.mechinn.android.ouralliance.data.frc2014.MoveTeamScouting2014;
import com.mechinn.android.ouralliance.data.frc2014.TeamScouting2014;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;
import se.emilsjolander.sprinkles.CursorList;
import se.emilsjolander.sprinkles.Query;

public abstract class Export extends BackgroundProgress {
    public static final String TAG = "Export";
    protected static final String CSV = ".csv";

    private String directory;
    private String filename;
    private boolean fileWrite;
    private Writer writer;
    private Prefs prefs;
    private Competition competition;
    private String result;

	public Export(Activity activity) {
		super(activity, FLAG_EXPORT);
        prefs = new Prefs(getActivity());
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            directory = getActivity().getExternalFilesDir(null).getAbsolutePath()+File.separator+prefs.getYear()+File.separator;
        }
        fileWrite = true;
	}
    public Export(Activity activity, OutputStream output) {
        super(activity, FLAG_EXPORT);
        setWriter(output);
        prefs = new Prefs(getActivity());
    }
    public boolean isFileWrite() {
        return fileWrite;
    }
    public Writer getWriter() {
        return writer;
    }
    public void setWriter(Writer writer) {
        this.writer = writer;
    }
    public void setWriter(OutputStream output) {
        writer = new BufferedWriter(new OutputStreamWriter(output));
    }
    public String getFilename() {
        return filename;
    }
    public void setFilename(String filename) {
        this.filename = filename;
    }
    public String getDirectory() {
        return directory;
    }
    public Prefs getPrefs() {
        return prefs;
    }
    public Competition getCompetition() {
        return competition;
    }
    public abstract String work();

    @Override
    protected void onPreExecute() {
        this.setTitle("Export data");
        if(!fileWrite || null!=directory) {
            super.onPreExecute();
        } else {
            this.cancel(true);
        }
        this.setProgressFlag(INDETERMINATE);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        competition = Query.one(Competition.class, "SELECT * FROM "+Competition.TAG+" WHERE "+Competition._ID+"=?",prefs.getComp()).get();
        result = work();
        return null==result;
    }

    public String run() {
        doInBackground();
        return result;
    }

	@Override
    protected void onPostExecute(Boolean result) {
        getDialog().dismiss();
        if(!result) {
            Toast.makeText(getActivity(),this.result,Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "No known viewer for this file type", Toast.LENGTH_LONG).show();
            Log.d(TAG, filename);
            String type = URLConnection.guessContentTypeFromName(filename);
            Log.d(TAG, type);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse("file://"+filename), type);
            try {
                getActivity().startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getActivity(), "No known viewer for this file type", Toast.LENGTH_LONG).show();
            }
        }
    }
}
