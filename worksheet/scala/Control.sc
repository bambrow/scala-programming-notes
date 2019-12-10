val i = 3
if (i > 2) 1 else 2
// res0: Int = 1

for (j <- 0 to 5) print(j)
// 012345

for (j <- 0 until 5) print(j)
// 01234

for (j <- 0 until 3; k <- 0 until 3) print(j + k)
// 012123234

for (j <- 0 until 5; k <- 0 until 5) print(j + k)
// 0123412345234563456745678

val l = List(3,5,2,6,1,4)
for (j <- l) print(j)
// 352614

for (j <- l; if j > 2) print(j)
// 3564

for (j <- l; if j > 2; if j < 4) print(j)
// 3

for (j <- l; if j > 2; k = j + 1; if k < 5) print(j)
// 3

for (j <- l; if j > 2; k = j + 1; if k < 5; x = k + 1) print(x)
// 5

for (j <- l) yield j * 2
// res10: List[Int] = List(6, 10, 4, 12, 2, 8)

l match {
  case Nil => "nothing"
  case _ :: _ => "something"
  case _ => "error"
}
// res11: String = something