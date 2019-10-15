
val l: List[Option[Int]] = List(Option(2), Some(3), None, Option(1), Some(4), Option.empty)
val l2: List[Option[String]] = List(Option("abc"), Some("bcd"), None, Option.empty, Option(null))
// l: List[Option[Int]] = List(Some(2), Some(3), None, Some(1), Some(4), None)
// l2: List[Option[String]] = List(Some(abc), Some(bcd), None, None, None)

l map (_.isDefined)
l map (_.isEmpty)
l map (_.nonEmpty)
// res1: List[Boolean] = List(true, true, false, true, true, false)
// res2: List[Boolean] = List(false, false, true, false, false, true)
// res3: List[Boolean] = List(true, true, false, true, true, false)

l map (_ orElse Some(-1))
l map (_ getOrElse -1)
// res4: List[Option[Int]] = List(Some(2), Some(3), Some(-1), Some(1), Some(4), Some(-1))
// res5: List[Int] = List(2, 3, -1, 1, 4, -1)

l filter (_.nonEmpty) map (_.get)
// res6: List[Int] = List(2, 3, 1, 4)

l map (_.fold("")(_.toString))
// res7: List[String] = List(2, 3, "", 1, 4, "")

l map (_ map (_.toString))
l map (_ flatMap (x => Some(x.toString)))
// res7: List[Option[String]] = List(Some(2), Some(3), None, Some(1), Some(4), None)
// res8: List[Option[String]] = List(Some(2), Some(3), None, Some(1), Some(4), None)

l foreach (_ foreach (x => print(x + " ")))
// 2 3 1 4

val pf = new PartialFunction[Int, String] {
  override def apply(v1: Int): String = v1.toString
  override def isDefinedAt(x: Int): Boolean = x % 2 == 0
}
l map (_ collect pf)
// res10: List[Option[String]] = List(Some(2), None, None, None, Some(4), None)

l map (_ filter (_ > 2))
l map (_ filterNot (_ > 2))
// res11: List[Option[Int]] = List(None, Some(3), None, None, Some(4), None)
// res12: List[Option[Int]] = List(Some(2), None, None, Some(1), None, None)

l map (_ exists (_ > 2))
l map (_ forall (_ > 2))
// res13: List[Boolean] = List(false, true, false, false, true, false)
// res14: List[Boolean] = List(false, true, true, false, true, true)

l map (_ contains 2)
// res15: List[Boolean] = List(true, false, false, false, false, false)

Option(2) zip Option("abc")
// res16: Iterable[(Int, String)] = List((2,abc))

val l3 = l zip l2 map (x => x._1 zip x._2)
// l3: List[Iterable[(Int, String)]] = List(List((2,abc)), List((3,bcd)), List(), List(), List())

l3 map (_.unzip)
// res17: List[(Iterable[Int], Iterable[String])] = List((List(2),List(abc)), (List(3),List(bcd)), (List(),List()), (List(),List()), (List(),List()))

l map (_.toList)
// res18: List[List[Int]] = List(List(2), List(3), List(), List(1), List(4), List())

l2 map (_.orNull)
// res19: List[String] = List(abc, bcd, null, null, null)

l map {
  case Some(x) => x.toString
  case _ => ""
}
// res20: List[String] = List(2, 3, "", 1, 4, "")
