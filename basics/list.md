```scala
scala> val l = List(1,2,3)
l: List[Int] = List(1, 2, 3)

scala> val l2 = List(4,5)
l2: List[Int] = List(4, 5)

scala> val l3 = 1 :: 2 :: 3 :: 4 :: 5 :: Nil
l3: List[Int] = List(1, 2, 3, 4, 5)

scala> l :: l2
res27: List[Any] = List(List(1, 2, 3), 4, 5)

scala> l ::: l2
res28: List[Int] = List(1, 2, 3, 4, 5)

scala> l ::: l2 == l3
res29: Boolean = true

scala> l.foreach(print)
123
scala> l.head
res31: Int = 1

scala> l.tail
res32: List[Int] = List(2, 3)

scala> l.isEmpty
res33: Boolean = false

scala> List(l, l2).flatten
res36: List[Int] = List(1, 2, 3, 4, 5)

scala> l.count(_ > 2)
res38: Int = 1

scala> l.count(_ > 1)
res39: Int = 2

scala> l.drop(2)
res40: List[Int] = List(3)

scala> l3.drop(2)
res41: List[Int] = List(3, 4, 5)

scala> l.exists(_ > 2)
res42: Boolean = true

scala> l.exists(_ > 3)
res43: Boolean = false

scala> l3.filter(_ % 2 == 0)
res44: List[Int] = List(2, 4)

scala> l.forall(_ > 2)
res45: Boolean = false

scala> l.forall(_ > 0)
res46: Boolean = true

scala> l.init
res47: List[Int] = List(1, 2)

scala> l.last
res48: Int = 3

scala> l.length
res49: Int = 3

scala> l.map(_ * 2)
res51: List[Int] = List(2, 4, 6)

scala> l.mkString
res52: String = 123

scala> l.mkString(", ")
res53: String = 1, 2, 3

scala> l.reverse
res55: List[Int] = List(3, 2, 1)

scala> l.indices
res57: scala.collection.immutable.Range = Range 0 until 3

scala> l.toArray
res58: Array[Int] = Array(1, 2, 3)

scala> l map (_.toString)
res60: List[String] = List(1, 2, 3)

scala> l map (_ + 10) map (_.toString)
res61: List[String] = List(11, 12, 13)

scala> l map (_ + 10) map (_.toString) flatMap (_.toList)
res62: List[Char] = List(1, 1, 1, 2, 1, 3)

scala> l partition (_ > 2)
res63: (List[Int], List[Int]) = (List(3),List(1, 2))

scala> l partition (_ > 1)
res64: (List[Int], List[Int]) = (List(2, 3),List(1))

scala> l find (_ > 2)
res65: Option[Int] = Some(3)

scala> l find (_ > 3)
res66: Option[Int] = None

scala> l takeWhile (_ < 3)
res67: List[Int] = List(1, 2)

scala> l takeWhile (_ > 3)
res68: List[Int] = List()

scala> l dropWhile (_ > 3)
res69: List[Int] = List(1, 2, 3)

scala> l dropWhile (_ < 3)
res70: List[Int] = List(3)

scala> l forall (_ > 2)
res71: Boolean = false

scala> l forall (_ < 5)
res73: Boolean = true

scala> l.foldRight(0)(_ + _)
res74: Int = 6

scala> l.foldRight(0)(_ * 2 + _)
res80: Int = 12

scala> l.foldRight(0)(_ + _ * 2)
res81: Int = 17

scala> l.foldLeft(0)(_ + _)
res82: Int = 6

scala> l.foldLeft(0)(_ * 2 + _)
res83: Int = 11

scala> l.foldLeft(0)(_ + _ * 2)
res84: Int = 12

scala> l.foldLeft(List[Int]()){ case (l, x) => x :: l }
res87: List[Int] = List(3, 2, 1)

scala> (0 /: l)(_ + _)
res92: Int = 6

scala> (List[Int]() /: l){ case (l, x) => x :: l }
res93: List[Int] = List(3, 2, 1)

scala> l.foldRight(List[Int]()){ case (x, l) => l :+ x }
res95: List[Int] = List(3, 2, 1)

scala> (l :\ 0)(_ + _)
res96: Int = 6

scala> (l :\ List[Int]()){ case (x, l) => l :+ x }
res97: List[Int] = List(3, 2, 1)

scala> l reduceRight (_ + _)
res98: Int = 6

scala> l reduceLeft (_ + _)
res99: Int = 6

scala> List.range(2,10,2)
res100: List[Int] = List(2, 4, 6, 8)

scala> List.empty
res104: List[Nothing] = List()

scala> List.concat(List(), List('a'), List('b'))
res105: List[Char] = List(a, b)

scala> List.fill(5)("abc")
res106: List[String] = List(abc, abc, abc, abc, abc)

scala> l3.dropRight(2)
res107: List[Int] = List(1, 2, 3)

scala> l3.drop(2)
res108: List[Int] = List(3, 4, 5)

scala> l3.takeRight(2)
res109: List[Int] = List(4, 5)

scala> l3.take(2)
res110: List[Int] = List(1, 2)

scala> l3.zipWithIndex
res111: List[(Int, Int)] = List((1,0), (2,1), (3,2), (4,3), (5,4))

scala> l3.indices zip l3
res112: scala.collection.immutable.IndexedSeq[(Int, Int)] = Vector((0,1), (1,2), (2,3), (3,4), (4,5))

scala> l3.grouped(2)
res113: Iterator[List[Int]] = non-empty iterator

scala> res113.foreach(println)
List(1, 2)
List(3, 4)
List(5)

scala> l3.sliding(2)
res115: Iterator[List[Int]] = non-empty iterator

scala> res115.foreach(println)
List(1, 2)
List(2, 3)
List(3, 4)
List(4, 5)

scala> l3.sameElements(l3.reverse)
res117: Boolean = false

scala> l3.sameElements(l3)
res118: Boolean = true
```
