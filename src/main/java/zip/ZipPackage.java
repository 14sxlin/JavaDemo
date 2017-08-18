package zip;

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


    public ZipPackage(final String srcfiles[]) {
        this.srcfiles = srcfiles;
    }


    public void setSrcfiles(final String[] srcfiles) {
        this.srcfiles = srcfiles;
    }

    public void dump(final String targetZip) throws IllegalFileName, IOException{
        if(srcfiles == null || srcfiles.length == 0
                || targetZip == null) {
            return;
        }
        if( ! fileNameLegal(targetZip)) {
            throw new IllegalFileName(targetZip);
        }

        createNewZip(targetZip);
        doPackage(targetZip);

    }

    private boolean fileNameLegal(final String file) {
        if(file == null || file.contains(" ")) {
            return false;
        }
        return true;
    }

    private void createNewZip(final String targetZip) throws FileNotFoundException {
        zipOutputStream = new ZipOutputStream(
                new FileOutputStream(targetZip));
    }


    private void doPackage(final String targetZip) throws IOException {
        zipOutputStream = new ZipOutputStream(new FileOutputStream(targetZip));
        for(final String srcfile : srcfiles) {
            addFileItem2Zip(srcfile);
        }
        zipOutputStream.close();
    }

    private void addFileItem2Zip(final String srcfilePath) throws IOException{
        final ZipEntry file2Zip = new ZipEntry(pathToFileName(srcfilePath));
        zipOutputStream.putNextEntry(file2Zip);
        transFileDataIntoEntry(srcfilePath);
        zipOutputStream.closeEntry();
    }

    private void transFileDataIntoEntry(final String file) throws IOException {
        final BufferedInputStream fileInput =
                new BufferedInputStream(new FileInputStream(file));
        final byte[] b = new byte[1024];
        while (fileInput.read(b, 0, 1024) != -1) {
            zipOutputStream.write(b, 0, 1024);
        }
        fileInput.close();
    }


    private String pathToFileName(final String path) {
        if(path == null) {
            return null;
        }
        final int index = path.lastIndexOf("\\");
        if(index == -1) {
            return path;
        }
        return path.substring(index+1);
    }

    @SuppressWarnings("serial")
    public class IllegalFileName extends RuntimeException{
        private final String illegalName;

        public IllegalFileName(final String illegalName) {
            this.illegalName = illegalName;
        }

        @Override
        public String getMessage() {
            return super.getMessage() + "r\n '"+illegalName
                    + "' is not a legal file name";
        }



    }

}
