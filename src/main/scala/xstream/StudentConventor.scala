package xstream

import com.thoughtworks.xstream.converters.Converter
import com.thoughtworks.xstream.io.HierarchicalStreamWriter
import com.thoughtworks.xstream.converters.MarshallingContext
import com.thoughtworks.xstream.io.HierarchicalStreamReader
import com.thoughtworks.xstream.converters.UnmarshallingContext

class StudentConventor extends Converter {

  def canConvert(obj: Class[_]): Boolean ={
    println(obj)
    val bool = obj == classOf[Student]
    println(s"can convert ? $bool")
    bool
  }


  def marshal(value: Any, writer: HierarchicalStreamWriter, context: MarshallingContext): Unit = {
    val stu = value.asInstanceOf[Student]
    writer.startNode("name")
    writer.setValue(s"${stu.firstName} , ${stu.lastName}")
    writer.endNode()
  }

  def unmarshal(reader: HierarchicalStreamReader, context: UnmarshallingContext): Object = {
    val stu = new Student
    while(reader.hasMoreChildren()){
      reader.moveDown()
      println(s"${reader.getNodeName} : ${reader.getValue}")
      if(reader.getNodeName == "name"){
        val names = reader.getValue.split(",")
        stu.firstName = names.head
        stu.lastName = names.last
      }
      reader.moveUp()
    }
    stu
  }





}