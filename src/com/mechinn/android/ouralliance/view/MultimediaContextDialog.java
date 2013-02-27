package com.mechinn.android.ouralliance.view;

import java.io.File;

import com.mechinn.android.ouralliance.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MultimediaContextDialog extends DialogFragment {
	public static final String TAG = MultimediaContextDialog.class.getSimpleName();
	public static final String IMAGE = "image";
	private String file;
    private View dialog;
    private ProgressBar waiting;
    private ImageView image;
    private Button delete;
    private Button open;
    public interface Listener {
        public void onDeletedImage();
    }
    
    Listener listener;
    
    @Override
    public void onAttach(Activity activity) {
    	super.onAttach(activity);
    	// Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
        	listener = (Listener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString() + " must implement "+TAG+".Listener");
        }
    }
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		dialog = inflater.inflate(R.layout.multimedia_context_menu, null);
		waiting = (ProgressBar) dialog.findViewById(R.id.waiting);
		image = (ImageView) dialog.findViewById(R.id.image);
		file = this.getArguments().getString(IMAGE, "");
		image.setTag(R.string.file, file);
		MultimediaAdapter.setImageBad(file, image);
		waiting.setVisibility(View.GONE);
		image.setVisibility(View.VISIBLE);
		delete = (Button) dialog.findViewById(R.id.delete);
		delete.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				file = file.replaceFirst("file://", "");
				if(new File(file).delete()) {
					Toast.makeText(MultimediaContextDialog.this.getActivity(), "Deleted media", Toast.LENGTH_SHORT).show();
					listener.onDeletedImage();
				} else {
					Toast.makeText(MultimediaContextDialog.this.getActivity(), "Could not delete media", Toast.LENGTH_SHORT).show();
				}
				MultimediaContextDialog.this.dismiss();
			}
		});
		open = (Button) dialog.findViewById(R.id.open);
		open.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				MultimediaAdapter.openMedia(image);
				MultimediaContextDialog.this.dismiss();
			}
		});
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
