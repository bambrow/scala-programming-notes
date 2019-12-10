package examples.overloading

object OverloadingTest5 {

  class A {
    def m(b: B, c: C) = print("1")
  }

  class B extends A {
    def m(b1: B, b2: B) = print("2")
  }

  class C extends B

  def main(args: Array[String]): Unit = {
    val a = new A
    val b = new B
    val c = new C
    // b.m(c, c)
  }

}
