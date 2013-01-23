package com.mechinn.android.ouralliance.v2013;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;

public class AddTeams implements LoaderCallbacks<Cursor> {
	private static final int INITALIZETEAMS = 100000;
	public AddTeams(Context context) {
		
	}

	public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
		switch(id) {
			case INITALIZETEAMS:
				return seasonData.get(2013);
			default:
				return null;
		}
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		// TODO Auto-generated method stub
		
	}

	public void onLoaderReset(Loader<Cursor> loader) {
		// TODO Auto-generated method stub
		
	}
}
