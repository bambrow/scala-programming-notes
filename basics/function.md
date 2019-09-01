```scala
scala> def f = println("hello")
f: Unit

scala> f
hello

scala> def f2(s: String): Unit = println(s)
f2: (s: String)Unit

scala> f2("world")
world

scala> def sum(a: Int, b: Int, c: Int) = a + b + c
sum: (a: Int, b: Int, c: Int)Int

scala> val a = sum _
a: (Int, Int, Int) => Int = $$Lambda$1423/580341434@6631e6af

scala> a(1,2,3)
res16: Int = 6

scala> val b = sum(1, _: Int, 3)
b: Int => Int = $$Lambda$1424/1165363160@76dea970

scala> b(2)
res17: Int = 6

scala> def echo(args: String*) = for (arg <- args) println(arg)
echo: (args: String*)Unit

scala> val arr = Array("hello", " ", "world", "!")
arr: Array[String] = Array(hello, " ", world, !)

scala> echo(arr: _*)
hello

world
!

scala> import scala.language.implicitConversions
import scala.language.implicitConversions

scala> implicit def intToString(x: Int) = x.toString
intToString: (x: Int)String

scala> implicit val x = 3
x: Int = 3

scala> def g(implicit y: Int) = y + 1
g: (implicit y: Int)Int

scala> g
res19: Int = 4

scala> def g2(implicit y: Int, z: Int) = y + z
g2: (implicit y: Int, implicit z: Int)Int

scala> g2
res20: Int = 6

scala> def g3(y: Int)(implicit z: Int) = y + z
g3: (y: Int)(implicit z: Int)Int

scala> g3(1)
res26: Int = 4

scala> def g4(x: Int)(implicit f: Int => String) = f(x).toList
g4: (x: Int)(implicit f: Int => String)List[Char]

scala> val l = List(111,222,333)
l: List[Int] = List(111, 222, 333)

scala> l.map(g4)
res31: List[List[Char]] = List(List(1, 1, 1), List(2, 2, 2), List(3, 3, 3))

scala> def multi(i: Int)(fac: Int) = i * fac
multi: (i: Int)(fac: Int)Int

scala> val byFive = multi(5)_
byFive: Int => Int = $$Lambda$1516/1309160568@1cdc470b

scala> val byTen = multi(10)_
byTen: Int => Int = $$Lambda$1517/1816848145@5bd817

scala> byFive(3)
res32: Int = 15

scala> byTen(3)
res33: Int = 30

scala> def g5(x: => Int) = {
     |   print(x)
     |   print(x)
     |   print(x)
     | }
g5: (x: => Int)Unit

scala> var z1 = 0
z1: Int = 0

scala> def x1 = { z1 = z1 + 1; z1 }
x1: Int

scala> lazy val x2 = { z1 = z1 + 1; z1 }
x2: Int = <lazy>

scala> val x3 = { z1 = z1 + 1; z1 }
x3: Int = 1

scala> var z2 = 0
z2: Int = 0

scala> g5({ z2 = z2 + 1; z2 })
123

scala> println(x1)
2

scala> println(x2)
3

scala> println(x3)
1

scala> println(x1)
4

scala> println(x2)
3

scala> println(x3)
1
```
