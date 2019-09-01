package overloading

object Overloading2 {

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

    def m(o: Any): Any = {
      print("4")
      o
    }
  }

  class B extends A {
    def m(b1: B, o2: Any): A = {
      print("5")
      b1
    }

    override def m(a: A, o2: Any): B = {
      print("6")
      this
    }

    override def m(o1: Any, o2: Any): Any = {
      print("7")
      o1
    }

    def m(i: Int): Unit = { print("8") }
  }

  def main(args: Array[String]): Unit = {
    val a = new A
    val b = new B
    b.m(b, a) // 5
    b.m(b, b) // 5
    // b.m(a, a)
    // b.m(a, b)
  }

}
