package com.mechinn.android.ouralliance.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioGroup;

public class UncheckableRadioGroup extends RadioGroup {
	public final static String TAG = UncheckableRadioGroup.class.getSimpleName();

	public UncheckableRadioGroup(Context context) {
		super(context);
		this.setOnCheckedChangeListener(new UncheckableRadioGroupOnCheckedChangeListener());
	}
	public UncheckableRadioGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setOnCheckedChangeListener(new UncheckableRadioGroupOnCheckedChangeListener());
	}
	
	public void programaticallyCheck(int id) {
		super.check(id);
		UncheckableRadioButton uncheckableRadioButton;
		for(int i=0;i<this.getChildCount();++i) {
			uncheckableRadioButton = (UncheckableRadioButton) this.getChildAt(i);
			uncheckableRadioButton.setWasChecked(false);
		}
		if(id>0) {
			uncheckableRadioButton = (UncheckableRadioButton) this.findViewById(id);
			uncheckableRadioButton.setWasChecked(true);
		}
	}
	
	public void setOnCheckedChangeListener(UncheckableRadioGroupOnCheckedChangeListener listener) {
		super.setOnCheckedChangeListener(listener);
	}
}
