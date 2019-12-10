package examples.variance

object Combination {

  trait F[-T, +R] {
    def f(x: T): R
  }

  class G[-T, +R](private[this] var y: T, private[this] val z: R) extends F[T, R] {
    override def f(x: T): R = { y = x; z }

    def f1(x: T) = "Hello"

    // def f2(y: R) = "Hello"

    // def f3 = y

    def f4 = z

    override def toString: String = y.toString + " " + z.toString
  }

  // T is an upper bound on types of argument values
  // R is a lower bound on types of result values

  def main(args: Array[String]): Unit = {
    println(new G(1, 2).f(3))
    println(new G(1, 2).f1(3))
    println(new G(1, 2).f4)
  }

}
