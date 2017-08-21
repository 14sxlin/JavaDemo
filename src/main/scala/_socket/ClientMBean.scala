package _socket

trait ClientMBean extends SocketDataReceiver{

  def shouldQuitInfinity(flag:String):Boolean
}