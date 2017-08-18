package fp.stream

object TestStream extends App {
  var i = 0
  val stream = Stream(1,2,3,4,5)
  stream.headOption match {
    case Some(head) => println(s"head= $head")
    case None => println("Nothing")
  }

  println("toList--------")
  for(i <- stream.toList)
    println(s"i = $i")

  println("take---------")
  for(i <- stream.take(3).toList)
    println(s"i = $i")
}