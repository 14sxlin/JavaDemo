package linsixin.demo.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import zip.ZipPackage;
import zip.ZipPackage.IllegalFileName;

public class TestZip {

	String targetZip = "new_file.zip";
	
	String []files = new String[]{
		"C:\\Users\\simba.lin\\Pictures\\1.PNG",
		"C:\\Users\\simba.lin\\Pictures\\2.PNG"
	};
	
	ZipPackage zip = new ZipPackage(files);
	@Test
	public void testDumpZip() throws IllegalFileName, IOException {
		zip.dump(targetZip);	
		File file = new File(targetZip);
		assertEquals(true, file.exists());
	}

}
