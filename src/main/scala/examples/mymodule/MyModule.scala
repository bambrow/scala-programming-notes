package examples.mymodule

import scala.annotation.tailrec

object MyModule {

  def abs(n: Int): Int = if (n < 0) -n else n
  private def formatAbs(x: Int) = {
    formatResult("absolute value")(abs)(x)
  }

  def factorial(n: Int): Int = {
    @tailrec
    def go(n: Int, acc: Int): Int = if (n <= 0) acc else go(n - 1, n * acc)
    go(n, 1)
  }
  private def formatFactorial(x: Int) = {
    formatResult("factorial")(factorial)(x)
  }

  def fib(n: Int): Int = {
    @tailrec
    def go(n: Int, x: Int, y: Int): Int = if (n <= 0) x else go(n - 1, y, y + x)
    go(n, 0, 1)
  }
  private def formatFib(x: Int) = {
    formatResult("Fibonacci")(fib)(x)
  }

  def formatResult(name: String)(f: Int => Int)(x: Int) = {
    val msg = "The %s of %d is %d"
    msg.format(name, x, f(x))
  }

  def findFirst[A](as: Array[A])(f: A => Boolean): Int = {
    @tailrec
    def go(n: Int): Int = if (n >= as.length) -1 else if (f(as(n))) n else go(n + 1)
    go(0)
  }

  def isSorted[A](as: Array[A])(f: (A, A) => Boolean): Boolean = {
    @tailrec
    def go(n: Int): Boolean = if (n >= as.length - 1) true else if (f(as(n+1), as(n))) false else go(n + 1)
    go(0)
  }

  def main(args: Array[String]): Unit = {
    println(formatAbs(-42))
    println(formatFactorial(7))
    println(formatFib(7))
    println("Find first: " + findFirst(Array(1,2,3,4,5))(x => x % 2 == 0))
    println("Is sorted? " + isSorted(Array(1,2,3,4,5))(_ < _))
    println("Is sorted? " + isSorted(Array("abc", "ab", "a"))(_.length > _.length))
  }

}
