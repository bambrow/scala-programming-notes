package examples.variance

object CellCon {

  class Cell[-T](private[this] val content: T) {
    def read[R <: T](x: R): R = x
    // def read1(x: T) = x
    def write(x: T): Cell[T] = new Cell(x)
  }

  def main(args: Array[String]): Unit = {
    val c = new Cell(new AnyRef)
    println(c.read("Hello"))
  }

}
