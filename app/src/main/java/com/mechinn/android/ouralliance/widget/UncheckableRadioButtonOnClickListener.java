package com.mechinn.android.ouralliance.widget;

import android.view.View;
import android.view.View.OnClickListener;

public class UncheckableRadioButtonOnClickListener implements OnClickListener {
    public static final String TAG = "UncheckableRadioButtonOnClickListener";
	public void onClick(View v) {
		UncheckableRadioGroup uncheckableRadioGroup = (UncheckableRadioGroup) v.getParent();
		UncheckableRadioButton uncheckableRadioButton = (UncheckableRadioButton) v;
		if(uncheckableRadioButton.isWasChecked()) {
			uncheckableRadioGroup.clearCheck();
		} else {
			uncheckableRadioButton.setWasChecked(true);
		}
	}

}
