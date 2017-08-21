package _socket

object ClientApp extends App {
  val client = new Client("localhost",Context.port)
  // if use wrong host or port will socket connection reset
  MBeanRegister.register(Context.domain, Context.clientObjName, client)
  client.openCon()
  client.receiveOnce()
}