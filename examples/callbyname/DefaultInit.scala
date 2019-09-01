package callbyname

object DefaultInit {

  object A {
    val x: Int = B.x + 1
  }

  object B {
    val x: Int = A.x + 1
  }

  def main(args: Array[String]): Unit = {
    println(s"B: ${B.x}, A: ${A.x}") // 2, 1
  }

}
