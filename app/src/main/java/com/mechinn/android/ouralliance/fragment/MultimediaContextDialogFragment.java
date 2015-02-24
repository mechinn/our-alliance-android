package com.mechinn.android.ouralliance.fragment;

import java.io.File;

import android.app.Activity;
import android.graphics.Point;
import android.support.v4.app.DialogFragment;
import android.view.Display;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.adapter.MultimediaAdapter;
import com.mechinn.android.ouralliance.event.MultimediaDeletedEvent;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import de.greenrobot.event.EventBus;

import static android.widget.ImageView.ScaleType.CENTER_INSIDE;

public class MultimediaContextDialogFragment extends DialogFragment implements Callback {
    public static final String TAG = "MultimediaContextDialogFragment";
	public static final String IMAGE = "image";
	private File file;
    private View dialog;
    private ProgressBar waiting;
    private ImageView image;
    private Button delete;
    private Button open;
    private int width;
    private int height;

    @Override
    public void onSuccess() {
        waiting.setVisibility(View.GONE);
    }

    @Override
    public void onError() {
        waiting.setVisibility(View.GONE);
    }
    
    @Override
    public void onAttach(Activity activity) {
    	super.onAttach(activity);
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = Math.round(size.x*0.9f);
        height = Math.round(size.y*0.9f);
    }
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
//        Picasso.with(getActivity()).setIndicatorsEnabled(true);
        file = (File) this.getArguments().getSerializable(IMAGE);
		dialog = inflater.inflate(R.layout.multimedia_context_menu, null);
        image = (ImageView) dialog.findViewById(R.id.image);
        image.setMaxWidth(width);
        image.setMaxHeight(height);
        image.setTag(R.string.file, file);
        image.setScaleType(CENTER_INSIDE);
        waiting = (ProgressBar) dialog.findViewById(R.id.loading);
        waiting.setVisibility(View.VISIBLE);
        Picasso.with(getActivity())
                .load(file)
                .fit().centerInside()
                .placeholder(R.drawable.ic_empty)
                .error(R.drawable.ic_error)
                .into(image,this);
		delete = (Button) dialog.findViewById(R.id.delete);
		delete.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(file.delete()) {
					Toast.makeText(MultimediaContextDialogFragment.this.getActivity(), "Deleted media", Toast.LENGTH_SHORT).show();
                    EventBus.getDefault().post(new MultimediaDeletedEvent());
				} else {
					Toast.makeText(MultimediaContextDialogFragment.this.getActivity(), "Could not delete media", Toast.LENGTH_SHORT).show();
				}
				MultimediaContextDialogFragment.this.dismiss();
			}
		});
		open = (Button) dialog.findViewById(R.id.open);
		open.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				MultimediaAdapter.openMedia(image);
				MultimediaContextDialogFragment.this.dismiss();
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
