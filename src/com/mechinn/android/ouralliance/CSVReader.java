package com.mechinn.android.ouralliance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {
	private BufferedReader inputFile;
	private String delimeter;
	
	public CSVReader(File input, String delim) throws FileNotFoundException {
		inputFile = new BufferedReader(new FileReader(input));
		delimeter = delim;
	}
	
	public List<String[]> readAll() throws IOException {
		List<String[]> table = new ArrayList<String[]>();
		String line;
		while(!(line = inputFile.readLine()).equals("")) {
			table.add(line.split(delimeter));
		}
		return table;
	}
	
	public void close() throws IOException {
		inputFile.close();
	}
}
