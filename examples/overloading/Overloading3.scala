package overloading

object Overloading3 {

  class A {
    def m(a1: A, o2: Any): A = {
      print("1")
      a1
    }

    def m(o: B): A = {
      print("2")
      o
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

    def m(o: A): A = {
      print("8")
      o
    }

    def m(a1: A, a2: A): A = {
      print("9")
      a1
    }
  }

  def main(args: Array[String]): Unit = {
    val a = new A
    val b = new B
    b.m(a) // 8
    // b.m(b)
    b.m(a, a) // 9
    b.m(a, b) // 9
    // b.m(b, a)
    // b.m(b, b)
  }

}
