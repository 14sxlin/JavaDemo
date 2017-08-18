package stream

object UsingStream extends App {
  //not allow garbage , may consume a lot memory
  val fibos : Stream[BigInt] =
    BigInt(0) #::
    BigInt(1) #::
    fibos.zip(fibos.tail).map(n =>{
      println("Adding %d and %d".format(n._1, n._2))
      n._1 + n._2
    })// will not call if value has been calculated

  //allow garbage
  def fibos1 : Stream[BigInt] = {
    BigInt(0) #::
    BigInt(1) #::
    fibos.zip(fibos.tail).map(n =>{
      println("Adding %d and %d".format(n._1, n._2))
      n._1 + n._2
    })//call every time
  }

  println("----------fibo------------")
    //foreach means immediately do
  fibos take 5 foreach println
  println("------------------------------")
  //stream will save the calculated value because val
  fibos take 6 foreach println

  println("----------fibo1------------")
  fibos1 take 5 foreach println
  println("------------------------------")
  //every time recalculate the value , not store
  fibos1 take 6 foreach println
}