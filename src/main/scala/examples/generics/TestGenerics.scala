package examples.generics

object TestGenerics {

  class C[T] // invariant
  class A
  class B extends A
  class D[+T]
  class F[-T]

  def main(args: Array[String]): Unit = {
    val x: A = new B()
    // val y: B = new A()
    // val c: C[A] = new C[B]()
    val d: D[A] = new D[B]()
    // val d2: D[B] = new D[A]()

    /*
    class E[+T](x:T) {
      def f(x: T) = 0
    }
    */

    class E[+T](x:T) {
      def f(y: Int) = x
    }
    // output is covariant

    val f: F[B] = new F[A]()
    // val f2: F[A] = new F[B]()

    class G[-T](x:T) {
      def f(x: T) = 0
    }

    /*
    class G[-T](x:T) {
      def f(y: Int) = x
    }
    */
    // input is contravariant

    val g: E[A] = new E[B](new B)
    println(g f 1)

    val h: G[B] = new G[A](new A)
    println(h f new B)

  }
}
