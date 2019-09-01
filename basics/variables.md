```scala
scala> val msg = "hello"
msg: String = hello

scala> val msg2: String = "hello"
msg2: String = hello

scala> val n: Int = 3
n: Int = 3

scala> var msg3 = "hello"
msg3: String = hello

scala> msg3 = "world"
msg3: String = world

scala> msg2 = "world"
<console>:12: error: reassignment to val
       msg2 = "world"
            ^

scala> 0 max 5
res0: Int = 5

scala> 0 min 5
res1: Int = 0

scala> -2.7 abs
<console>:12: warning: postfix operator abs should be enabled
by making the implicit value scala.language.postfixOps visible.
This can be achieved by adding the import clause 'import scala.language.postfixOps'
or by setting the compiler option -language:postfixOps.
See the Scaladoc for value scala.language.postfixOps for a discussion
why the feature should be explicitly enabled.
       -2.7 abs
            ^
res2: Double = 2.7

scala> import scala.language.postfixOps
import scala.language.postfixOps

scala> -2.7 round
res3: Long = -3

scala> 1.5 isIn
isInfinite   isInfinity   isInstanceOf

scala> 1.5 isInfinity
res4: Boolean = false

scala> (1.0 / 0) isInfinity
res5: Boolean = true

scala> 4 to 6
res6: scala.collection.immutable.Range.Inclusive = Range 4 to 6

scala> "hello" capitalize
res7: String = Hello

scala> "hello" drop 2
res8: String = llo

scala> val dec = 31
dec: Int = 31

scala> val hex = 0xFF
hex: Int = 255

scala> val lng = 31L
lng: Long = 31

scala> val srt: Short = 36
srt: Short = 36

scala> val bte: Byte = 15
bte: Byte = 15

scala> val d = 12.34
d: Double = 12.34

scala> val de = 1.234e4
de: Double = 12340.0

scala> val flt = 12.34f
flt: Float = 12.34

scala> val c = 'h'
c: Char = h

scala> val unicode = '\u0043'
unicode: Char = C

scala> val raw = """ he said "hello" """
raw: String = " he said "hello" "

scala> "hello".isInstanceOf[String]
res9: Boolean = trueaa

scala> 3.isInstanceOf[Int]
res10: Boolean = true

scala> 3.asInstanceOf[Double]
res11: Double = 3.0

scala> classOf[String]
res12: Class[String] = class java.lang.String
```
