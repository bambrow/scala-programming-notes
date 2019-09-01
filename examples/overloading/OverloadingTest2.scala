package overloading

object OverloadingTest2 {

  class A {
    def m(b: B, a: A) = print("1")
  }

  class B extends A {
    def m(a: A, b: B) = print("2")
  }

  def main(args: Array[String]): Unit = {
    val a = new A
    val b = new B
    b.m(b, b) // 2
  }

}
