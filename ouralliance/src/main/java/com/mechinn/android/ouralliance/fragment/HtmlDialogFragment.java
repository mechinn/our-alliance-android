package com.mechinn.android.ouralliance.fragment;

import com.mechinn.android.ouralliance.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;

public class HtmlDialogFragment extends DialogFragment {
	public static final String TAG = HtmlDialogFragment.class.getSimpleName();
	public static final String HTMLFILE = "htmlFile";
    private View dialog;
    private WebView html;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		setRetainInstance(true);
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		dialog = inflater.inflate(R.layout.html, null);
		html = (WebView) dialog.findViewById(R.id.content);
		String htmlFile = this.getArguments().getString(HTMLFILE);
		Log.d(TAG, htmlFile);
		if(null!=htmlFile) {
			html.loadUrl(htmlFile);
		}
		builder.setView(dialog).setCancelable(false)
			.setPositiveButton("Done", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.dismiss();
				}
			});
		// Create the AlertDialog object and return it
		return builder.create();
	}
}
