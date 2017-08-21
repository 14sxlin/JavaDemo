package _socket

import java.lang.management.ManagementFactory
import javax.management.ObjectName

object MBeanRegister {

  private val mbeanServer = ManagementFactory.getPlatformMBeanServer

  def register(domian:String,name:String,obj:Object) = {
      val objName = new ObjectName(s"$domian:name=$name")
      mbeanServer.registerMBean(obj, objName)
  }


}