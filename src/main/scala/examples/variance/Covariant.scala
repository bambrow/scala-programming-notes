package examples.variance

object Covariant {

  class Cell[+T](private[this] val content: T) {
    def read: T = content
    // def write(x: T): Unit = content = x
  }

  // covariant
  // read only
  // cannot do write
  // lower bound of return value

  // annotates a value that may flow from an instance of the class to its client
  // e.g. the return type of a public method
  // you can also think covariant annotations as describing values that are read by the client from the instance
  // from the client's perspective, lower bound
  // the clients may safely assume that the values returned by method calls are an instance of any supertype of T
  // from the implementation's perspective, upper bound
  // the method body must return an instance of some subtype of T

  def main(args: Array[String]): Unit = {

    val c = new Cell("Hello")
    val c1: Cell[AnyRef] = c // OK, because String <: AnyRef and T is covariant, so Cell[String] <: Cell[AnyRef]

    // if write is allowed then the following code will be valid
    // c1.write(new AnyRef)
    // c.read.charAt(0)

  }

}
