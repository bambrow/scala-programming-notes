val l1 = List(1, 2, 3)
val l2 = List(4, 5, 6)

l1.reverse
l1.reverseIterator.toSeq
l1 reverse_::: l2
// res0: List[Int] = List(3, 2, 1)
// res1: Seq[Int] = List(3, 2, 1)
// res2: List[Int] = List(3, 2, 1, 4, 5, 6)

l1.scan(0)(_ + _)
l1.scanLeft(0)(_ + _)
l1.scanRight(0)(_ + _)
// res3: List[Int] = List(0, 1, 3, 6)
// res4: List[Int] = List(0, 1, 3, 6)
// res5: List[Int] = List(6, 5, 3, 0)

// (l1 ++ l2) search 4
// (l1 ++ l2) search (4, 0, 2)
// res6: scala.collection.Searching.SearchResult = Found(3) -- 2.13
// res7: scala.collection.Searching.SearchResult = InsertionPoint(2) -- 2.13

// List(0,1,3,5,7,8,9) segmentLength (_ % 2 == 1)
List(0,1,3,5,7,8,9) segmentLength (_ % 2 == 1, 2)
// res8: Int = 0 -- 2.13
// res9: Int = 3

/*
l1 sizeCompare 5
l1 sizeCompare 3
l1 sizeCompare 1
l1 sizeCompare l2
// res10: Int = -1
// res11: Int = 0
// res12: Int = 1
// res13: Int = 0
*/
// -- 2.13

(l1 ++ l2) slice (2, 5)
((l1 ++ l2) sliding 3).toSeq
((l1 ++ l2) sliding (3, 2)).toSeq
// res14: List[Int] = List(3, 4, 5)
// res15: Seq[List[Int]] = List(List(1, 2, 3), List(2, 3, 4), List(3, 4, 5), List(4, 5, 6))
// res16: Seq[List[Int]] = List(List(1, 2, 3), List(3, 4, 5), List(5, 6))

(l1 ++ l2) sortBy (_ % 3)
(l1 ++ l2) sortWith (_ > _)
// res17: List[Int] = List(3, 6, 1, 4, 2, 5)
// res18: List[Int] = List(6, 5, 4, 3, 2, 1)

List(3,2,1,5,4,6).sorted
// res19: List[Int] = List(1, 2, 3, 4, 5, 6)

(l1 ++ l2) span (_ < 3)
(l1 ++ l2) splitAt 3
// res20: (List[Int], List[Int]) = (List(1, 2),List(3, 4, 5, 6))
// res21: (List[Int], List[Int]) = (List(1, 2, 3),List(4, 5, 6))

(l1 ++ l2) startsWith l1
(l1 ++ l2) startsWith (List(2,3,4), offset = 1)
(l1 ++ l2) endsWith l2
// res22: Boolean = true
// res23: Boolean = true
// res24: Boolean = true

l1.sum
l1.tail
l1.tails.toSeq
// res25: Int = 6
// res26: List[Int] = List(2, 3)
// res27: Seq[List[Int]] = List(List(1, 2, 3), List(2, 3), List(3), List())

l1 take 1
l1 take 2
l1 takeRight 1
l1 takeRight 2
// res28: List[Int] = List(1)
// res29: List[Int] = List(1, 2)
// res30: List[Int] = List(3)
// res31: List[Int] = List(2, 3)

(l1 ++ l2) takeWhile (_ < 3)
// (l1 ++ l2) tapEach println
// res32: List[Int] = List(1, 2)
// 1
// 2
// 3
// 4
// 5
// 6
// res33: List[Int] = List(1, 2, 3, 4, 5, 6) -- 2.13
