import Stream._
trait Stream[+A]{

    def headOption:Option[A] = this match{
      case Empty => None
      case Cons(head,_) => Some(head())
    }

    def toList : List[A] = {
      this match{
        case Empty => List.empty
        case Cons(head,tail) => head() :: tail().toList
      }
    }

    def take(n:Int) : Stream[A] = {
      this match {
        case Cons(h,t) if n>0 => cons(h(),t().take(n-1))
        case Cons(h,_) if n==1 => cons(h(),empty)
        case _ => Empty
      }
    }

    def takeWhile(p: A => Boolean) : Stream[A] = {
      this match {
        case Cons(head,tail) if p(head()) => cons(head(),tail().takeWhile(p))
        case _ => Empty
      }
    }
}

case object Empty extends Stream[Nothing]
case class Cons[+A](head:() => A, tail:() => Stream[A]) extends Stream[A]

object Stream{

  //this is a smart constructor, using lower case start
  def cons[A](head : => A, tail: => Stream[A]):Stream[A] = {
    lazy val savedHead = head
    lazy val savedTail = tail
    Cons(()=>savedHead, ()=> savedTail)
  }

  def empty[A]:Stream[A] = Empty

  def apply[A](as:A*):Stream[A] =
    if(as.isEmpty) empty
    else cons(as.head,apply(as.tail:_*))


}