package overloading

object OverloadingTest7 {

  class A {
    def m(d: D, a: A) = print("1")
  }

  class B extends A {
    def m(a: A, b: B) = print("2")
  }

  class C extends B

  class D extends C

  def main(args: Array[String]): Unit = {
    val a = new A
    val b = new B
    val c = new C
    val d = new D
    b.m(d, d) // 2
  }

}
