package com.mechinn.android.ouralliance;

import com.mechinn.android.ouralliance.fragment.LoadingDialogFragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

public abstract class BackgroundProgress extends AsyncTask<Void, Object, Boolean> {
    public static final String TAG = "BackgroundProgress";
	public static final int INDETERMINATE = -1;
	public static final int NORMAL = 0;
	public static final int FLAG_SETUP = 0;
	public static final int FLAG_EXPORT = 1;
    public static final int FLAG_COMPETITION_LIST = 2;
	private String title;
	private int flag;
    private Prefs prefs;
	private LoadingDialogFragment dialog;
	private FragmentManager fragmentManager;
	private Integer progressFlag;
	private Integer primary;
    private Integer progressTotal;
	private Integer version;
	private CharSequence status;
    private Listener listener;
    private FragmentActivity activity;
    private boolean complete;

    public interface Listener {
        public void complete(int flag);
        public void cancelled(int flag);
    }
	
	public BackgroundProgress(FragmentActivity activity, int flag) {
        this.activity = activity;
        this.prefs = new Prefs(activity);
        try {
        	listener = (Listener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement "+TAG+".Listener");
        }
        this.flag = flag;
		title = "";
		this.fragmentManager = activity.getSupportFragmentManager();
        dialog = new LoadingDialogFragment(this);
	}
    public FragmentActivity getActivity() {
        return activity;
    }
	protected String getTitle() {
		return title;
	}
	protected void setTitle(String title) {
		this.title = title;
	}
	protected int getFlag() {
		return flag;
	}
	protected void setFlag(int flag) {
		this.flag = flag;
	}
	protected Integer getVersion() {
		return version;
	}
	protected void setVersion(Integer version) {
		this.version = version;
	}
	public LoadingDialogFragment getDialog() {
		return dialog;
	}
	public void setDialog(LoadingDialogFragment dialog) {
		this.dialog = dialog;
	}
	
	@Override
	protected void onPreExecute() {
        complete = false;
		progressFlag = NORMAL;
		primary = 0;
		progressTotal = 100;
		status = "Loading...";
        dialog.setCancelable(true);
		Bundle dialogArgs = new Bundle();
		if(null!=title) {
			dialogArgs.putCharSequence(LoadingDialogFragment.TITLE, title);
		}
		dialog.setArguments(dialogArgs);
        dialog.show(fragmentManager, "Background Task");
	}
	
	@Override
	protected void onProgressUpdate(Object... progress) {
        Integer progressFlag = (Integer)progress[0];
        Integer primary = (Integer)progress[1];
        Integer total = (Integer)progress[2];
		Integer version = (Integer)progress[3];
        String status = (String)progress[4];
        if(null==status) {
            status = "";
        }
		if(null!=version) {
			dialog.setProgressStatus(version+": "+ status);
		} else {
			dialog.setProgressStatus(status);
		}
        switch (progressFlag) {
            case INDETERMINATE:
                dialog.setProgressIndeterminate();
            default:
                dialog.setProgressPercent(primary, total);
        }
    }
	
	@Override
	protected void onCancelled(Boolean result) {
		listener.cancelled(flag);
	}

	@Override
    protected void onPostExecute(Boolean result) {
        try {
            dialog.dismiss();
        } catch(Exception e) {
            Log.w(TAG,"error dismissing dialog",e);
        }
		listener.complete(flag);
        complete = true;
    }
	
	protected void setProgressFlag(int progressFlag) {
		this.progressFlag = progressFlag;
        publishProgress(progressFlag, primary, progressTotal, version, status);
	}
	
	protected void increasePrimary() {
		setProgressFlag(NORMAL);
        publishProgress(progressFlag, ++primary, progressTotal, version, status);
	}

    protected void setVersion(int version) {
        this.version = version;
        publishProgress(progressFlag, primary, progressTotal, version, status);
    }
	
	protected void increaseVersion() {
        publishProgress(progressFlag, primary, progressTotal, ++version, status);
	}
	
	protected void setTotal(int total) {
		setProgressFlag(NORMAL);
		this.primary = 0;
		this.progressTotal = total;
        publishProgress(progressFlag, primary, progressTotal, version, status);
	}
	
	protected void setStatus(CharSequence status) {
		this.status = status;
		Log.d(TAG, status.toString());
        publishProgress(progressFlag, primary, progressTotal, version, status);
	}
    public Prefs getPrefs() {
        return prefs;
    }
    public boolean isComplete() {
        return complete;
    }
}
