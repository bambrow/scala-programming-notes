val t = (3, "hello", true)
t._1
t._2
// res0: Int = 3
// res1: String = hello

def twice(f: Int => Int)(x: Int): Int = f(f(x))
twice(_ + 1)(5)
twice(_ * 2)(5)
// res2: Int = 7
// res3: Int = 20

class C[T] { def f(t: T) = println(t) }
val ci = new C[String]
ci.isInstanceOf[C[_]]
// res4: Boolean = true

val li = 2 :: 3 :: 4 :: 5 :: Nil
li filter (_ > 3)
li.foldLeft(0)(_ + _)
// res5: List[Int] = List(4, 5)
// res6: Int = 14

def sum(a: Int, b: Int, c: Int) = a + b + c
val a = sum _
val b = sum(1, _: Int, 3)
a(1,2,3)
b(2)
// res7: Int = 6
// res8: Int = 6

def sum2(l: List[Int]): Int = l match {
  case Nil => 0
  case x :: xs => x + sum2(xs)
}

sum2(li)
// res9: Int = 14

def nil[T](l: List[T]): Boolean = l match {
  case Nil => true
  case _ => false
}

nil(li)
// res10: Boolean = false
