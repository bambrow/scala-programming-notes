package examples.variance

object Contravariant {

  class Cell[-T](private[this] var content: T) {
    // def read: T = content
    def write(x: T): Unit = content = x
  }

  // contravariant
  // write only
  // cannot do read
  // upper bound of argument value

  // annotates a value that may flow from the client to an instance of the class
  // e.g. the parameter type of a public method
  // you can also think contravariant annotations as describing values that are written by the client into the instance
  // from the client's perspective, upper bound
  // the clients are required to provide the values of the specified parameter types or any subtypes of T
  // from the implementation's perspective, lower bound
  // the method may safely assume that the values provided by the client are of any supertype of T

  def main(args: Array[String]): Unit = {

    val c = new Cell(new AnyRef)
    val c1: Cell[String] = c // OK, because String <: AnyRef and T is contravariant, so Cell[AnyRef] <: Cell[String]

    // if read is allowed then the following code will be valid
    // c1.read.charAt(0)

  }

}
