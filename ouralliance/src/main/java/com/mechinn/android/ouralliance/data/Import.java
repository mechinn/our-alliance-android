package com.mechinn.android.ouralliance.data;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.data.frc2014.MatchScouting2014;
import com.mechinn.android.ouralliance.data.frc2014.MoveTeamScouting2014;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.prefs.CsvPreference;
import se.emilsjolander.sprinkles.Query;

import java.io.*;
import java.util.Arrays;

public class Import extends Thread {
    public static final String TAG = "Import";
    protected static final String CSV = ".csv";
    public static final String RESULT = "Result";

    public enum Type {
        TEAMSCOUTING2014, MATCHSCOUTING2014;
        @Override
        public String toString() {
            switch(this) {
                case TEAMSCOUTING2014:
                    return "2014 team scouting";
                case MATCHSCOUTING2014:
                    return "2014 match scouting";
            }
            return "";
        }
        public String path() {
            switch(this) {
                case TEAMSCOUTING2014:
                    return "teamScouting";
                case MATCHSCOUTING2014:
                    return "matchScouting";
            }
            return "";
        }
    };

    private String filename;
	private String directory;
    private Context context;
    private Handler handler;
    private Prefs prefs;
    private Competition competition;
    private boolean fileRead;
    private Message message;
    private Reader reader;
    private String deviceName;
    private Type type;
    private boolean error;

	public Import(Context context, Handler handler, Type type) {
        common(context,handler);
        this.type = type;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            this.directory = context.getExternalFilesDir(null).getAbsolutePath()+File.separator+prefs.getYear()+File.separator;
        }
        fileRead = true;
	}
    public Import(Context context, Handler handler, String deviceName, InputStream input) {
        common(context,handler);
        this.deviceName = deviceName;
        final File file = new File(context.getCacheDir(), "from_"+deviceName.replace(" ","_").toLowerCase()+".csv");
        try {
            final OutputStream output = new FileOutputStream(file);
            final byte[] buffer = new byte[1024];
            int read;
            try {
                while((read=input.read(buffer)) != -1) {
                    output.write(buffer,0,read);
                }
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                output.close();
            }
            reader = new FileReader(file);
        } catch (FileNotFoundException e) {
            Log.wtf(TAG, "file not found", e);
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG,"error writing file "+file);
            e.printStackTrace();
        }
    }
    public void common(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
        this.prefs = new Prefs(context);
        message = new Message();
    }

    public void run() {
        execute();
    }

    public void execute() {
        if(!fileRead || null!=directory) {
            this.competition = Query.one(Competition.class, "SELECT * FROM "+Competition.TAG+" WHERE "+Competition._ID+"=?",prefs.getComp()).get();
            if(fileRead) {
                filename = directory+type.path();
                new File(filename).mkdirs();
                filename += File.separator+competition.getCode()+CSV;
                try {
                    reader = new FileReader(filename);
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                    message.getData().putString(RESULT,"Error opening writable file: "+filename);
                    error=true;
                }
            }
            if(!error) {
                CsvBeanReader beanReader = null;
                try {
                    beanReader = new CsvBeanReader(reader, CsvPreference.EXCEL_PREFERENCE);

                    // write the header
                    final String[] header = beanReader.getHeader(true);
                    switch (prefs.getYear()) {
                        default:
                            Log.w(TAG, "No valid year selected");
                            message.getData().putString(RESULT, "No valid year selected");
                            error = true;
                            break;
                        case 2014:
                            if (Arrays.equals(header, MoveTeamScouting2014.FIELD_MAPPING)) {
                                this.type = Type.TEAMSCOUTING2014;
                                Log.d(TAG, "import "+this.type);
                                // write the beans
                                MoveTeamScouting2014 move;
                                while ((move = beanReader.read(MoveTeamScouting2014.class, header, MoveTeamScouting2014.readProcessor)) != null) {
                                    Log.d(TAG, "lineNo=" + beanReader.getLineNumber() + ", rowNo=" + beanReader.getRowNumber() + ", data=" + move);
                                    move.save(new Season(prefs.getSeason()), this.competition);
                                }
                            } else if (Arrays.equals(header, MatchScouting2014.FIELD_MAPPING)) {
                                this.type = Type.MATCHSCOUTING2014;
                                Log.d(TAG, "import "+this.type);
                                // write the beans
                                MatchScouting2014 match;
                                while ((match = beanReader.read(MatchScouting2014.class, header, MatchScouting2014.readProcessor)) != null) {
                                    Log.d(TAG, "lineNo=" + beanReader.getLineNumber() + ", rowNo=" + beanReader.getRowNumber() + ", data=" + match);
                                    match.setCompetition(this.competition);
                                    match.getCompetitionTeam().save();
                                    match.getMatch().save();
                                    match.save();
                                }
                            } else {
                                Log.w(TAG, "Invalid file columns: " + filename);
                                message.getData().putString(RESULT, "Invalid file columns: " + filename);
                                error = true;
                            }
                            break;
                    }
                } catch (FileNotFoundException e) {
                    Log.e(TAG, e.toString());
                    message.getData().putString(RESULT, "File does not exist: " + filename);
                    error = true;
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                    message.getData().putString(RESULT, "Error reading file: " + filename);
                    error = true;
                } finally {
                    if (beanReader != null) {
                        try {
                            beanReader.close();
                        } catch (IOException e) {
                            Log.e(TAG, e.toString());
                            message.getData().putString(RESULT, "Error closing reader");
                            error = true;
                        }
                    }
                }
            }
            if(!error) {
                if (fileRead) {
                    message.getData().putString(RESULT, "Imported "+type+" from " + filename);
                } else {
                    message.getData().putString(RESULT, "Received "+type+" from " + deviceName);
                }
            }
            handler.sendMessage(message);
        }
    }
}
