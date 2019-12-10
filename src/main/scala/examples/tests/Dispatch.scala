package examples.tests

object Dispatch {

	class C (x: Int, y: String) {
		val value = x
		val name = y
		override def toString(): String = "C:" + value + "," + name
	}

	class D (x: Int, y: String, z: Int) extends C(x,y) {
		val age = z
		override def toString(): String = "D:" + value + "," + name + "," + age
	}

  def printC(c: C) = {
    println(c)
  }

	def main(args: Array[String]): Unit = {
	  printC(new C(20, "Amy"))
	  printC(new D(25, "Jack", 20))
	}

}
