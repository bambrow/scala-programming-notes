```scala
scala> val i = 3
i: Int = 3

scala> if (i > 2) 1 else 2
res0: Int = 1

scala> for (j <- 0 to 5) print(j)
012345

scala> for (j <- 0 until 5) print(j)
01234

scala> for (j <- 0 until 3; k <- 0 until 3) print(j + k)
012123234

scala> for (j <- 0 until 5; k <- 0 until 5) print(j + k)
0123412345234563456745678

scala> val l = List(3,5,2,6,1,4)
l: List[Int] = List(3, 5, 2, 6, 1, 4)

scala> for (j <- l) print(j)
352614

scala> for (j <- l; if j > 2) print(j)
3564

scala> for (j <- l; if j > 2; if j < 4) print(j)
3

scala> for (j <- l; if j > 2; k = j + 1; if k < 5; l = k + 1) print(j)
3

scala> for (j <- l; if j > 2; k = j + 1; if k < 5; l = k + 1) print(l)
5

scala> for (j <- l) yield j * 2
res11: List[Int] = List(6, 10, 4, 12, 2, 8)

scala> l match {
     |   case Nil => "nothing"
     |   case _ :: _ => "something"
     |   case _ => "strange"
     | }
res13: String = something
```
