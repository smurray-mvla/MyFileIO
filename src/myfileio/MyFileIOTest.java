package myfileio;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MyFileIOTest {
	MyFileIO fio = new MyFileIO();
	File fd;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
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
	void testGetFileHandle() {
		fd = fio.getFileHandle("");
		System.out.println("Testing File Handle with empty name:");
		assertTrue("".equals(fd.getName()));
		assertTrue(fd.exists() == false);
		fd = fio.getFileHandle("output/.readme");
		assertTrue(".readme".equals(fd.getName()));
		assertTrue(fd.exists());
	}

	@Test
	void testCreateEmptyFile() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	void testDeleteFile() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	void testCheckTextFile() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	void testOpenFileReader() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	void testOpenFileWriter() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	void testOpenBufferedReader() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	void testOpenBufferedWriter() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	void testCloseFileFileReader() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	void testCloseFileFileWriter() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	void testCloseFileBufferedReader() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	void testCloseFileBufferedWriter() {
		fail("Not yet implemented"); // TODO
	}

}
