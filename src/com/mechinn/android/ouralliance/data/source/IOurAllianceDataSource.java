package com.mechinn.android.ouralliance.data.source;


import android.content.CursorLoader;
import android.net.Uri;

import com.mechinn.android.ouralliance.data.AOurAllianceData;
import com.mechinn.android.ouralliance.error.OurAllianceException;

public interface IOurAllianceDataSource<A extends AOurAllianceData> {
	public Uri insert(A data);
	
	public int update(A data) throws OurAllianceException;

	public int delete(A data) throws OurAllianceException;
	
	public CursorLoader get(Uri uri);
	
	public CursorLoader get(A id);
	
	public CursorLoader get(long id);

	public CursorLoader getAll();
}
