package examples.overloading

object Overloading {

  class A {
    def m(x: A): A = { println("A.m(A)"); new B() }
    def m(x: B): Unit = { println("A.m(B)") }
    def m(x: A, y: A): Unit = { println("A.m(A, A)") }
    def m(x: A, y: B): Unit = { println("A.m(A, B)") }
    def m(x: B, y: A): Unit = { println("A.m(B, A)") }
    def m(x: C, y: C): Unit = { println("A.m(C, C)") }
    def m(x: Any): Unit = { println("A.m(Any)") }
    def m(x: Short): Unit = { println("A.m(Short)") }
    def m(a: Int): Unit = { println("A.m(Int)") }
    def m(x: Long): Unit = { println("A.m(Long)") }
    def m(x: Double): Unit = { println("A.m(Double)") }
  }

  class B extends A {
    override def m(x: A): A = { println("B.m(A)"); new A() }
    override def m(x: B): Unit = { println("B.m(B)") }
    override def m(x: Any): Unit = { println("B.m(Any)") }
    def m(x: A, y: Any): Unit = { println("B.m(A, Any)") }
    def m(x: Any, y: Any): Unit = { println("B.m(Any, Any)") }
  }

  class C extends B

  implicit def fromString(x: String): A = new A()

  val a: A = new A
  val b: B = new B
  val c: C = new C
  val s1: Short = 1
  val s2: Short = 2
  val a1: A = new B
  val b1: B = new C

  def main(args: Array[String]): Unit = {
    a.m(a) // A.m(A)
    a1.m(a) // B.m(A)
    a1.m(a1) // B.m(A)
    a.m(s1) // A.m(Short)
    a.m(s1 + s2) // A.m(Int)
    b.m(a) // B.m(A)
    b.m(c) // B.m(B)
    a.m(c, c) // A.m(C, C)
    // a1.m(b, b)
    // a1.m(b, c)
    // a1.m(c, b)
    a1.m(new AnyRef) // B.m(Any)
    a1.m(b: A, b) // A.m(A, B)
    // b.m(a, a)
    // b.m(a, b)
    // b.m(a, c)
    // b.m(c, c)
    b.m(a, new AnyRef) // B.m(A, Any)
    b.m("a", "a") // B.m(Any, Any)
    "a".m("a") // A.m(Any)
  }

}
