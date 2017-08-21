package _socket

import javax.management.MBeanServer
import java.lang.management.ManagementFactory

object ServerApp extends App {
  val server = Server(Context.port)
  server.openCon()
  server.readAndWait()
  MBeanRegister.register(Context.domain, Context.serverObjName, server)

}