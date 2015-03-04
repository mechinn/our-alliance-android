package com.mechinn.android.ouralliance.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import timber.log.Timber;

public abstract class GalleryViewPager extends ViewPager {
    public static final String TAG = "GalleryViewPager";

	public GalleryViewPager(Context context) {
		super(context);
	}

	public GalleryViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public boolean galleryTouchEvent(int width, MotionEvent ev) {
		int total = this.getAdapter().getCount();
		Timber.d("total: " + total);
		int current = this.getCurrentItem();
		Timber.d("current: "+current);
		int visible  = width/(getWidth()+getPageMargin());
		Timber.d("width: "+this.getWidth());
		Timber.d("visible: "+visible);
        return current + visible < total && dispatchTouchEvent(ev);
    }
}
