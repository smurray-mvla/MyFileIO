import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import myfileio.MyFileIO;

public class BenchmarkFileAccess {
	public static final int NUM_FILES = 5;
	public static final int NUM_ITER = 10;
	public static final int[] CHARS_PER_FILE = {100,1000,10000,100000,1000000};
	private MyFileIO fileIO = new MyFileIO();
	
	private long[][] results = new long[NUM_FILES][NUM_ITER];
	
	private void initResults() {
		for (int fileNum = 0; fileNum < NUM_FILES; fileNum++) {
			for (int iter =0; iter < NUM_ITER; iter++) {
				results[fileNum][iter]=0;
			}
		}
	}
	
	private void benchmarkFileReader() {
		long start, stop;
		initResults();
		System.out.println("Executing FileReader Benchmark");
		for (int fileNum = 0; fileNum < NUM_FILES; fileNum++ ) {
			File fh = fileIO.getFileHandle("data/test_"+fileNum);
			System.out.println("Benchmarking "+fh.getName()+":");
			if (fileIO.checkTextFile(fh, true) == MyFileIO.FILE_OK) {
				long fileLength = fh.length();
				for (int iter = 0; iter < NUM_ITER; iter++) {
					System.out.println("   Iteration: "+iter);
					FileReader fr = fileIO.openFileReader(fh);
					start = System.nanoTime();
					for (int charCnt = 0; charCnt < fileLength; charCnt++) {
						try {
							fr.read();
						}
						catch (IOException e) {
							System.out.println("IO Exception occurred while reading data/text_"+fileNum);
							e.printStackTrace();
						}
					}
					stop = System.nanoTime();
					fileIO.closeFile(fr);
					results[fileNum][iter] = stop - start;
				}
			}
		}
	}
	
	private void benchmarkBufferedReader() {
		long start, stop;
		initResults();
		System.out.println("Executing BufferedReader Benchmark");
		for (int fileNum = 0; fileNum < NUM_FILES; fileNum++ ) {
			File fh = fileIO.getFileHandle("data/test_"+fileNum);
			System.out.println("   Benchmarking "+fh.getName()+":");
			if (fileIO.checkTextFile(fh, true) == MyFileIO.FILE_OK) {
				long fileLength = fh.length();
				for (int iter = 0; iter < NUM_ITER; iter++) {
					System.out.println("      Iteration: "+iter);
					BufferedReader br = fileIO.openBufferedReader(fh);
					start = System.nanoTime();
					for (int charCnt = 0; charCnt < fileLength; charCnt++) {
						try {
							br.read();
						}
						catch (IOException e) {
							System.out.println("IO Exception occurred while reading data/text_"+fileNum);
							e.printStackTrace();
						}
					}
					stop = System.nanoTime();
					fileIO.closeFile(br);
					results[fileNum][iter] = stop - start;
				}
			}
		}
	}
	
	private void benchmarkFileWriter() {
		initResults();
		long start, stop;
		System.out.println("Executing FileWriter Benchmark");
		for (int fileNum = 0; fileNum < NUM_FILES; fileNum++ ) {
			File fh = fileIO.getFileHandle("output/test_"+fileNum);
			System.out.println("Benchmarking "+fh.getName()+":");
			if (fileIO.checkTextFile(fh, false) == MyFileIO.FILE_OK) {
				long fileLength = fh.length();
				for (int iter = 0; iter < NUM_ITER; iter++) {
					System.out.println("   Iteration: "+iter);
					FileWriter fw = fileIO.openFileWriter(fh);
					start = System.nanoTime();
					for (int charCnt = 0; charCnt < CHARS_PER_FILE[fileNum]; charCnt++) {
						try {
							fw.write('A');
						}
						catch (IOException e) {
							System.out.println("IO Exception occurred while reading data/text_"+fileNum);
							e.printStackTrace();
						}
					}
					stop = System.nanoTime();
					fileIO.closeFile(fw);
					fileIO.deleteFile(fh.getPath());
					results[fileNum][iter] = stop - start;
				}
			}
		}
	}
	
	private void benchmarkBufferedWriter() {
		initResults();
		long start, stop;
		System.out.println("Executing BufferedWriter Benchmark");
		for (int fileNum = 0; fileNum < NUM_FILES; fileNum++ ) {
			File fh = fileIO.getFileHandle("output/test_"+fileNum);
			System.out.println("Benchmarking "+fh.getName()+":");
			if (fileIO.checkTextFile(fh, false) == MyFileIO.FILE_OK) {
				long fileLength = fh.length();
				for (int iter = 0; iter < NUM_ITER; iter++) {
					System.out.println("   Iteration: "+iter);
					BufferedWriter bw = fileIO.openBufferedWriter(fh);
					start = System.nanoTime();
					for (int charCnt = 0; charCnt < CHARS_PER_FILE[fileNum]; charCnt++) {
						try {
							bw.write('A');
						}
						catch (IOException e) {
							System.out.println("IO Exception occurred while reading data/text_"+fileNum);
							e.printStackTrace();
						}
					}
					stop = System.nanoTime();
					fileIO.closeFile(bw);
					fileIO.deleteFile(fh.getPath());
					results[fileNum][iter] = stop - start;
				}
			}
		}
	}
	
	private void printResults(String benchmark) {
		String str = "";
		str += "\nBenchmark: "+benchmark+"\n";
		str += "Test File";
		for (int iter = 0; iter < NUM_ITER; iter++) {
			str += ","+iter;
		}
		str += "\n";
		for (int fileNum = 0; fileNum < NUM_FILES; fileNum++) {
			str += "data/test_"+fileNum;
			for (int iter = 0; iter < NUM_ITER; iter++) {
				str += ","+results[fileNum][iter];
			}
			str += "\n";
		}
		System.out.print(str);
		File fh = fileIO.getFileHandle("output/"+benchmark+".csv");
		int status = fileIO.checkTextFile(fh, false);
		if ((status == MyFileIO.FILE_OK) || (status == MyFileIO.WRITE_EXISTS)) {
			BufferedWriter bw = fileIO.openBufferedWriter(fh);
			try {
				bw.write(str);
			}
			catch (IOException e) {
				System.out.println("IO Exception while writing file: "+fh.getPath());
				e.printStackTrace();
			}
			fileIO.closeFile(bw);
		}
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BenchmarkFileAccess bmfa = new BenchmarkFileAccess();
		bmfa.benchmarkFileReader();
		bmfa.printResults("FileReader Benchmark");
		bmfa.benchmarkBufferedReader();
		bmfa.printResults("BufferedReader Benchmark");
		bmfa.benchmarkFileWriter();
		bmfa.printResults("FileWriter Benchmark");
		bmfa.benchmarkBufferedWriter();
		bmfa.printResults("BufferedWriter Benchmark");
	}

}
