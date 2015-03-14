package com.mechinn.android.ouralliance.csv;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;

import com.mechinn.android.ouralliance.Prefs;

import org.apache.commons.lang3.StringUtils;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.util.AsyncExecutor;
import timber.log.Timber;

/**
 * Created by mechinn on 3/14/15.
 */
public abstract class ExportCsv implements AsyncExecutor.RunnableEx {
    public final static String TRUE = "true";
    public final static String FALSE = "false";
    private List<String> filePartsList;
    private File dir;
    private String filename;
    private Prefs prefs;
    private String[] header;
    private List<List<String>> list;
    private Context context;
    private String title;
    public ExportCsv(Context context, String... fileParts) {
        this.context = context;
        this.prefs = new Prefs(context);
        filePartsList = new ArrayList<>();
        filePartsList.add(prefs.getYearString());
        filePartsList.add("csv");
        for(String part : fileParts) {
            filePartsList.add(part);
        }
        File fileDir = context.getExternalFilesDir(null);
        dir = new File(fileDir, StringUtils.join(filePartsList,File.separator));
        dir.mkdirs();
        list = new ArrayList<>();
    }
    public Prefs getPrefs() {
        return prefs;
    }
    public String fmtBoolean(Boolean value) {
        if(null==value) {
            return "";
        }
        return value?TRUE:FALSE;
    }
    public String fmtInteger(Integer value) {
        if(null==value) {
            return "";
        }
        return String.valueOf(value);
    }
    public String fmtLong(Long value) {
        if(null==value) {
            return "";
        }
        return String.valueOf(value);
    }
    public String fmtDouble(Double value) {
        if(null==value) {
            return "";
        }
        return String.valueOf(value);
    }
    public String fmtFloat(Float value) {
        if(null==value) {
            return "";
        }
        return String.valueOf(value);
    }
    public String fmtDate(Date value) {
        if(null==value) {
            return "";
        }
        return value.toString();
    }
    public void setHeader(String[] header) {
        this.header = header;
    }
    public void addToList(List<String> line) {
        list.add(line);
    }
    public void setFileName(String filename) {
        this.filename = filename+".csv";
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void run() throws IOException {
        File file = new File(dir, filename);
        Timber.d("filename: "+file.getAbsolutePath());
        FileWriter fileWriter = new FileWriter(file);
        CsvListWriter csvWriter = null;
        try {
            csvWriter = new CsvListWriter(fileWriter, CsvPreference.EXCEL_PREFERENCE);
            csvWriter.writeHeader(header);
            for(List<String> line : list) {
                Timber.d("writing: " + StringUtils.join(line));
                csvWriter.write(line);
            }
        } finally {
            if (csvWriter != null) {
                csvWriter.close();
            }
        }
        Intent shareFile = new Intent("com.mechinn.android.ouralliance.ACTION_RETURN_FILE");
        shareFile.setAction(Intent.ACTION_SEND);
        shareFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Uri fileUri = FileProvider.getUriForFile(context,"com.mechinn.android.ouralliance.provider.files",file);
        shareFile.putExtra(Intent.EXTRA_STREAM,fileUri);
        String type = context.getContentResolver().getType(fileUri);
        Timber.d("type: " + type);
        shareFile.setType(type);
        Timber.d("uri: " + fileUri);
        context.startActivity(Intent.createChooser(shareFile, title));
    }
}
