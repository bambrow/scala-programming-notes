package overloading

object OverloadingTest6 {

  class A {
    def m(a: A, b: B) = print("1")
  }

  class B extends A {
    def m(a1: A, a2: A) = print("2")
  }

  class C extends B

  def main(args: Array[String]): Unit = {
    val a = new A
    val b = new B
    val c = new C
    // b.m(c, c)
  }

}
