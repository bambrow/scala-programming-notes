package variance

object CellCov {

  class Cell[+T](private[this] val content: T) {
    def read: T = content
    def write[R >: T](x: R): Cell[R] = new Cell(x)
    // def write1(x: T): Cell[T] = new Cell(x)
  }

  def main(args: Array[String]): Unit = {
    val c = new Cell("Hello")
    println(c.read)
    val c1 = c.write("world")
    println(c1.read)
    val c2 = c1.write(new AnyRef)
    println(c2.read)
  }

}
