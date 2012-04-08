package com.mechinn.android.ouralliance;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import com.mechinn.android.ouralliance.data.Prefs;

import android.app.Activity;
import android.util.Log;

public class FTPConnection {
	private final String TAG = "FTPConnection";
	private Activity activity;
	private Prefs prefs;
	private FTPClient ftp;

	public FTPConnection(Activity act) {
		activity = act;
		prefs = new Prefs(activity);
		ftp = new FTPClient();
	}
	
	public boolean connect() {
		try {
			ftp.connect(prefs.getHost(), prefs.getPort());
			// now check the reply code, if positive mean connection success
	        if (FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
	            // login using username & password
	            boolean status = ftp.login(prefs.getUser(), prefs.getPass());

	            /* Set File Transfer Mode
	             *
	             * To avoid corruption issue you must specified a correct
	             * transfer mode, such as ASCII_FILE_TYPE, BINARY_FILE_TYPE,
	             * EBCDIC_FILE_TYPE .etc. Here, I use BINARY_FILE_TYPE
	             * for transferring text, image, and compressed files.
	             */
	            ftp.setFileType(FTP.BINARY_FILE_TYPE);
	            ftp.enterLocalPassiveMode();

	            return status;
	        }
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean disconnect() {
	    try {
	    	ftp.logout();
	    	ftp.disconnect();
	        return true;
	    } catch (Exception e) {
	        Log.d(TAG, "Error occurred while disconnecting from ftp server.");
	    }

	    return false;
	}
	
	public String getCurrentWorkingDirectory() {
	    try {
	        String workingDir = ftp.printWorkingDirectory();
	        return workingDir;
	    } catch(Exception e) {
	        Log.d(TAG, "Error: could not get current working directory.");
	    }

	    return null;
	}
	
	public boolean changeDirectory(String directory_path) {
	    try {
	        return ftp.changeWorkingDirectory(directory_path);
	    } catch(Exception e) {
	        Log.d(TAG, "Error: could not change directory to " + directory_path);
	    }

	    return false;
	}
	
	public void printFilesList(String dir_path) {
	    try {
	        FTPFile[] ftpFiles = ftp.listFiles(dir_path);
	        int length = ftpFiles.length;

	        for (int i = 0; i < length; i++) {
	            String name = ftpFiles[i].getName();
	            boolean isFile = ftpFiles[i].isFile();

	            if (isFile) {
	                Log.i(TAG, "File : " + name);
	            }
	            else {
	                Log.i(TAG, "Directory : " + name);
	            }
	        }
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public boolean makeDirectory(String new_dir_path) {
	    try {
	        boolean status = ftp.makeDirectory(new_dir_path);
	        return status;
	    } catch(Exception e) {
	        Log.d(TAG, "Error: could not create new directory named " + new_dir_path);
	    }

	 return false;
	}
	
	public boolean removeDirectory(String dir_path) {
	    try {
	        boolean status = ftp.removeDirectory(dir_path);
	        return status;
	    } catch(Exception e) {
	        Log.d(TAG, "Error: could not remove directory named " + dir_path);
	    }

	    return false;
	}
	
	public boolean removeFile(String filePath) {
	    try {
	        boolean status = ftp.deleteFile(filePath);
	        return status;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return false;
	}
	
	public boolean renameFile(String from, String to) {
	    try {
	        boolean status = ftp.rename(from, to);
	        return status;
	    } catch (Exception e) {
	        Log.d(TAG, "Could not rename file: " + from + " to: " + to);
	    }

	    return false;
	}
	
	/**
	 * mFTPClient: FTP client connection object (see FTP connection example)
	 * srcFilePath: path to the source file in FTP server
	 * desFilePath: path to the destination file to be saved in sdcard
	 */
	public boolean download(String srcFilePath, String desFilePath) {
	    boolean status = false;
	    try {
	        FileOutputStream desFileStream = new FileOutputStream(desFilePath);;
	        status = ftp.retrieveFile(srcFilePath, desFileStream);
	        desFileStream.close();

	        return status;
	    } catch (Exception e) {
	        Log.d(TAG, "download failed");
	    }

	    return status;
	}
	
	/**
	 * mFTPClient: FTP client connection object (see FTP connection example)
	 * srcFilePath: source file path in sdcard
	 * desFileName: file name to be stored in FTP server
	 * desDirectory: directory path where the file should be upload to
	 */
	public boolean upload(String srcFilePath, String desFileName, String desDirectory) {
	    boolean status = false;
	    try {
	        FileInputStream srcFileStream = new FileInputStream(srcFilePath);

	        // change working directory to the destination directory
	        if (changeDirectory(desDirectory)) {
	            status = ftp.storeFile(desFileName, srcFileStream);
	        }

	        srcFileStream.close();
	        return status;
	    } catch (Exception e) {
	        Log.d(TAG, "upload failed");
	    }

	    return status;
	}
}
