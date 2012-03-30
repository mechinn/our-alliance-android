package com.mechinn.android.ouralliance;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVWriter {
	private FileWriter outputFile;
	private String delimeter;
	public CSVWriter(File output, String delim) throws IOException {
		outputFile = new FileWriter(output);
		delimeter = delim;
	}
	
	public void writeAll(List<String[]> table) throws IOException {
		for(String[] row : table) {
			for(int i = 0;i<row.length-1;++i) {
				outputFile.append(row[i]+delimeter);
			}
			outputFile.append(row[row.length-1]+"\n");
		}
		outputFile.flush();
	}
	
	public void close() throws IOException {
		outputFile.close();
	}
}
