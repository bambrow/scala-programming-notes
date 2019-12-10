
class repeat(body: => Unit) {
  def until(cond: => Boolean): Unit = {
    body
    if (!cond) until(cond)
  }
}

object repeat {
  def apply(body: => Unit) = new repeat(body)
}

var x = 0
repeat {
  x += 1
  print(x + " ")
} until (x == 5)
// 1 2 3 4 5

println

class ifs(cond: => Boolean, blocked: Boolean = false) {
  def ++(cond: => Boolean): ifs = new ifs(this.cond && cond, this.blocked)
  def continue(cond: => Boolean): ifs = this ++ cond
  def exec(body: => Unit): Unit = if (!blocked) body
  def run(body: => Unit): Unit = this exec body
  def quit(body: => Unit): ifs = {
    if (!cond && !blocked) { body; new ifs(false, true) } else this
  }
}

object ifs {
  def apply(cond: => Boolean) = new ifs(cond)
}

ifs(3 > 1) ++ (2 > 1) continue (3 > 2) exec println("hello")
// hello

x = 0
repeat {
  ifs {
    x > 0
  } quit println(x) continue {
    x > 1
  } quit println(x) continue {
    x > 2
  } quit println(x) continue {
    x > 3
  } quit println(x) continue {
    x > 4
  } quit println(x) exec {
    println("finally wins!")
  }
  x += 1
} until (x > 5)
// 0
// 1
// 2
// 3
// 4
// finally wins!

class attempt(body: => Unit) {
  class checked(blocked: Boolean, failed: Boolean) {
    class attempted(failed: Boolean) {
      def fail(body1: => Unit): Unit = if (failed) body1
    }
    def otherwise(body2: => Unit): attempted = if (!blocked) {
      body2; new attempted(failed)
    } else new attempted(failed)
  }
  def when(cond: => Boolean): checked = {
    if (cond) {
      try { body; new checked(true, false) }
      catch { case _: Exception => new checked(true, true) }
    } else new checked(false, false)
  }
}

object attempt {
  def apply(body: => Unit) = new attempt(body)
}

attempt {
  1 / 0
  println("succeed")
} when (1 > 0) otherwise {
  println("otherwise")
} fail {
  println("failed")
}
// failed

attempt {
  1.0 / 0
  println("succeed")
} when (1 > 0) otherwise {
  println("otherwise")
} fail {
  println("failed")
}
// succeed

attempt {
  1.0 / 0
  println("succeed")
} when (0 > 1) otherwise {
  println("otherwise")
} fail {
  println("failed")
}
// otherwise
