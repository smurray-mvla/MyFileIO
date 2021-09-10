package myfileio;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(OrderAnnotation.class)
class MyFileIOTest {
	MyFileIO fio = new MyFileIO();

	String[] first10 = {"mupnbhpirr","vsjjgiavuz","ubmugngmiw","mjhktqcwgj","wjkzgzccak"};
	
	private static File fd;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		for (int i = 0; i <5; i++) {
			fd = new File("output/.testcase"+i);
			fd.delete();
			fd = new File("output/.test_"+i);
			fd.delete();
		}
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	@Order(1)
	void testGetFileHandle() {
		System.out.println("\n\nTesting getFileHandle:");
		fd = fio.getFileHandle("");
		System.out.print("   Testing File Handle with empty name:");
		assertTrue("".equals(fd.getName()));
		assertTrue(fd.exists() == false);
		System.out.println(" PASSED");
		System.out.print("  Testing Filehandle on valid file: ");
		fd = fio.getFileHandle("output/.readme");
		assertTrue(".readme".equals(fd.getName()));
		assertTrue(fd.exists());
		assertTrue(fd.length() >= 63);
		assertTrue(fd.canRead());
		assertTrue(fd.canWrite());
		assertTrue(fd.isFile());
		if (File.separatorChar == '\\') {
			assertTrue("output\\.readme".equals(fd.getPath()));
			
		} else {
			assertTrue("output/.readme".equals(fd.getPath()));
		}
		System.out.println(" PASSED");
	}

	@Test
	@Order(2)
	void testCreateEmptyFile() {
		String fname;
		File fd;
		System.out.println("\n\nTesting createEmptyFile:");
		for (int i = 0; i < 5 ; i++) {
			fname = "output/.testcase"+i;
			System.out.print("   Attempting to create empty file "+fname);
			fd = fio.getFileHandle(fname);
			assertFalse(fd.exists());
			assertTrue(fd.length() == 0);
			assertTrue(fd.canRead() == false);
			assertTrue(fd.canWrite() == false);
			assertTrue(fd.isFile() == false);
			assertTrue(fio.createEmptyFile(fname));
			assertTrue(fd.exists() == true);
			assertTrue(fd.length() == 0);
			assertTrue(fd.canRead() == true);
			assertTrue(fd.canWrite() == true);
			assertTrue(fd.isFile() == true);
			assertFalse(fio.createEmptyFile(fname));
			System.out.println(" PASSED");
		}			
	} 
	
	@Test
	@Order(3)
	void testDeleteFile() {
		String fname;
		File fd;
		System.out.println("\n\nTesting deleteFile:");
		for (int i = 0; i < 5 ; i++) {
			fname = "output/.testcase"+i;
			fd = fio.getFileHandle(fname);
			if (!fd.exists()) {
				assertTrue(fio.createEmptyFile(fname));
			}
			System.out.print("   Attempting to delete file "+fname);
			assertTrue(fd.exists());
			assertTrue(fd.length() == 0);
			assertTrue(fd.canRead() == true);
			assertTrue(fd.canWrite() == true);
			assertTrue(fd.isFile() == true);
			assertTrue(fio.deleteFile(fname));
			assertTrue(fd.exists() == false);
			assertTrue(fd.length() == 0);
			assertTrue(fd.canRead() == false);
			assertTrue(fd.canWrite() == false);
			assertTrue(fd.isFile() == false);
			assertFalse(fio.deleteFile(fname));
			System.out.println(" PASSED");
		}			
	} 
	
