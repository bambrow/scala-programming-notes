val l1 = List(1, 2, 3)
val l2 = List(4, 5, 6)

l1 forall (_ > 1)
l1 forall (_ > 0)
l1 foreach print
// res0: Boolean = false
// res1: Boolean = true
// 123

l1 groupBy (_ > 1)
// res3: scala.collection.immutable.Map[Boolean,List[Int]] = HashMap(false -> List(1), true -> List(2, 3))
l1 groupBy {
  x: Int => l2(x - 1)
}
// res4: scala.collection.immutable.Map[Int,List[Int]] = HashMap(5 -> List(2), 6 -> List(3), 4 -> List(1))

/*
l1.groupMap(_ > 1)(_.toString)
// res5: scala.collection.immutable.Map[Boolean,List[String]] = Map(false -> List(1), true -> List(2, 3))
l1.groupMap({
  x: Int => x % 2
})({
  x: Int => x.toString
})
// res6: scala.collection.immutable.Map[Int,List[String]] = Map(0 -> List(2), 1 -> List(1, 3))

(l1 ++ l2).groupMapReduce(_ % 2)(_.toString)(_ + _)
// res7: scala.collection.immutable.Map[Int,String] = Map(0 -> 246, 1 -> 135)

(l1 ++ l2).groupMapReduce({
  x: Int => x % 3
})({
  x: Int => (x * 111).toString
})({
  (x: String, y: String) => y + x
})
// res8: scala.collection.immutable.Map[Int,String] = Map(0 -> 666333, 1 -> 444111, 2 -> 555222)
*/
// -- 2.13

((l1 ++ l2) grouped 2).toSeq
((l1 ++ l2) grouped 4).toSeq
// res9: Seq[List[Int]] = List(List(1, 2), List(3, 4), List(5, 6))
// res10: Seq[List[Int]] = List(List(1, 2, 3, 4), List(5, 6))

l1.head
l1.headOption
(l1 ++ l1) indexOf 1
(l1 ++ l1) indexOf (1, 1)
(l1 ++ l1) indexOfSlice l1
(l1 ++ l1) indexOfSlice (l1, 1)
(l1 ++ l1) indexWhere (_ < 2)
(l1 ++ l1) indexWhere (_ < 2, 1)
// res11: Int = 1
// res12: Option[Int] = Some(1)
// res13: Int = 0
// res14: Int = 3
// res15: Int = 0
// res16: Int = 3
// res17: Int = 0
// res18: Int = 3