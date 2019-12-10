val l = List(1,2,3)
val l2 = List(4,5)
val l3 = 1 :: 2 :: 3 :: 4 :: 5 :: Nil

l :: l2
l ::: l2
l ::: l2 == l3
// res0: List[Any] = List(List(1, 2, 3), 4, 5)
// res1: List[Int] = List(1, 2, 3, 4, 5)
// res2: Boolean = true

l foreach print
// 123

l.head
l.tail
l.isEmpty
List(l, l2).flatten
// res4: Int = 1
// res5: List[Int] = List(2, 3)
// res6: Boolean = false
// res7: List[Int] = List(1, 2, 3, 4, 5)

l count (_ > 2)
l count (_ > 1)
l drop 2
l3 drop 2
l exists (_ > 2)
l exists (_ > 3)
// res8: Int = 1
// res9: Int = 2
// res10: List[Int] = List(3)
// res11: List[Int] = List(3, 4, 5)
// res12: Boolean = true
// res13: Boolean = false

l3 filter (_ % 2 == 0)
l forall (_ > 2)
l forall (_ > 0)
l.init
l.last
l.length
// res14: List[Int] = List(2, 4)
// res15: Boolean = false
// res16: Boolean = true
// res17: List[Int] = List(1, 2)
// res18: Int = 3
// res19: Int = 3

l map (_ * 2)
l.mkString
l mkString ", "
// res20: List[Int] = List(2, 4, 6)
// res21: String = 123
// res22: String = 1, 2, 3

l.reverse
l.indices
l.toArray
// res23: List[Int] = List(3, 2, 1)
// res24: scala.collection.immutable.Range = Range 0 until 3
// res25: Array[Int] = Array(1, 2, 3)

l map (_.toString)
l map (_ + 10) map (_.toString)
l map (_ + 10) map (_.toString) flatMap (_.toList)
// res26: List[String] = List(1, 2, 3)
// res27: List[String] = List(11, 12, 13)
// res28: List[Char] = List(1, 1, 1, 2, 1, 3)

l partition (_ > 2)
l partition (_ > 1)
l find (_ > 2)
l find (_ > 3)
// res29: (List[Int], List[Int]) = (List(3),List(1, 2))
// res30: (List[Int], List[Int]) = (List(2, 3),List(1))
// res31: Option[Int] = Some(3)
// res32: Option[Int] = None

l takeWhile (_ < 3)
l takeWhile (_ > 3)
l dropWhile (_ > 3)
l dropWhile (_ < 3)
// res33: List[Int] = List(1, 2)
// res34: List[Int] = List()
// res35: List[Int] = List(1, 2, 3)
// res36: List[Int] = List(3)

l forall (_ > 2)
l forall (_ < 5)
// res37: Boolean = false
// res38: Boolean = true

l.foldRight(0)(_ + _)
l.foldRight(0)(_ * 2 + _)
l.foldRight(0)(_ + _ * 2)
l.foldLeft(0)(_ + _)
l.foldLeft(0)(_ * 2 + _)
l.foldLeft(0)(_ + _ * 2)
l.foldLeft(List[Int]()){ case (l, x) => x :: l }
l.foldRight(List[Int]()){ case (x, l) => l :+ x }
// res39: Int = 6
// res40: Int = 12
// res41: Int = 17
// res42: Int = 6
// res43: Int = 11
// res44: Int = 12
// res45: List[Int] = List(3, 2, 1)
// res46: List[Int] = List(3, 2, 1)

l reduceRight (_ + _)
l reduceLeft (_ + _)
List range (2,10,2)
List.empty
List concat (List(), List('a'), List('b'))
// res47: Int = 6
// res48: Int = 6
// res49: List[Int] = List(2, 4, 6, 8)
// res50: List[Nothing] = List()
// res51: List[Char] = List(a, b)

List.fill(5)("abc")
l3 dropRight 2
l3 drop 2
l3 takeRight 2
l3 take 2
// res52: List[String] = List(abc, abc, abc, abc, abc)
// res53: List[Int] = List(1, 2, 3)
// res54: List[Int] = List(3, 4, 5)
// res55: List[Int] = List(4, 5)
// res56: List[Int] = List(1, 2)

l3.zipWithIndex
l3.indices zip l3
// res57: List[(Int, Int)] = List((1,0), (2,1), (3,2), (4,3), (5,4))
// res58: IndexedSeq[(Int, Int)] = Vector((0,1), (1,2), (2,3), (3,4), (4,5))

l3 grouped 2 foreach println
// List(1, 2)
// List(3, 4)
// List(5)

l3 sliding 2 foreach println
// List(1, 2)
// List(2, 3)
// List(3, 4)
// List(4, 5)

l3 sameElements l3.reverse
l3 sameElements l3
// res61: Boolean = false
// res62: Boolean = true