	@Test
	@Order(4)
	void testCheckTextFile()  {
		System.out.println("\n\nTesting checkTextFile");

		File fd;
		fd = fio.getFileHandle("");
		System.out.print("   Testing empty file name: ");
		assertEquals(MyFileIO.EMPTY_NAME,fio.checkTextFile(fd, true));
		assertEquals(MyFileIO.EMPTY_NAME,fio.checkTextFile(fd, false));
		System.out.print(" PASSED\n   Testing detection of NOT_A_FILE: ");
		fd = fio.getFileHandle("output");
		assertEquals(MyFileIO.NOT_A_FILE,fio.checkTextFile(fd, true));
		assertEquals(MyFileIO.NOT_A_FILE,fio.checkTextFile(fd, false));
		System.out.print(" PASSED\n   Testing non-existent file (READ_EXIST_NOT or FILE_OK): ");
		fd = fio.getFileHandle("output/.testcase1");
		assertEquals(MyFileIO.READ_EXIST_NOT,fio.checkTextFile(fd,true));
		assertEquals(MyFileIO.FILE_OK,fio.checkTextFile(fd,false));
		System.out.print(" PASSED\n   Testing detection of Zero Length File ");
		fio.createEmptyFile("output/.testcase1");
		fd = fio.getFileHandle("output/.testcase1");
		assertEquals(MyFileIO.READ_ZERO_LENGTH,fio.checkTextFile(fd,true));
		assertEquals(MyFileIO.WRITE_EXISTS,fio.checkTextFile(fd,false));
		assertTrue(fio.deleteFile("output/.testcase1"));
		System.out.print(" PASSED\n   Testing detection of Access Issues: ");
		fd = fio.getFileHandle("output/.readme");
		if (fd.setReadable(false)) { 
			assertEquals(MyFileIO.NO_READ_ACCESS,fio.checkTextFile(fd,true));
			fd.setReadable(true);
		}
		if (fd.setWritable(false)) { 
			assertEquals(MyFileIO.NO_WRITE_ACCESS,fio.checkTextFile(fd,false));
			fd.setWritable(true);
		}
		System.out.print(" PASSED\n   Testing FILE_OK for read,WRITE_EXIST for write: ");
		assertEquals(MyFileIO.FILE_OK,fio.checkTextFile(fd,true));
		assertEquals(MyFileIO.WRITE_EXISTS,fio.checkTextFile(fd,false));
		System.out.println(" PASSED");
	}
	
	@Test
	@Order(5)
	void testOpenFileReader() {
		// this should not exist
		System.out.println("\n\nTesting openFileReader:");
		String fname = "output/.testcase1";
		System.out.print("   Testing non-existent file: ");
		File fd = new File(fname);
		FileReader fr = fio.openFileReader(fd);
		assertNull(fr);
		System.out.println(" PASSED");
		int size = 100;
		System.out.println("   Checking the integrity of the data/test_* files");
		for (int i=0; i < 5; i++ ) {
			String rdStr = "";
			fd = fio.getFileHandle("data/test_"+i);
			System.out.print("      File data/test_"+i+" is ");
			assertTrue(fd.exists());
			assertTrue(fd.length() >= (size));
			assertTrue(fd.length() <= (size+10));
			fr = fio.openFileReader(fd);
			assertTrue(fr != null);
			for (int j = 0; j < 10; j++) {
				try {
					rdStr += (char) fr.read();
				}
				catch (IOException e) {
					System.out.println("Caught an IOException: ");
					e.printStackTrace();
				}
			}
			assertTrue(first10[i].equals(rdStr));
			System.out.println(" OKAY");
			size *=10;
		}
		
	} 
	
	@Test
	@Order(6)
	void testOpenBufferedReader() {
		// this should not exist
		System.out.println("\n\nTesting openBufferedReader:");
		String fname = "output/.testcase1";
		System.out.print("   Testing non-existent file: ");
		File fd = new File(fname);
		BufferedReader br = fio.openBufferedReader(fd);
		assertNull(br);
		System.out.println(" PASSED");
		int size = 100;
		System.out.println("   Checking the integrity of the data/test_* files");
		for (int i=0; i < 5; i++ ) {
			String rdStr = "";
			fd = fio.getFileHandle("data/test_"+i);
			System.out.print("      File data/test_"+i+" is ");
			assertTrue(fd.exists());
			assertTrue(fd.length() >= (size));
			assertTrue(fd.length() <= (size+10));
			br = fio.openBufferedReader(fd);
			assertTrue(br != null);
			for (int j = 0; j < 10; j++) {
				try {
					rdStr += (char) br.read();
				}
				catch (IOException e) {
					System.out.println("Caught an IOException: ");
					e.printStackTrace();
				}
			}
			assertTrue(first10[i].equals(rdStr));			
			System.out.println(" OKAY");
			size *=10;
		}
	} 
	
