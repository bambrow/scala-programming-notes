```scala
scala> val t = (3, "hello", true)
t: (Int, String, Boolean) = (3,hello,true)

scala> t._1
res13: Int = 3

scala> t._2
res14: String = hello

scala> def twice(f: Int => Int)(x: Int): Int = f(f(x))
twice: (f: Int => Int)(x: Int)Int

scala> twice(_+1)(5)
res15: Int = 7

scala> twice(_*2)(5)
res16: Int = 20

scala> class C[T] { def f(t: T) = println(t) }
defined class C

scala> val ci = new C[String]
ci: C[String] = C@3a4f4a81

scala> ci.isInstanceOf[C[_]]
res17: Boolean = true

scala> val li = 2 :: 3 :: 4 :: 5 :: Nil
li: List[Int] = List(2, 3, 4, 5)

scala> li.filter(_ > 3)
res18: List[Int] = List(4, 5)

scala> li.foldLeft(0)(_ + _)
res19: Int = 14

scala> def sum(a: Int, b: Int, c: Int) = a + b + c
sum: (a: Int, b: Int, c: Int)Int

scala> val a = sum _
a: (Int, Int, Int) => Int = $$Lambda$1311/1570602757@73484926

scala> val b = sum(1, _: Int, 3)
b: Int => Int = $$Lambda$1312/7863421@5a4e993d

scala> a(1,2,3)
res20: Int = 6

scala> b(2)
res21: Int = 6

scala> import java.io.File._
import java.io.File._

scala> import java.util.{Map, HashMap}
import java.util.{Map, HashMap}

scala> def sum2(l: List[Int]): Int = l match {
     |   case Nil => 0
     |   case x :: xs => x + sum2(xs)
     | }
sum2: (l: List[Int])Int

scala> sum2(li)
res22: Int = 14

scala> def nil[T](l: List[T]): Boolean = l match {
     |   case Nil => true
     |   case _ => false
     | }
nil: [T](l: List[T])Boolean

scala> nil(li)
res23: Boolean = false

scala> var age: Int = _
age: Int = 0
```
