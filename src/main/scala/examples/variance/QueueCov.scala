package examples.variance

class QueueCov[+T] private(private[this] var leading: List[T], private[this] var trailing: List[T]) {

  def enqueue[U >: T](x: U) = {
    new QueueCov[U](leading, x :: trailing)
  }

  /*
  def enqueue1(x: T) = {
    new QueueCov[T](leading, x :: trailing)
  }
  */

  private def mirror: Unit = {
    if (leading.isEmpty) {
      leading = trailing.reverse
      trailing = Nil
    }
  }

  def head: T = {
    require(!isEmpty, "Queue.head on empty queue")
    mirror
    leading.head
  }

  def dequeue: (T, QueueCov[T]) = {
    require(!isEmpty, "Queue.dequeue on empty queue")
    mirror
    val x :: leading1 = leading
    (x, new QueueCov(leading1, trailing))
  }

  def isEmpty: Boolean = trailing.isEmpty && leading.isEmpty

  override def toString: String = {
    s"Queue${(leading ::: trailing.reverse).toString.drop(4)}"
  }
}

object QueueCov {
  def empty[T]: QueueCov[T] = new QueueCov(Nil, Nil)

  def apply[T](xs: T*): QueueCov[T] = new QueueCov(xs.toList, Nil)

  def main(args: Array[String]): Unit = {
    val q: QueueCov[Int] = QueueCov.empty
    val q1 = q.enqueue(1).enqueue(2).enqueue(3).enqueue(4)
    val qAny: QueueCov[Any] = q1
    println(qAny.enqueue("Hello"))
    println(q1.dequeue._2.dequeue)
    println(q1)
  }
}
