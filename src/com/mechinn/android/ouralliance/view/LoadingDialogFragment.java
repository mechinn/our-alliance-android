package com.mechinn.android.ouralliance.view;

import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.Setup;
import com.mechinn.android.ouralliance.data.CompetitionTeam;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoadingDialogFragment extends DialogFragment {
	public static final String TAG = LoadingDialogFragment.class.getSimpleName();
	public static final String TITLE = "title";
	public static final String MAX = "max";
    private View dialog;
    private Setup setup;
    private ProgressBar progress;
    private TextView status;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	this.setCancelable(false);
		setRetainInstance(true);
    	super.onCreate(savedInstanceState);
    }
    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance()) {
            getDialog().setDismissMessage(null);
        }
        super.onDestroyView();
    }
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
		dialog = inflater.inflate(R.layout.loading, container, false);
		progress = (ProgressBar) dialog.findViewById(R.id.progressBar);
        status = (TextView) dialog.findViewById(R.id.status);
		return dialog;
	}

	@Override
	public void onStart () {
		super.onStart();
		this.getDialog().setTitle(this.getArguments().getCharSequence(TITLE,""));
		// Disable the back button
		OnKeyListener keyListener = new OnKeyListener() {
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if( keyCode == KeyEvent.KEYCODE_BACK){					
					return true;
				}
				return false;
			}
 
		
		};
		this.getDialog().setOnKeyListener(keyListener);
		this.getDialog().setCanceledOnTouchOutside(false);
	}
	public void setProgressIndeterminate() {
		progress.setIndeterminate(true);
	}
	public void setProgressPercent(int primary, int total) {
		progress.setIndeterminate(false);
    	progress.setProgress(primary);
    	progress.setMax(total);
    	Log.d(TAG, "percent: "+progress.getProgress()+"/"+progress.getMax());
    }
	public void setProgressStatus(CharSequence statusText) {
		status.setText(statusText);
	}
}
