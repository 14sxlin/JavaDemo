package xstream

import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.io.xml.StaxDriver

object XStreamDemo extends App{
  val xstream = new XStream(new StaxDriver)

//  val entity = new Entity("sparrowxin",11,"happy man")
  val entity = new Entity
  entity.name = "sparrowxin"
  entity.age = 12
  entity.desc = "happy man"

  val xml = xstream.toXML(entity)

  println(xml)

  val newEntity = xstream.fromXML(xml).asInstanceOf[Entity]
  assert(entity.name == newEntity.name)

}