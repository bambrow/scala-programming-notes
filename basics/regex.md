```scala
scala> import scala.util.matching.Regex
import scala.util.matching.Regex

scala> val decimal = """(-)?(\d+)(\.\d*)?""".r
decimal: scala.util.matching.Regex = (-)?(\d+)(\.\d*)?

scala> val input = "1.0 to 99 by 3"
input: String = 1.0 to 99 by 3

scala> for (s <- decimal findAllIn input) println(s)
1.0
99
3

scala> decimal findFirstIn input
res3: Option[String] = Some(1.0)

scala> decimal findPrefixOf input
res4: Option[String] = Some(1.0)

scala> val decimal(sign, i, d) = "-1.23"
sign: String = -
i: String = 1
d: String = .23

scala> val decimal(sign, i, d) = "1.15"
sign: String = null
i: String = 1
d: String = .15
```
