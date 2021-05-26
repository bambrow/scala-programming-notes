val l1 = List(1, 2, 3)
val l2 = List(4, 5, 6)

(l1 combinations 2).toSeq
// res0: Seq[List[Int]] = List(List(1, 2), List(1, 3), List(2, 3))

l1 contains 2
// res1: Boolean = true

l1 containsSlice Seq(2,3)
l1 containsSlice Seq(3,4)
// res2: Boolean = true
// res3: Boolean = false

val a1 = Array.ofDim[Int](2)
l1 copyToArray a1
a1
// res4: Int = 2
// res5: Array[Int] = Array(1, 2)

val a2 = Array.ofDim[Int](4)
l1 copyToArray a2
a2
// res6: Int = 3
// res7: Array[Int] = Array(1, 2, 3, 0)

l1 copyToArray (a2, 1)
a2
// res8: Int = 3
// res9: Array[Int] = Array(1, 1, 2, 3)

l1 copyToArray (a2, 2, 2)
a2
// res10: Int = 2
// res11: Array[Int] = Array(1, 1, 1, 2)

l1.corresponds(l2){
  (x, y) => y == x + 3
}
// res12: Boolean = true

(l1 ++ l2) count (_ % 2 == 0)
// res13: Int = 3

l1 diff List(2,3,4)
// res14: List[Int] = List(1)

(l1 ++ List(2,3,4)).distinct
// res15: List[Int] = List(1, 2, 3, 4)

// (l1 ++ l2) distinctBy (_ / 2)
// res16: List[Int] = List(1, 2, 4, 6) -- 2.13