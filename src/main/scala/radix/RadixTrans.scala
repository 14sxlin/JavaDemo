package radix

object RadixTrans extends App {
  def printA_Za_z(){
    for( begin <- Array('a','A');
          i <- 0 to 25){
      print(begin + i toChar) // so great
//      print((char)(begin + i)) // in java use this form
    }
  }

//  printA_Za_z()

  println(100.toHexString)
  println(100.toOctalString)

  println(Integer.parseInt("100", 7))
}