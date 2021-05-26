val l1 = List(1, 2, 3)
val l2 = List(4, 5, 6)

l1 drop 1
l1 dropRight 1
l1 dropWhile (_ < 3)
// res0: List[Int] = List(2, 3)
// res1: List[Int] = List(1, 2)
// res2: List[Int] = List(3)

// l1.empty == List[Int]()
// res3: Boolean = true -- 2.13

l1 endsWith List(2,3)
l1 equals List(1,2,3)
// res4: Boolean = true
// res5: Boolean = true

l1 exists (_ > 2)
l1 exists (_ > 3)
l1 filter (_ > 2)
l1 filterNot (_ > 2)
// res6: Boolean = true
// res7: Boolean = false
// res8: List[Int] = List(3)
// res9: List[Int] = List(1, 2)

l1 find (_ > 1)
// l1 findLast (_ > 1)
// res10: Option[Int] = Some(2)
// res11: Option[Int] = Some(3) -- 2.13

List(l1, l2) flatMap {
_ map (_.toString)
}
// res12: List[String] = List(1, 2, 3, 4, 5, 6)

List(l1, l2) flatMap {
  x: List[Int] => x map {
    y: Int => y.toString
  }
}
// res13: List[String] = List(1, 2, 3, 4, 5, 6)

List(l1, l2).flatten
// res14: List[Int] = List(1, 2, 3, 4, 5, 6)

l1.fold(0)(_ + _ * 2)
l1.foldLeft(0)(_ + _ * 2)
l1.foldRight(0)(_ + _ * 2)
// res15: Int = 12
// res16: Int = 12
// res17: Int = 17

l1.foldLeft(List[Int]()) {
  (z: List[Int], x: Int) => x :: z
}
// res18: List[Int] = List(3, 2, 1)

l1.foldRight(List[Int]()) {
  (x: Int, z: List[Int]) => z :+ x
}
// res19: List[Int] = List(3, 2, 1)

List('a', 'b', 'c', 'd').foldLeft(new StringBuilder()) {
  (z: StringBuilder, x: Char) => z append x
}.toString
// res20: String = abcd

List('a', 'b', 'c', 'd').foldRight(new StringBuilder()) {
  (x: Char, z: StringBuilder) => z append x
}.toString
// res21: String = dcba

