val l1 = List(1, 2, 3)
val l2 = List(4, 5, 6)

l1.indices
l1.init
l1.inits.toSeq
// res0: scala.collection.immutable.Range = Range 0 until 3
// res1: List[Int] = List(1, 2)
// res2: Seq[List[Int]] = List(List(1, 2, 3), List(1, 2), List(1), List())

l1 intersect l2
l1 intersect List(2,3,4)
l1 isDefinedAt 1
l1 isDefinedAt 3
// res3: List[Int] = List()
// res4: List[Int] = List(2, 3)
// res5: Boolean = true
// res6: Boolean = false

l1.isEmpty
l1.nonEmpty
l1.iterator
// res7: Boolean = false
// res8: Boolean = true
// res9: Iterator[Int] = <iterator>

l1.last
(l1 ++ l1) lastIndexOf 1
(l1 ++ l1) lastIndexOf (1, end = 2)
(l1 ++ l1) lastIndexOfSlice l1
(l1 ++ l1) lastIndexOfSlice (l1, end = 2)
(l1 ++ l1) lastIndexWhere (_ < 2)
(l1 ++ l1) lastIndexWhere (_ < 2, end = 2)
// res10: Int = 3
// res11: Int = 3
// res12: Int = 0
// res13: Int = 3
// res14: Int = 0
// res15: Int = 3
// res16: Int = 0

l1.lastOption
// (l1 lazyZip l1.indices).toSeq
// res17: Option[Int] = Some(3)
// res18: Seq[(Int, Int)] = List((1,0), (2,1), (3,2)) -- 2.13

l1.length
l1.size
l1 lengthCompare 1
l1 lengthCompare 3
l1 lengthCompare 5
// l1 lengthCompare l2
// res19: Int = 3
// res20: Int = 3
// res21: Int = 1
// res22: Int = 0
// res23: Int = -1
// res24: Int = 0 -- 2.13
