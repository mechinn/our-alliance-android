package com.mechinn.android.ouralliance;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import au.com.bytecode.opencsv.CSVWriter;

import com.mechinn.android.ouralliance.data.CompetitionTeam;
import com.mechinn.android.ouralliance.data.frc2013.TeamScouting2013;
import com.mechinn.android.ouralliance.data.source.CompetitionTeamDataSource;
import com.mechinn.android.ouralliance.data.source.frc2013.TeamScouting2013DataSource;
import com.mechinn.android.ouralliance.error.OurAllianceException;

public class Export extends BackgroundProgress {
	public static final String TAG = Export.class.getSimpleName();
	private String filename;
	private CSVWriter writer;
	private Context context;
	
	private Prefs prefs;
	private TeamScouting2013DataSource teamScouting2013Data;
	private CompetitionTeamDataSource competitionTeamData;
	public Export(Activity activity) {
		super(activity, FLAG_EXPORT);
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			filename = activity.getExternalFilesDir(null).getAbsolutePath()+File.separator+"test.csv";
		}
		context = activity;
		prefs = new Prefs(activity);
		teamScouting2013Data = new TeamScouting2013DataSource(activity);
		competitionTeamData = new CompetitionTeamDataSource(activity);
//		HeaderColumnNameTranslateMappingStrategy<MockBean> strat = new HeaderColumnNameTranslateMappingStrategy<MockBean>();
//        strat.setType(MockBean.class);
//        Map<String, String> map = new HashMap<String, String>();
//        map.put("n", "name");
//        map.put("o", "orderNumber");
//        map.put("foo", "id");
//        strat.setColumnMapping(map);
//
//        CsvToBean<MockBean> csv = new CsvToBean<MockBean>();
//        List<MockBean> list = csv.parse(strat, new StringReader(s));
//        assertNotNull(list);
//        assertTrue(list.size() == 2);
//        MockBean bean = list.get(0);
//        assertEquals("kyle", bean.getName());
//        assertEquals("123456", bean.getOrderNumber());
//        assertEquals("emp123", bean.getId());
	}
	
	public void getData() {
		boolean cancel = false;
		Cursor cursor = null;
		try {
			cursor = competitionTeamData.queryAllTeams(prefs.getComp());
			List<CompetitionTeam> teams = CompetitionTeamDataSource.getList(cursor);
			String list = "";
			int i;
			for(i=0;i<teams.size()-1;++i) {
				list += teams.get(i).getTeam().getId()+",";
			}
			list += teams.get(i).getTeam().getId();
			Log.d(TAG, list);
			int year = prefs.getYear();
			switch(year) {
				case 2013:
					get2013Data(list);
			}
		} catch (OurAllianceException e) {
			e.printStackTrace();
			cancel = true;
		} catch (SQLException e) {
			e.printStackTrace();
			cancel = true;
		} catch (IOException e) {
			e.printStackTrace();
			cancel = true;
		} finally {
			if(null!=cursor) {
				try { cursor.close(); } catch (Exception e) {
                    Log.d(TAG,"unknown error",e);
                }
			}
		}
		if(cancel) {
			this.cancel(true);
		}
	}
	
	public void get2013Data(String list) throws OurAllianceException, SQLException, IOException {
		Cursor cursor = null;
		try {
			cursor = teamScouting2013Data.queryAllTeams(list);
			List<TeamScouting2013> scouting = TeamScouting2013DataSource.getList(cursor);
			List<String[]> data = new ArrayList<String[]>();
			for(TeamScouting2013 each : scouting) {
				data.add(each.toStringArray());
			}
			exportToCSV(data);
		} finally {
			if(null!=cursor) {
				try { cursor.close(); } catch (Exception e) {
                    Log.d(TAG,"unknown error",e);
                }
			}
		}
	}
	
	public boolean exportToCSV(List<String[]> data) throws IOException {
		FileWriter file = null;
		try {
			file = new FileWriter(filename);
			writer = new CSVWriter(file, ',');
			writer.writeAll(data);
		} finally {
			if(null!=writer) {
				try {
					writer.flush();
					writer.close();
				} catch (Exception e) {
                    Log.d(TAG,"unknown error",e);
                }
			}
			if(null!=file) {
				try {
					file.flush();
					file.close();
				} catch (Exception e) {
                    Log.d(TAG,"unknown error",e);
                }
			}
		}
		return true;
	}
	
	@Override
	protected void onPreExecute() {
		this.setTitle("Export data");
		if(null!=filename) {
			super.onPreExecute();
		} else {
			this.cancel(true);
		}
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		getData();
		setStatus("Finished");
		return true;
	}

	@Override
    protected void onPostExecute(Boolean result) {
		getDialog().dismiss();
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
    }
}
