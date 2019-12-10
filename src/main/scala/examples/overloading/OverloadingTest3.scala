package examples.overloading

object OverloadingTest3 {

  class A {
    def m(b: B, a: A) = print("1")
    def m(b1: B, b2: B) = print("3")
  }

  class B extends A {
    def m(a: A, b: B) = print("2")
  }

  def main(args: Array[String]): Unit = {
    val a = new A
    val b = new B
    // b.m(b, b)
  }

}
