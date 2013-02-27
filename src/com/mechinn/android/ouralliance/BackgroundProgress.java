package com.mechinn.android.ouralliance;

import com.mechinn.android.ouralliance.view.LoadingDialogFragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public abstract class BackgroundProgress extends AsyncTask<Void, Object, Boolean> {
	public static final String TAG = BackgroundProgress.class.getSimpleName();
	public static final int INDETERMINATE = -1;
	public static final int NORMAL = 0;
	public static final int FLAG_SETUP = 0;
	public static final int FLAG_EXPORT = 1;
	private String title;
	private int flag;

	private LoadingDialogFragment dialog;
	private FragmentManager fragmentManager;
	private Integer progressFlag;
	private Integer primary;
    private Integer progressTotal;
	private Integer version;
	private CharSequence status;
    private Listener listener;

    public interface Listener {
        public void complete(int flag);
        public void cancelled(int flag);
    }
	
	public BackgroundProgress(Activity activity, int flag) {
        try {
        	listener = (Listener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement "+TAG+".Listener");
        }
        this.flag = flag;
		title = "";
		this.fragmentManager = activity.getFragmentManager();
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
		progressFlag = NORMAL;
		primary = 0;
		progressTotal = 100;
		status = "Loading...";
		dialog = new LoadingDialogFragment();
		Bundle dialogArgs = new Bundle();
		if(null!=title) {
			dialogArgs.putCharSequence(LoadingDialogFragment.TITLE, title);
		}
		dialog.setArguments(dialogArgs);
        dialog.show(fragmentManager, "Setup Our Alliance");
	}
	
	@Override
	protected void onProgressUpdate(Object... progress) {
		int progressFlag = (Integer)progress[0];
		int primary = (Integer)progress[1];
		int total = (Integer)progress[2];
		Integer version = (Integer)progress[3];
		if(null!=version) {
			dialog.setProgressStatus(version+": "+((String)progress[4]));
		} else {
			dialog.setProgressStatus((String)progress[4]);
		}
		switch(progressFlag) {
			case INDETERMINATE:
				dialog.setProgressIndeterminate();
			case NORMAL:
			default:
				dialog.setProgressPercent(primary,total);
		}
    }
	
	@Override
	protected void onCancelled(Boolean result) {
		if(null!=dialog) {
			dialog.dismiss();
		}
		listener.cancelled(flag);
	}

	@Override
    protected void onPostExecute(Boolean result) {
		dialog.dismiss();
		listener.complete(flag);
    }
	
	protected void setProgressFlag(int progressFlag) {
		this.progressFlag = progressFlag;
        publishProgress(new Object[]{progressFlag, primary, progressTotal, version, status});
	}
	
	protected void increasePrimary() {
		setProgressFlag(NORMAL);
        publishProgress(new Object[]{progressFlag, ++primary, progressTotal, version, status});
	}
	
	protected void increaseVersion() {
        publishProgress(new Object[]{progressFlag, primary, progressTotal, (++version), status});
	}
	
	protected void setTotal(int total) {
		setProgressFlag(NORMAL);
		this.primary = 0;
		this.progressTotal = total;
        publishProgress(new Object[]{progressFlag, primary, progressTotal, version, status});
	}
	
	protected void setStatus(CharSequence status) {
		this.status = status;
		Log.d(TAG, status.toString());
        publishProgress(new Object[]{progressFlag, primary, progressTotal, version, status});
	}
}
