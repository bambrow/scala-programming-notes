val l1: List[Int] = List(1,2,3)
val l2: List[Int] = List(4,5,6)

l1 ++ l2
// res0: List[Int] = List(1, 2, 3, 4, 5, 6)
// l1 concat l2
// res1: List[Int] = List(1, 2, 3, 4, 5, 6) -- 2.13

l1 ++: l2
// res2: List[Int] = List(1, 2, 3, 4, 5, 6)
// l2 prependedAll l1
// res3: List[Int] = List(1, 2, 3, 4, 5, 6) -- 2.13

l1 +: l2
// res4: List[Any] = List(List(1, 2, 3), 4, 5, 6)
// l2 prepended l1
// res5: List[Any] = List(List(1, 2, 3), 4, 5, 6) -- 2.13

l1 :+ l2
// res6: List[Any] = List(1, 2, 3, List(4, 5, 6))
// l1 appended l2
// res7: List[Any] = List(1, 2, 3, List(4, 5, 6)) -- 2.13

// l1 :++ l2
// res8: List[Int] = List(1, 2, 3, 4, 5, 6) -- 2.13
// l1 appendedAll l2
// res9: List[Int] = List(1, 2, 3, 4, 5, 6) -- 2.13

l1 ::: l2
// res10: List[Int] = List(1, 2, 3, 4, 5, 6)

1 :: 2 :: 3 :: 4 :: Nil
// res11: List[Int] = List(1, 2, 3, 4)

1 :: 2 :: 3 :: 4 :: List.empty
// res12: List[Int] = List(1, 2, 3, 4)