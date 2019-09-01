package traits

object Traits3 {

  trait X {
    def m(): Unit
  }

  trait X1 extends X {
    override def m(): Unit = println("X1")
  }

  trait X2 extends X {
    override def m(): Unit = println("X2")
  }

  trait X3 extends X {
    override def m(): Unit = println("X3")
  }

  object x extends X1 with X2 with X3

  def main(args: Array[String]): Unit = {
    x.m // X3
  }
}
