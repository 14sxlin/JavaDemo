package _socket

import java.net.Socket
import org.apache.log4j.Logger
import java.io.InputStream
import scala.collection.mutable.ArrayBuffer
import scala.annotation.tailrec

/**
 * This app aim to find that how should
 * client read data,using read or while(true) read
 */
class Client(host:String,port:Int) extends ClientMBean {

  val logger = Logger.getLogger(getClass)

  var in : InputStream = _

  def openCon() : Unit = {
    val server = new Socket(host,Context.port)
    in =  server.getInputStream
  }


  override def shouldQuitInfinity(flag:String) = {
    flag == Context.quit
  }


  override def receiveOnce():Unit = {
    val total = ArrayBuffer[Byte]()
    var totalLen = 0

    val buffer = new Array[Byte](2)

    @tailrec
    def readUtilBufferNotFull():Unit = {

      val length = in.read(buffer)
      assert(length != -1,"In network socket length will never be -1")

      total ++= buffer.slice(0, length)
      totalLen += length

      logger.info(s"length = $length")
      logger.info(s"data = ${new String(buffer.slice(0, length))}")

      if(length == buffer.length)
        readUtilBufferNotFull()
    }
    //scala's while is ugly. Use @tailrec recursive function
    //it's allowed to define function in function or other place

    readUtilBufferNotFull()
    logger.info(s"total length = $totalLen")
    logger.info(s"total data = ${new String(total.toArray)}")
  }

  override def receiveInfinity():Unit = {
    val data = new Array[Byte](1024)
    var total = 0
    var length = in.read(data)
    var strData : String = null
    while(length != -1 && !shouldQuitInfinity(strData)){
      total += length
      strData = new String(data.slice(0, length))
      logger.info(s"length = $length")
      logger.info(s"data = ${strData}")
      length = in.read(data)
    }

    println(s"finish receive data , Content-Length : $total")
  }

  override def close():Unit = {
    in.close()
  }

}