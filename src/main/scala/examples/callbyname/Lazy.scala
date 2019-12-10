package examples.callbyname

object Lazy {

  def f1(x: Int): Unit = {
    println(x)
    println(x)
  }

  def f2(x: => Int): Unit = {
    println(x)
    println(x)
  }

  def f3(x: Unit => Int): Unit = {
    println(x())
    println(x())
  }

  def main(args: Array[String]): Unit = {

    var y = 0
    var z = 0
    f1({ y = y + 1; y }) // 1 1
    f2({ z = z + 1; z }) // 1 2
    var y1 = 0
    f3({ _ => y1 = y1 + 1; y1 }) // 1 2

    var z1 = 0
    def x1 = { z1 = z1 + 1; z1 }
    lazy val x2 = { z1 = z1 + 1; z1 }
    val x3 = { z1 = z1 + 1; z1 }
    println(x1) // 2
    println(x2) // 3
    println(x3) // 1
    println(x1) // 4
    println(x2) // 3
    println(x3) // 1

  }

}
