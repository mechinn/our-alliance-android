package com.mechinn.android.ouralliance.data.frc2014;

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
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;
import se.emilsjolander.sprinkles.CursorList;
import se.emilsjolander.sprinkles.Query;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class ExportMatchScouting2014 extends Export {
    public static final String TAG = "ExportMatchScouting2014";

    public ExportMatchScouting2014(Activity activity) {
        super(activity);
    }
    public ExportMatchScouting2014(Activity activity, OutputStream output) {
        super(activity, output);
    }

    public String work() {
        String result = null;
        if(isFileWrite()) {
            setFilename(getDirectory() + Import.Type.MATCHSCOUTING2014.path());
            new File(getFilename()).mkdirs();
            setFilename(getFilename() + File.separator + getCompetition().getEventCode() + CSV);
            try {
                setWriter(new FileWriter(getFilename()));
            } catch (IOException e) {
                Log.e(TAG, e.toString());
                result = "Error opening writable file: "+getFilename();
            }
        }
        if(null==result) {
            CursorList<MatchScouting2014> matches = Query.many(MatchScouting2014.class,
                    "SELECT " + MatchScouting2014.TAG + ".*" +
                            " FROM " + MatchScouting2014.TAG +
                            " INNER JOIN " + Match.TAG +
                            " ON " + MatchScouting2014.TAG + "." + MatchScouting2014.MATCH + "=" + Match.TAG + "." + Match._ID +
                            " AND " + Match.COMPETITION + "=?"
                    , getPrefs().getComp()
            ).get();
            CsvBeanWriter beanWriter = null;
            try {
                beanWriter = new CsvBeanWriter(getWriter(), CsvPreference.EXCEL_PREFERENCE);

                // write the header
                beanWriter.writeHeader(MatchScouting2014.FIELD_MAPPING);

                // write the beans
                for (MatchScouting2014 match : matches) {
                    Log.d(TAG, "writing: " + match.toString());
                    beanWriter.write(match, MatchScouting2014.FIELD_MAPPING, MatchScouting2014.writeProcessor);
                }
            } catch (IOException e) {
                Log.e(TAG, e.toString());
                if (isFileWrite()) {
                    result = "Error writing to : " + getFilename();
                } else {
                    result = "Error sending to bluetooth device";
                }
            } finally {
                if (beanWriter != null) {
                    try {
                        beanWriter.close();
                    } catch (IOException e) {
                        Log.e(TAG, e.toString());
                        result = "Error closing writer";
                    }
                }
            }
        }
		return result;
	}
}
