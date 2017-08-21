package _socket

trait SocketDataSender {

  def sendData(data:String):Unit

  def close():Unit
}