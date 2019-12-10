package examples.variance

object ListCon {

  trait List[-A] {

    def contains[A](a: A): Boolean = this match {
      case Nil(_) => false
      case x :: xs => x == a || xs.contains(a)
    }

    def ::[T <: A](head: T): List[A] = new ::(head, this)

    override def toString: String = this match {
      case Nil(_) => "Nil"
      case x :: xs => x.toString + ", " + xs.toString
    }
  }

  case class Nil[A](a: A) extends List[A]
  case class ::[A](head: A, tail: List[A]) extends List[A]

  def main(args: Array[String]): Unit = {
    val p = 1 :: 2 :: 3 :: Nil(0)
    println(p)
    println(p.contains(4))
  }

}
