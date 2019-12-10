package examples.traits

object Coffee2 {

  trait C1 {
    override def toString: String = "C1"
  }

  trait D1 {
    override def toString: String = "D1"
  }

  trait C2 extends C1 {
    override def toString: String = super.toString + " with C2"
  }

  trait C3 {
    override def toString: String = super.toString + " with C3"
  }

  trait C4 extends C1 {
    override def toString: String = super.toString + " with C4"
  }

  trait C5 extends C4 {
    override def toString: String = super.toString + " with C5"
  }

  trait D2 extends D1 {
    override def toString: String = super.toString + " with D2"
  }

  trait D3 {
    override def toString: String = super.toString + " with D3"
  }

  trait D4 extends D1 {
    override def toString: String = super.toString + " with D4"
  }

  trait D5 extends D4 {
    override def toString: String = super.toString + " with D5"
  }

  trait E {
    override def toString: String = "E"
  }

  def main(args: Array[String]): Unit = {
    println(new E with C1) // C1
    // E <- C1
    println(new E with C2) // C1 with C2
    // E <- C1 <- C2
    println(new E with C3) // E with C3
    // E <- C3
    println(new E with C2 with D2) // D1 with D2
    // E <- C1 <- C2 <- D1 <- D2
    println(new E with C2 with D3) // C1 with C2 with D3
    // E <- C1 <- C2 <- D3
    println(new E with C3 with D3) // E with C3 with D3
    // E <- C3 <- D3
    println(new E with C2 with C3 with C4) // C1 with C2 with C3 with C4
    // E <- C1 <- C2 <- C3 <- C4
    println(new E with C3 with C2 with D3 with C4) // C1 with C2 with D3 with C4
    // E <- C3 <- C1 <- C2 <- D3 <- C4
    println(new E with C2 with D2 with C4 with D4) // D1 with D2 with C4 with D4
    // E <- C1 <- C2 <- D1 <- D2 <- C4 <- D4
    println(new E with C2 with D2 with C3 with C4 with D3 with D4) // D1 with D2 with C3 with C4 with D3 with D4
    // E <- C1 <- C2 <- D1 <- D2 <- C3 <- C4 <- D3 <- D4
    println(new E with D5 with D4) // D1 with D4 with D5
    // E <- D1 <- D4 <- D5
    println(new E with D5 with C5 with D4 with C4 with D2 with C2) // C1 with C4 with C5 with D2 with C2
    // E <- D1 <- D4 <- D5 <- C1 <- C4 <- C5 <- D2 <- C2
    println(new E with C2 with D2 with C5 with D5 with C4 with D4) // D1 with D2 with C4 with C5 with D4 with D5
    // E <- C1 <- C2 <- D1 <- D2 <- C4 <- C5 <- D4 <- D5
    println(new E with C1 with D5 with C5 with D2) // D1 with D4 with D5 with C4 with C5 with D2
    // E <- C1 <- D1 <- D4 <- D5 <- C4 <- C5 <- D2
    println(new E with C1 with D5 with C5 with C4 with D4 with D2) // D1 with D4 with D5 with C4 with C5 with D2
    // E <- C1 <- D1 <- D4 <- D5 <- C4 <- C5 <- D2
    println(new E with C1 with D5 with C5 with D3 with C3) // D1 with D4 with D5 with C4 with C5 with D3 with C3
    // E <- C1 <- D1 <- D4 <- D5 <- C4 <- C5 <- D3 <- C3
  }


}
