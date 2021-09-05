package myfileio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MyFileIO {
	
	/* checkTextFile return values */
	public static final int FILE_OK=0;
	public static final int EMPTY_NAME=1;
	public static final int NOT_A_FILE = 2;
	public static final int READ_EXIST_NOT=3;
	public static final int READ_ZERO_LENGTH=4;
	public static final int NO_READ_ACCESS=5;
	public static final int NO_WRITE_ACCESS=6;
	public static final int WRITE_EXISTS=7;
	
	public File getFileHandle (String filename) {
		return (new File(filename));
	}
	
	public boolean createEmptyFile(String filename) {
	    File fh = getFileHandle(filename);
	    boolean status = false;
	    if ("".equals(filename) || fh.exists())
	    	return false; 
	    try {
	    	status = fh.createNewFile();
	    }
	    catch (IOException e) {
	    	System.out.println("An IOException occurred.");
	    	e.printStackTrace();
	    }
	    catch (SecurityException e) {
	    	System.out.println("A Security Exception occurred.");
	    	e.printStackTrace();
	    }
	    return status;
	}
	
	public boolean deleteFile(String filename) {
	    File fh = getFileHandle(filename);
	    boolean status = false;
	    if ("".equals(filename) || !fh.exists() || !fh.isFile())
	    	return false; 
	    try {
	    	status = fh.delete();
	    }
	    catch (SecurityException e) {
	    	System.out.println("A Security Exception occurred.");
	    	e.printStackTrace();
	    }
	    return status;	
	}	

	public int checkTextFile(File file, boolean read) {
		if ("".equals(file.getName())) 
			return EMPTY_NAME;
		if (read) {
			if (!file.exists())
				return READ_EXIST_NOT;
			if (!file.isFile())
				return NOT_A_FILE;
			if (file.length() == 0)
				return READ_ZERO_LENGTH;
			if (!file.canRead()) 
				return NO_READ_ACCESS;
		} else {
			if (file.exists()) {
				if (!file.canWrite()) 
					return NO_WRITE_ACCESS;
				return (file.isFile() ? WRITE_EXISTS : NOT_A_FILE);
			}
		}
		return FILE_OK;
	}
	
	public FileReader openFileReader(File file) {
		FileReader fr = null;
		try {
			fr = new FileReader(file);
		}
		catch (FileNotFoundException e) {
			System.out.println("ERROR - Did not find file for reading!!");
		}
		return fr;
	}
	
	public FileWriter openFileWriter(File file) {
		FileWriter fw = null;
		try {
			fw = new FileWriter(file);
		}
		catch (IOException e) {
			System.out.println("IO Exception error - could not open file for writing!!");
		}
		return fw;
	}
	
	public BufferedReader openBufferedReader(File file) {
		BufferedReader br = null;
		FileReader fr = openFileReader(file);
		if (fr != null)
			br = new BufferedReader(fr);
		return br;
	}
	
	public BufferedWriter openBufferedWriter(File file) {
		BufferedWriter bw = null;
		FileWriter fw = openFileWriter(file);
		if (fw != null)
			bw = new BufferedWriter(fw);
		return bw;
	}
	
	public void closeFile(FileReader fr) {
		try {
			fr.close();
		}
		catch (IOException e) {
			System.out.println("Attempt to close FileReader failed");
			e.printStackTrace();
		}
	}
	
	public void closeFile(FileWriter fw) {
		try {
			fw.flush();
			fw.close();
		}
		catch (IOException e) {
			System.out.println("Attempt to flush and close FileWriter failed");
			e.printStackTrace();
		}

	}

	public void closeFile(BufferedReader br) {
		try {
			br.close();
		}
		catch (IOException e) {
			System.out.println("Attempt to close BufferedReader failed");
			e.printStackTrace();
		}
	}

	public void closeFile(BufferedWriter bw) {
		try {
			bw.flush();
			bw.close();
		}
		catch (IOException e) {
			System.out.println("Attempt to flush and close BufferedWriter failed");
			e.printStackTrace();
		}
	}

}
