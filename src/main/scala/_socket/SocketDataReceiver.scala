package _socket

trait SocketDataReceiver {

  def receiveOnce():Unit

  def receiveInfinity():Unit

  def close():Unit
}