val l1 = List(1, 2, 3)
val l2 = List(4, 5, 6)

(l1 ++ l2).toArray
(l1 ++ l2).toBuffer
(l1 ++ l2).toIndexedSeq
// res0: Array[Int] = Array(1, 2, 3, 4, 5, 6)
// res1: scala.collection.mutable.Buffer[Int] = ArrayBuffer(1, 2, 3, 4, 5, 6)
// res2: IndexedSeq[Int] = Vector(1, 2, 3, 4, 5, 6)

(l1 ++ l2) zip (l1 ++ l2).indices
((l1 ++ l2) zip (l1 ++ l2).indices).toMap
// res3: List[(Int, Int)] = List((1,0), (2,1), (3,2), (4,3), (5,4), (6,5))
// res4: scala.collection.immutable.Map[Int,Int] = HashMap(5 -> 4, 1 -> 0, 6 -> 5, 2 -> 1, 3 -> 2, 4 -> 3)

(l1 ++ l2).toSet
(l1 ++ l2).toString
(l1 ++ l2).toVector
// res5: scala.collection.immutable.Set[Int] = HashSet(5, 1, 6, 2, 3, 4)
// res6: String = List(1, 2, 3, 4, 5, 6)
// res7: scala.collection.immutable.Vector[Int] = Vector(1, 2, 3, 4, 5, 6)

(l1 zip l2).unzip
// res8: (List[Int], List[Int]) = (List(1, 2, 3),List(4, 5, 6))

(l1 zip l2 zip List(7,8,9) map {
  case ((x, y), z) => (x, y, z)
}).unzip3
// res9: (List[Int], List[Int], List[Int]) = (List(1, 2, 3),List(4, 5, 6),List(7, 8, 9))

l1 updated (1, 4)
l1 updated (2, 5)
// res10: List[Int] = List(1, 4, 3)
// res11: List[Int] = List(1, 2, 5)

l1.zipWithIndex
// res12: List[(Int, Int)] = List((1,0), (2,1), (3,2))
