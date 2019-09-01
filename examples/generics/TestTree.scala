package generics

object TestTree {

  trait CanBeCompared[T] {
    def <(other: T): Boolean
    def <=(other:T) = <(other) || ==(other)
    def >(other:T) = !(<(other)) && !=(other)
    def >=(other: T) = !(<(other))
  }

  abstract class Tree[T <: CanBeCompared[T]] extends CanBeCompared[Tree[T]]

  case class Node[T <: CanBeCompared[T]](v: T, l: Tree[T], r: Tree[T]) extends Tree[T] {
    def <(other: Tree[T]) = other match {
      case Node(ov,ol,or) => (v < ov) && (l < ol) && (r < or)
      case _ => false
    }
    override def toString() = "("+l+")"+ v+"("+r+")"
  }
  
}