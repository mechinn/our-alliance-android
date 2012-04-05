package com.mechinn.android.ouralliance;

import java.util.ArrayList;

public class SchemaArray extends ArrayList<String> {
	private static final long serialVersionUID = -4613168368005754083L;
	
	public SchemaArray(String[] s) {
		super();
		for(String each : s) {
			add(each);
		}
	}
	
	public SchemaArray(String[] s1, String[] s2) {
		super();
		for(String each : s1) {
			add(each);
		}
		for(String each : s2) {
			add(each);
		}
	}

	@Override
	public String toString() {
		String out = "";
		for(int i=0;i<size()-1;++i) {
			out += get(i)+",";
		}
		out += get(size()-1);
		return out;
	}
	
	@Override
	public String[] toArray() {
		String[] out = new String[size()];
		for(int i=0;i<size();++i) {
			out[i] = get(i);
		}
		return out;
	}
}
