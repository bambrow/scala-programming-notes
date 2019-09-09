val msg: String = "hello"
var msg2: String = "hello"

msg2 = "world"
// msg = "world"

0 max 5
0 min 5
-2.7.abs
-2.7.round
1.5.isInfinity
(1.0 / 0).isInfinity
// res0: Int = 5
// res1: Int = 0
// res2: Double = 2.7
// res3: Long = -3
// res4: Boolean = false
// res5: Boolean = true

4 to 6
msg.capitalize
msg drop 2
// res6: scala.collection.immutable.Range.Inclusive = Range 4 to 6
// res7: String = Hello
// res8: String = llo

val x1: Int = 3
val x2: Long = 3L
val x3: Short = 3
val x4: Byte = 3
val x5: Double = 3.0
val x6: Float = 3.0f
val x7: Char = 'h'
val x8: Char = '\u0043'

val raw = """  "hello world"   """
raw.isInstanceOf[String]
3.isInstanceOf[Int]
3.asInstanceOf[Double]
// res9: Boolean = true
// res10: Boolean = true
// res11: Double = 3.0