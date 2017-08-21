package _private.construct

object PersonApp extends App {
  val person = Person.getInstance()
  person.name = "sparrowxin"
  person.sayHello()

//  val p1 = new Person()  // It's not allowed
}