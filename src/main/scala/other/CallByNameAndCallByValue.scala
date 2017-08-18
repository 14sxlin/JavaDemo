package other

object CallByNameAndCallByValue extends App {

  def getIntWithSideEffect() = {
    println("hello")
    2
  }

  def callByValue(x:Int) = { // once calculated it won't calculate again
    println("begin")
    println(s"x1 = $x")
    println(s"x2 = $x")
    println("end")
  }

  def callByName(x: => Int) = {  //every time you need the value it calculate again
    println("begin")
    println(s"x1 = $x")
    println(s"x2 = $x")
    println("end")
  }

  //I think this is call by value , just the value is a function
  def whatAboutThis(x: () => Int) = {
    println(s"x1 = $x")
    println(s"x2 = ${x()}")
  }

  def shit( fun : () => Unit){
    println("going to println")
    fun()
  }

  println("call by name")
  callByName(getIntWithSideEffect())

  println("call by value")
  callByValue(getIntWithSideEffect())

  println("call by value in a function")
  whatAboutThis(getIntWithSideEffect)

  println("using {} to call by name")
  callByName{// this won't execute until this block being call
    println("something in {}")
    getIntWithSideEffect()
  }

  println("using {} to call by value")
  callByValue{//this will be execute immediately
    println("something in {}")
    getIntWithSideEffect()
  }

}