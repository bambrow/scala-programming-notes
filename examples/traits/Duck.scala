package traits

object Duck {

  trait Duck {
    val quackSound: String
    def quack = println(quackSound)
  }

  class Mallard extends Duck {
    override val quackSound: String = "Quack"
  }

  class RubberDuck extends Duck {
    override val quackSound: String = "Squeak"
  }

  def main(args: Array[String]): Unit = {
    val a = new Mallard
    val b = new RubberDuck
    a.quack // Quack
    b.quack // Squeak
  }

}
