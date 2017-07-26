package linsixin.demo;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipPackage {
	private String[] srcfiles;
	private ZipOutputStream zipOutputStream;
	private String targetZip;
	
	
	public ZipPackage(String srcfiles[]) {
		this.srcfiles = srcfiles;
	}
	

	public void setSrcfiles(String[] srcfiles) {
		this.srcfiles = srcfiles;
	}

	public void dump(String targetZip) throws IllegalFileName, IOException{
		if(srcfiles == null || srcfiles.length == 0 
				|| targetZip == null) {
			return;
		}
		if( ! fileNameLegal(targetZip))
			throw new IllegalFileName(targetZip);
		
		this.targetZip = targetZip;
		createNewZip();
		doPackage();
		
	}
	
	private boolean fileNameLegal(String file) {
		if(file == null || file.contains(" "))
			return false;
		return true;
	}
	
	private void createNewZip() throws FileNotFoundException {
		zipOutputStream = new ZipOutputStream(
				new FileOutputStream(targetZip));
	}
	
	
	private void doPackage() throws IOException {
		zipOutputStream = new ZipOutputStream(new FileOutputStream(targetZip));
		for(String srcfile : srcfiles) {
			addFileItem2Zip(srcfile);
		}
		zipOutputStream.close();
	}
	
	private void addFileItem2Zip(String srcfilePath) throws IOException{
		ZipEntry file2Zip = new ZipEntry(parseFileName(srcfilePath));
		zipOutputStream.putNextEntry(file2Zip);
		readFile(srcfilePath); 
        zipOutputStream.closeEntry();
	}
	
	private void readFile(String file) throws IOException {
		BufferedInputStream fileInput = 
				new BufferedInputStream(new FileInputStream(file));
        byte[] b = new byte[1024];
        while (fileInput.read(b, 0, 1024) != -1) {
        	zipOutputStream.write(b, 0, 1024);
        }
        fileInput.close();
	}	
	
	
	private String parseFileName(String path) {
		if(path == null)
			return null;
		int index = path.lastIndexOf("\\");
		if(index == -1)	
			return path;
		return path.substring(index+1);
	}
	
	@SuppressWarnings("serial")
	public class IllegalFileName extends RuntimeException{
		private String illegalName;
		
		public IllegalFileName(String illegalName) {
			this.illegalName = illegalName;
		}

		@Override
		public String getMessage() {
			return super.getMessage() + "r\n '"+illegalName
					+ "' is not a legal file name";
		}
		
		
		
	}
	
}
