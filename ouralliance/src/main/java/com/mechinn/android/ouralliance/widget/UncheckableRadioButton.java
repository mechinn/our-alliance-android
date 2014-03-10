package com.mechinn.android.ouralliance.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioButton;

public class UncheckableRadioButton extends RadioButton {
    public static final String TAG = "UncheckableRadioButton";
	private boolean wasChecked;
	public UncheckableRadioButton(Context context) {
		super(context);
		this.setOnClickListener(new UncheckableRadioButtonOnClickListener());
	}
	public UncheckableRadioButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setOnClickListener(new UncheckableRadioButtonOnClickListener());
	}
	public UncheckableRadioButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.setOnClickListener(new UncheckableRadioButtonOnClickListener());
	}
	public boolean isWasChecked() {
		return wasChecked;
	}
	public void setWasChecked(boolean wasChecked) {
		this.wasChecked = wasChecked;
	}
	public void setOnClickListener(UncheckableRadioButtonOnClickListener listener) {
		super.setOnClickListener(listener);
	}
}
