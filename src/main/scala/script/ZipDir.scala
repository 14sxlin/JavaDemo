package script

import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

object ZipDir extends App {

  if(args.length==2)
  {
    val srcDir = args(0)
    val targetDir = args(1)

    def dump(files:Array[String],targetZip:String){
      val zipOutput = new ZipOutputStream(new FileOutputStream(targetZip))
      files foreach putFileIntoZip(zipOutput)
      zipOutput.close()
    }

    def putFileIntoZip(zipOutput:ZipOutputStream)(file:String){
      val dirOrFile = new File(file)
      if(dirOrFile.isDirectory()){
        val files = dirOrFile.listFiles()
        files map printAbsPathAndGetIt foreach
            putFileIntoZip(zipOutput)
      }else{
        zipOutput.putNextEntry(new ZipEntry(new File(file).getName))
        val fileInput =
                    new BufferedInputStream(new FileInputStream(file));
        val b = new Array[Byte](1024);
        while (fileInput.read(b, 0, 1024) != -1) {
          zipOutput.write(b, 0, 1024);
        }
        fileInput.close();
        zipOutput.closeEntry();
      }
    }

    def zipDir(dirPath:String) = {
      val dir = new File(dirPath)
      if(dir.exists()&&dir.isDirectory())
      {
        val files = dir.list().map(file => dirPath+"\\"+file)
        dump(files, s"$targetDir\\${dir.getName}.zip")
      }
    }

    def printAbsPathAndGetIt(dir:File) = {
      val absPath = dir.getAbsolutePath
      println(absPath)
      absPath
    }



    val dir = new File(srcDir)
    if(dir.isDirectory()){
      val filesAndDirs = dir.listFiles()
      val dirs = filesAndDirs filterNot(_.isFile())
      if(!dirs.isEmpty){
        dirs map printAbsPathAndGetIt foreach zipDir
      }

      val files = filesAndDirs filter(_.isFile)
      if(!files.isEmpty){
        dump(files map printAbsPathAndGetIt,s"$targetDir\\others.zip")
      }
    }
    println("package success")
  }else{
    println("param srcDir targetDir")
  }

}