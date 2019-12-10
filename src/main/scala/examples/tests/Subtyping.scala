package examples.tests

object Subtyping {

  class A { val x = 0 }
  class B extends A { val y = 1 }

  def main(args: Array[String]): Unit = {

    def f(g: A => Int) = g(new A())
    def g(b: B) = b.y

    // f(g)

    def f2(g2: B => Int) = g2(new B())
    def g2(a: A) = a.x

    f2(g2)

    def ff(gg: Int => B) = gg(1)
    def gg(x: Int) = new A()

    // ff(gg).y

    def ff2(gg2: Int => A) = gg2(1)
    def gg2(y: Int) = new B()

    ff2(gg2).x

  }
}
