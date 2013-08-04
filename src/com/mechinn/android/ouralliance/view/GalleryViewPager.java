package com.mechinn.android.ouralliance.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public abstract class GalleryViewPager extends ViewPager {
	public static final String TAG = GalleryViewPager.class.getSimpleName();

	public GalleryViewPager(Context context) {
		super(context);
	}

	public GalleryViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public boolean galleryTouchEvent(int width, MotionEvent ev) {
		int total = this.getAdapter().getCount();
		Log.d(TAG,"total: "+total);
		int current = this.getCurrentItem();
		Log.d(TAG,"current: "+current);
		int visible  = width/(getWidth()+getPageMargin());
		Log.d(TAG,"width: "+this.getWidth());
		Log.d(TAG,"visible: "+visible);
		if(current+visible<total) {
			return dispatchTouchEvent(ev);
		}
		return false;
	}
}
