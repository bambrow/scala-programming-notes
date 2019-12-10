package examples.tree

object OrderedTree {

  abstract class Tree[T <: Ordered[T]]

  case class Node[T <: Ordered[T]](v: T, l: Tree[T], r: Tree[T]) extends Tree[T]
  case class Leaf[T <: Ordered[T]](v: T) extends Tree[T]

  class A {
    override def toString() = "A"
  }
  class B extends A {
    override def toString() = "B"
  }
  class C[+T](x: T) {
    def f(t: Int) = x
  }
  class D[-T] {
    def f(t: T) = 1
  }

  class Test(a: Int) extends Ordered[Test] {
    val x = a
    override def <(other: Test) = this.x < other.x
    override def <=(other: Test) = this.x <= other.x
    override def >(other: Test) = this.x > other.x
    override def >=(other: Test) = this.x >= other.x
    override def compare(other: Test) = {
      if (this > other) 1
      else if (this < other) -1
      else 0
    }
    override def compareTo(other: Test) = this.compare(other)
    override def toString() = "Test<" + x + ">"
  }

  def minimum[T <: Ordered[T]](t: Tree[T]): T = {
    t match {
      case Node(v,l,r) => {
        var value = v
        val left = minimum(l)
        val right = minimum(r)
        if (value > left) value = left
        if (value > right) value = right
        value
      }
      case Leaf(v) => v
    }
  }

  def main(args: Array[String]): Unit = {
    val myTree: Tree[Test] = Node(new Test(4), Node(new Test(3), Leaf(new Test(2)), Leaf(new Test(5))), Node(new Test(7), Leaf(new Test(6)), Node(new Test(9), Leaf(new Test(1)), Leaf(new Test(8)))))
    println(minimum(myTree))
    val c: C[A] = new C[B](new B())
    val d: D[B] = new D[A]()
    println(c.f(0))
    println(d.f(new B()))
  }

}
