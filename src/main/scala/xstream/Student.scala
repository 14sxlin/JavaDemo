package xstream

import com.thoughtworks.xstream.annotations.XStreamAlias

@XStreamAlias("Student")
class Student {
  @XStreamAlias("FirstName")
   var firstName:String = _
   @XStreamAlias("Lastname")
   var lastName:String = _
   @XStreamAlias("RollNo")
   var rollNo : Int = _
}