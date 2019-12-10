package examples.overloading

object Overloading5 {

  class A {
    def m(a1: A, o2: Any): A = {
      print("1")
      a1
    }

    def m(a1: A, a2: A): A = {
      print("2")
      a1
    }

    def m(o1: Any, o2: Any): Any = {
      print("3")
      o1
    }

    def m(o1: Any, o2: C): A = {
      print("4")
      o2
    }
  }

  class B extends A {
    def m(o2: Any, b1: B): A = {
      print("5")
      b1
    }

    def m(o2: Any, a: A): B = {
      print("6")
      this
    }

    override def m(o1: Any, o2: Any): Any = {
      print("7")
      o1
    }

    def m(i: Int): Unit = { print("8") }
  }

  class C extends B

  def main(args: Array[String]): Unit = {
    val a = new A
    val b = new B
    val c = new C
    // b.m(b, a)
    b.m(b, b) // 5
    // b.m(a, a)
    b.m(a, b) // 5
    // b.m(new AnyRef, c)
  }

}
