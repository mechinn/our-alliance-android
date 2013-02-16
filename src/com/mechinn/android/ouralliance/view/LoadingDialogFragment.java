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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoadingDialogFragment extends DialogFragment {
	private static final String TAG = "LoadingDialogFragment";
	public static final String TITLE = "title";
	public static final String MAX = "max";
    private View dialog;
    private Setup setup;
    private ProgressBar progress;
    private TextView status;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    }
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
		setRetainInstance(true);
		dialog = inflater.inflate(R.layout.loading, container, false);
		progress = (ProgressBar) dialog.findViewById(R.id.progressBar);
		progress.setMax(this.getArguments().getInt(MAX,100));
        status = (TextView) dialog.findViewById(R.id.status);
		return dialog;
	}

	@Override
	public void onStart () {
		super.onStart();
		this.getDialog().setTitle(this.getArguments().getCharSequence(TITLE,""));
		this.getDialog().setCancelable(false);
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
	public void setProgressPercent(int primary) {
		progress.setIndeterminate(false);
    	progress.setProgress(primary);
    }
	public void setProgressStatus(CharSequence statusText) {
		status.setText(statusText);
	}
}