	@Test
	@Order(7)
	void testOpenFileWriter() {
		// this should not exist
		File fwd;
		File frd;
		int size = 100;
		System.out.println("\n\nTesting openFileWriter:");
		System.out.println("   Copying data/test_* files to output");
		for (int i=0; i < 5; i++) {
			frd = fio.getFileHandle("data/test_"+i);
			fwd = fio.getFileHandle("output/.test_"+i);
			System.out.print("      Copying data/test_"+i+" to output/.test_"+i);
			assertTrue(frd.exists());
			assertFalse(fwd.exists());
			FileReader fr = fio.openFileReader(frd);
			FileWriter fw = fio.openFileWriter(fwd);
			for (int j=0; j < frd.length(); j++) {
				try {
					char aChar = (char) fr.read();
					fw.write(aChar);
				}
				catch (IOException e) {
					System.out.println("IO Exception occured");
					e.printStackTrace();
				}
			}
			fio.closeFile(fr);
			fio.closeFile(fw);
			System.out.println(" PASSED");
		}
		System.out.println("   Checking integrity of copied files:");
		for (int i=0; i < 5; i++ ) {
			String rdStr = "";
			System.out.print("      File output/.test_"+i+" is ");
			fd = fio.getFileHandle("output/.test_"+i);
			assertTrue(fd.exists());
			assertTrue(fd.length() >= (size));
			assertTrue(fd.length() <= (size+10));
			FileReader fr = fio.openFileReader(fd);
			assertTrue(fr != null);
			for (int j = 0; j < 10; j++) {
				try {
					rdStr += (char) fr.read();
				}
				catch (IOException e) {
					System.out.println("Caught an IOException: ");
					e.printStackTrace();
				}
			}
			assertTrue(first10[i].equals(rdStr));
			fio.closeFile(fr);
			fd.delete();
			assertFalse(fd.exists());
			size *=10;
			System.out.println(" OKAY");
		}
	} 
	
	@Test
	@Order(8)
	void testOpenBufferedWriter() {
		// this should not exist
		File bwd;
		File brd;
		int size = 100;
		System.out.println("\n\nTesting BufferedFileWriter:");
		System.out.println("   Copying data/test_* files to output");
		for (int i=0; i < 5; i++) {
			brd = fio.getFileHandle("data/test_"+i);
			bwd = fio.getFileHandle("output/.test_"+i);
			System.out.print("      Copying data/test_"+i+" to output/.test_"+i);
			assertTrue(brd.exists());
			assertFalse(bwd.exists());
			BufferedReader br = fio.openBufferedReader(brd);
			BufferedWriter bw = fio.openBufferedWriter(bwd);
			for (int j=0; j < brd.length(); j++) {
				try {
					char aChar = (char) br.read();
					bw.write(aChar);
				}
				catch (IOException e) {
					System.out.println("IO Exception occured");
					e.printStackTrace();
				}
			}
			fio.closeFile(br);
			fio.closeFile(bw);
			System.out.println(" PASSED");
		}
		System.out.println("   Checking integrity of copied files:");
		for (int i=0; i < 5; i++ ) {
			String rdStr = "";
			System.out.print("      File output/.test_"+i+" is ");
			fd = fio.getFileHandle("output/.test_"+i);
			assertTrue(fd.exists());
			assertTrue(fd.length() >= (size));
			assertTrue(fd.length() <= (size+10));
			BufferedReader br = fio.openBufferedReader(fd);
			assertTrue(br != null);
			for (int j = 0; j < 10; j++) {
				try {
					rdStr += (char) br.read();
				}
				catch (IOException e) {
					System.out.println("Caught an IOException: ");
					e.printStackTrace();
				}
			}
			assertTrue(first10[i].equals(rdStr));
			fio.closeFile(br);
			fd.delete();
			assertFalse(fd.exists());
			size *=10;
			System.out.println(" OKAY");
		}
	} 
	
}
