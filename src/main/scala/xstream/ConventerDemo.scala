package xstream

import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.io.xml.DomDriver

object ConventerDemo extends App {

  val student = new Student
  student.firstName = "xin"
  student.lastName = "sparrow"

  val xstream = new XStream(new DomDriver)
  val stuConventer = new StudentConventor
  xstream.registerConverter(stuConventer)
  val xml  = xstream.toXML(student)

  print(xml)
  assert(xml.contains("<name>"))


  val newStudent = xstream.fromXML(xml).asInstanceOf[Student]
  println(s"new firstname : ${newStudent.firstName}")
  println(s"new lastname  : ${newStudent.lastName}")
//  assert(newStudent.eq(student))

}