val l1 = List(1, 2, 3)
val l2 = List(4, 5, 6)

l1 addString new StringBuilder
// res0: StringBuilder = 123

l1 addString (new StringBuilder, ",")
// res1: StringBuilder = 1,2,3

l1 addString (new StringBuilder, "[", ",", "]")
// res2: StringBuilder = [1,2,3]

val oddMultiplyTwo = new PartialFunction[Int, Int] {
  override def apply(v1: Int): Int = v1 * 2
  override def isDefinedAt(x: Int): Boolean = x % 2 != 0
}

val evenDivideTwo = new PartialFunction[Int, Int] {
  override def apply(v1: Int): Int = v1 / 2
  override def isDefinedAt(x: Int): Boolean = x % 2 == 0
}

(l1 ++ l2) collect oddMultiplyTwo
// res3: List[Int] = List(2, 6, 10)

(l1 ++ l2) collect evenDivideTwo
// res4: List[Int] = List(1, 2, 3)

l1 collectFirst oddMultiplyTwo match {
  case Some(x) => "got " + x.toString
  case _ => "got nothing"
}
// res5: String = got 2

l1 apply 1
// res6: Int = 2
l1(1)
// res7: Int = 2

val p1: PartialFunction[Int, Int] = l1 andThen l2
p1(0)
p1(1)
// res8: Int = 5
// res9: Int = 6

val p2: PartialFunction[Int, Int] = l1 orElse l2
p2(0)
p2(1)
// res10: Int = 1
// res11: Int = 2
