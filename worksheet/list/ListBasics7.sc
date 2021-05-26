val l1 = List(1, 2, 3)
val l2 = List(4, 5, 6)

l1 map (_.toString)
l1.max
l1 maxBy (_ % 2)
// res0: List[String] = List(1, 2, 3)
// res1: Int = 3
// res2: Int = 1

// l1 maxByOption (_ % 2)
// l1.maxOption
l1.min
l1 minBy (_ % 2)
// l1 minByOption (_ % 2)
// l1.minOption
// res3: Option[Int] = Some(1) -- 2.13
// res4: Option[Int] = Some(3) -- 2.13
// res5: Int = 1
// res6: Int = 2
// res7: Option[Int] = Some(2) -- 2.13
// res8: Option[Int] = Some(1) -- 2.13

l1.mkString
l1 mkString "--"
l1 mkString ("[", "-", "]")
// res9: String = 123
// res10: String = 1--2--3
// res11: String = [1-2-3]

l1 padTo (5, -1)
l1 padTo (10, 0)
// res12: List[Int] = List(1, 2, 3, -1, -1)
// res13: List[Int] = List(1, 2, 3, 0, 0, 0, 0, 0, 0, 0)

l1 partition (_ > 2)
l1 partition (_ > 3)
// res14: (List[Int], List[Int]) = (List(3),List(1, 2))
// res15: (List[Int], List[Int]) = (List(),List(1, 2, 3))

l1 patch (1, l2, 2)
l1 patch (1, l2, 1)
l1 patch (1, l2, 0)
// res16: List[Int] = List(1, 4, 5, 6)
// res17: List[Int] = List(1, 4, 5, 6, 3)
// res18: List[Int] = List(1, 4, 5, 6, 2, 3)

l1.permutations.toSeq
// res19: Seq[List[Int]] = List(List(1, 2, 3), List(1, 3, 2), List(2, 1, 3), List(2, 3, 1), List(3, 1, 2), List(3, 2, 1))

l1.product
// res20: Int = 6

(l1 ++ l2) reduce (_ + _)
(l1 ++ l2) reduceLeft (_ + _)
(l1 ++ l2) reduceLeftOption (_ + _)
(l1 ++ l2) reduceRight (_ + _)
(l1 ++ l2) reduceRightOption (_ + _)
// res21: Int = 21
// res22: Int = 21
// res23: Option[Int] = Some(21)
// res24: Int = 21
// res25: Option[Int] = Some(21)
