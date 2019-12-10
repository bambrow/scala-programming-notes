def f = println("hello")
def f2(s: String): Unit = println(s)

f
f2("world")
// hello
// world

def sum(a: Int, b: Int, c: Int) = a + b + c
val a = sum _
a(1,2,3)
// res2: Int = 6

val b = sum(1, _: Int, 3)
b(2)
// res3: Int = 6

def echo(args: String*) = for (arg <- args) println(arg)
echo("hello", " ", "world", "!")
// hello
// world
// !

echo(Array("a", "b", "c"): _*)
// a
// b
// c

import scala.language.implicitConversions

implicit def intToString(x: Int) = x.toString

implicit val x = 3

def g(implicit y: Int) = y + 1
g
// res6: Int = 4

def g2(implicit y: Int, z: Int) = y + z
g2
// res7: Int = 6

def g3(y: Int)(implicit z: Int) = y + z
g3(1)
// res8: Int = 4

def g4(x: Int)(implicit f: Int => String): List[Char] = f(x).toList
val l = List(12,34,56)
l map g4
// res9: List[List[Char]] = List(List(1, 2), List(3, 4), List(5, 6))

def multi(i: Int)(fac: Int) = i * fac
val byFive = multi(5)_
val byTen = multi(10)_
byFive(3)
byTen(3)
// res10: Int = 15
// res11: Int = 30

def g5(x: => Int) = {
  print(x)
  print(x)
  print(x)
}

var z1 = 0
def x1 = { z1 = z1 + 1; z1 }
lazy val x2 = { z1 = z1 + 1; z1 }
val x3 = { z1 = z1 + 1; z1 }

var z2 = 0
g5({ z2 = z2 + 1; z2 })
// 123

println(x1)
println(x2)
println(x3)
println(x1)
println(x2)
println(x3)
// 2
// 3
// 1
// 4
// 3
// 1