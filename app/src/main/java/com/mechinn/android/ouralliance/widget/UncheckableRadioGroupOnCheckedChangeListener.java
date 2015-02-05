package com.mechinn.android.ouralliance.widget;

import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class UncheckableRadioGroupOnCheckedChangeListener implements OnCheckedChangeListener {
    public static final String TAG = "UncheckableRadioGroupOnCheckedChangeListener";

	public void onCheckedChanged(RadioGroup group, int checkedId) {
		UncheckableRadioButton uncheckableRadioButton;
		for(int i=0;i<group.getChildCount();++i) {
			uncheckableRadioButton = (UncheckableRadioButton) group.getChildAt(i);
			uncheckableRadioButton.setWasChecked(false);
		}
	}

}
