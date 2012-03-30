package com.mechinn.android.ouralliance;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class CSVReader {
	private FileReader reader;
	private String delimeter;
	
	public CSVReader(File input, String delim) throws FileNotFoundException {
		reader = new FileReader(input);
		delimeter = delim;
	}
}
