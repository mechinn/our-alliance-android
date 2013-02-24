package com.mechinn.android.ouralliance.widget;

import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class UncheckableRadioGroupOnCheckedChangeListener implements OnCheckedChangeListener {
	public final static String TAG = UncheckableRadioGroupOnCheckedChangeListener.class.getName();

	public void onCheckedChanged(RadioGroup group, int checkedId) {
		UncheckableRadioButton uncheckableRadioButton;
		for(int i=0;i<group.getChildCount();++i) {
			uncheckableRadioButton = (UncheckableRadioButton) group.getChildAt(i);
			uncheckableRadioButton.setWasChecked(false);
		}
	}

}
