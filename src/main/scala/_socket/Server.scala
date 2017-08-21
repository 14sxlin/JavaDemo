package _socket

import java.net.ServerSocket
import java.io.OutputStream
import java.net.Socket
import org.apache.log4j.Logger
import java.io.InputStream

case class Server(port:Int) extends  ServerMBean {

  val logger = Logger.getLogger(getClass)

  var client : Socket = _
  var out : OutputStream = _
  var in : InputStream = _

  def openCon(){
    val server = new ServerSocket(port)
    logger.info(s"listeneing at $port")
    client = server.accept()
    server.close()
    logger.info("client has connected,server close")
    out = client.getOutputStream
    in = client.getInputStream
  }

  def readAndWait() = {
    val run = new Runnable() {
      def run() {
        val data = new Array[Byte](1024)
        while(true){
          val len = in.read(data)
          logger.info(s"server read : ${new String(data)} \n$len")
        }
      }
    }

    new Thread(run).start()
  }

  override def sendData(data:String):Unit = {
    out.write(data.getBytes)
    out.flush()
  }

  override def sendQuitInfinity() : Unit = {
    sendData(Context.quit)
  }

  override def close():Unit = {
    out.close()
    client.close()
  }

}