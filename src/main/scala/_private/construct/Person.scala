package _private.construct

class Person private() {
    var name:String = _

    def sayHello() = {
      val n = if (name!= null) name else "xiaoming"
      println(s"hello I'm $n")
    }
}

object Person{
  def getInstance() = new Person()
}