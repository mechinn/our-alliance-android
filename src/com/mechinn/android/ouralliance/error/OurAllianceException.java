package com.mechinn.android.ouralliance.error;

public class OurAllianceException extends Exception {
	public static final String TAG = OurAllianceException.class.getName();
	private static final long serialVersionUID = -3613891161907051575L;
	
	public OurAllianceException() {
		this("Exception");
	}
	public OurAllianceException(String msg) {
		super(msg);
	}
	public OurAllianceException(String msg, Throwable t) {
		super(msg,t);
	}
	public OurAllianceException(String tag, String msg) {
		super(buildMsg(tag,msg));
	}
	public OurAllianceException(String tag, String msg, Throwable t) {
		super(buildMsg(tag,msg),t);
	}
	private static String buildMsg(String tag, String msg) {
		return tag+" | "+msg;
	}
}
