import scala.util.matching.Regex

val decimal = """(-)?(\d+)(\.\d*)?""".r

val input = "1.0 to 99 by 3"
for (s <- decimal findAllIn input) println(s)
// 1.0
// 99
// 3

decimal findFirstIn input
decimal findPrefixOf input
decimal findFirstMatchIn input
decimal findPrefixMatchOf input
// res1: Option[String] = Some(1.0)
// res2: Option[String] = Some(1.0)
// res3: Option[scala.util.matching.Regex.Match] = Some(1.0)
// res4: Option[scala.util.matching.Regex.Match] = Some(1.0)

val decimal(sign, i, d) = "-1.23"
// sign: String = -
// i: String = 1
// d: String = .23

val decimal(sign2, i2, d2) = "1.15"
// sign2: String = null
// i2: String = 1
// d2: String = .15