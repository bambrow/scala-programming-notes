package variance

object ListCov {

  trait List[+A] {

    def map[B](f: A => B): List[B] = this match {
      case Nil => Nil
      case x :: xs => f(x) :: xs.map(f)
    }

    def contains[T >: A](a: T): Boolean = this match {
      case Nil => false
      case x :: xs => x == a || xs.contains(a)
    }

    def fold[T >: A](z: T)(op: (T, A) => T): T = this match {
      case Nil => z
      case x :: xs => xs.fold(op(z, x))(op)
    }

    def ::[T >: A](head: T): List[T] = new ::(head, this)

    override def toString: String = this match {
      case Nil => "Nil"
      case x :: xs => x.toString + ", " + xs.toString
    }
  }

  case object Nil extends List[Nothing]
  case class ::[A](head: A, tail: List[A]) extends List[A]

  def main(args: Array[String]): Unit = {
    val p = 1 :: 2 :: 3 :: Nil
    println(p)
    val p1 = p.map(_ + 1)
    println(p1)
    println(p1.contains(4))
    println(p1.fold(0)(_ + _))
  }

}
