package tests

object Singleton {

  object Counter {
    var count = 0
    def increment { count = count + 1 }
    def get() = count
    def useCount(f: (Int) => Unit) = f(count)

  }

  class Foo {
    val x = 12
    var y: Int = 20
    def f(z: Int): Int = x+y+z
    def +(other: Foo) : Int = y + other.y
    def set(w: Int) {
      Counter.increment
      y = w
    }
  }

  def printArg(x: Int) { println("Got the value " + x) }
  def main(args: Array[String]): Unit = {
    val o = new Foo()
    o set 17; 
    val p = new Foo()
    p.set(25)
    println(o.+(p))
    println(o + p)
    println("Counter is now " + Counter.get())
    Counter.useCount(printArg)
    Counter.useCount((x:Int) => println(x*1000))
  }

  object Another {
    def twice(f: Int => Int) = f(f(1))
    def main(args: Array[String]) {
      println(twice { x => x+1 })
      println(twice(x => x*2))
      println(twice { x => {
        println(x)
        x+1
      }})
    }
  }

}
