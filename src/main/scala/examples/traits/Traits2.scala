package examples.traits

object Traits2 {

  trait AA {
    override def toString: String = "AA"
  }

  trait BB extends AA {
    override def toString: String = "BB"
  }

  trait CC extends AA with BB

  trait TT1 {
    val x: AA
    override def toString: String = x.toString
  }

  trait TT2 {
    val x: BB
  }

  class DD extends TT1 with TT2 {
    val x = new BB {}
    val y = new CC {}
  }

  def main(args: Array[String]): Unit = {
    println(new DD x) // BB
    println(new DD y) // BB
  }

}
