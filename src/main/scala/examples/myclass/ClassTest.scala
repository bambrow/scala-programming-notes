package examples.myclass

object ClassTest {

  class A(val k: Int)

  class B(k: Int)

  case class C(k: Int)

  class D(private val k: Int)

  // class E(private k: Int)

  // case class E(private k: Int)

  class E private(val k: Int)

  class F(val k: Int*)

  class G(private[this] val k: Int)

  def main(args: Array[String]): Unit = {
    println(new A(1).k)
    println(new B(1)) // no .k
    println(new C(1).k)
    println(new D(1)) // no access to .k
    // println(new E(1).k) // no access to constructor
    println(new F(1, 2, 3, 4, 5).k)
    println(new G(1)) // no access to .k
  }

}
