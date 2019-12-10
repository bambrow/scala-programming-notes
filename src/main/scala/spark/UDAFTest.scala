package spark

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.catalyst.expressions.aggregate.AggregateExpression
import org.apache.spark.sql.execution.aggregate.ScalaUDAF
import org.apache.spark.sql.{Dataset, Row, SparkSession}

object UDAFTest extends App {

  implicit val ss: SparkSession = SparkSession.builder.master("local").getOrCreate

  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)

  import ss.implicits._

  val df1: Dataset[Row] = Seq(
    ("Amy", 65, 101),
    ("Amy", 65, 102),
    ("Amy", 72, 101),
    ("Amy", 82, 103),
    ("Amy", 85, 101),
    ("Amy", 85, 102),
    ("Amy", 90, 103),
    ("Amy", 95, 101),
    ("Bob", 85, 101),
    ("Bob", 85, 103),
    ("Bob", 85, 102),
    ("Bob", 95, 102),
    ("Bob", 95, 101),
    ("Bob", 97, 102),
    ("Bob", 99, 103)
  ) toDF ("name", "grade", "course")

  val countUDAF = new CountUDAF
  val df2 = df1.groupBy('name).agg(countUDAF('grade) as "count")
  df2.show
  /*
  --- initialize(buffer: [null])
  --- initialize(buffer: [null])
  --- update(buffer: [0], input: [65])
  --- update(buffer: [1], input: [65])
  --- update(buffer: [2], input: [72])
  --- update(buffer: [3], input: [82])
  --- update(buffer: [4], input: [85])
  --- update(buffer: [5], input: [85])
  --- update(buffer: [6], input: [90])
  --- update(buffer: [7], input: [95])
  --- update(buffer: [0], input: [85])
  --- update(buffer: [1], input: [85])
  --- update(buffer: [2], input: [85])
  --- update(buffer: [3], input: [95])
  --- update(buffer: [4], input: [95])
  --- update(buffer: [5], input: [97])
  --- update(buffer: [6], input: [99])
  --- initialize(buffer: [null])
  --- initialize(buffer: [null])
  --- merge(buffer1: [0], buffer2: [8])
  --- evaluate(buffer: [8])
  --- initialize(buffer: [null])
  --- initialize(buffer: [null])
  --- merge(buffer1: [0], buffer2: [7])
  --- evaluate(buffer: [7])
  +----+-----+
  |name|count|
  +----+-----+
  | Amy|    8|
  | Bob|    7|
  +----+-----+
   */

  val df3 = df1.groupBy('name).agg(countUDAF.distinct('grade) as "count")
  df3.show
  /*
  --- initialize(buffer: [null])
  --- initialize(buffer: [null])
  --- update(buffer: [0], input: [72])
  --- update(buffer: [1], input: [82])
  --- initialize(buffer: [null])
  --- initialize(buffer: [null])
  --- update(buffer: [0], input: [99])
  --- initialize(buffer: [null])
  --- initialize(buffer: [null])
  --- update(buffer: [0], input: [95])
  --- initialize(buffer: [null])
  --- initialize(buffer: [null])
  --- update(buffer: [0], input: [65])
  --- initialize(buffer: [null])
  --- initialize(buffer: [null])
  --- update(buffer: [0], input: [85])
  --- initialize(buffer: [null])
  --- initialize(buffer: [null])
  --- update(buffer: [0], input: [90])
  --- initialize(buffer: [null])
  --- initialize(buffer: [null])
  --- update(buffer: [0], input: [95])
  --- initialize(buffer: [null])
  --- initialize(buffer: [null])
  --- update(buffer: [0], input: [85])
  --- initialize(buffer: [null])
  --- initialize(buffer: [null])
  --- update(buffer: [0], input: [97])
  --- initialize(buffer: [null])
  --- initialize(buffer: [null])
  --- merge(buffer1: [0], buffer2: [2])
  --- merge(buffer1: [2], buffer2: [1])
  --- merge(buffer1: [3], buffer2: [1])
  --- merge(buffer1: [4], buffer2: [1])
  --- merge(buffer1: [5], buffer2: [1])
  --- evaluate(buffer: [6])
  --- initialize(buffer: [null])
  --- initialize(buffer: [null])
  --- merge(buffer1: [0], buffer2: [1])
  --- merge(buffer1: [1], buffer2: [1])
  --- merge(buffer1: [2], buffer2: [1])
  --- merge(buffer1: [3], buffer2: [1])
  --- evaluate(buffer: [4])
  +----+-----+
  |name|count|
  +----+-----+
  | Amy|    6|
  | Bob|    4|
  +----+-----+
   */

  ss.udf.register("count_udaf", countUDAF)
  ss.sql("select count_udaf(*) from range(3)").show
  /*
  --- initialize(buffer: [null])
  --- initialize(buffer: [null])
  --- update(buffer: [0], input: [0])
  --- update(buffer: [1], input: [1])
  --- update(buffer: [2], input: [2])
  --- initialize(buffer: [null])
  --- initialize(buffer: [null])
  --- merge(buffer1: [0], buffer2: [3])
  --- evaluate(buffer: [3])
  +-------------+
  |countudaf(id)|
  +-------------+
  |            3|
  +-------------+
   */

  val countUDAFCol = countUDAF($"id", $"name")
  countUDAFCol.explain(extended = true)
  // countudaf('id, 'name, CountUDAF@ce19c86, 0, 0)

  println(countUDAFCol.expr.simpleString)
  println(countUDAFCol.expr.toString)
  println(countUDAFCol.expr.verboseString)
  println(countUDAFCol.expr.verboseStringWithSuffix)
  // countudaf('id, 'name, CountUDAF@ce19c86, 0, 0)
  // countudaf('id, 'name, CountUDAF@ce19c86, 0, 0)
  // countudaf('id, 'name, CountUDAF@ce19c86, 0, 0)
  // countudaf('id, 'name, CountUDAF@ce19c86, 0, 0)
  println(countUDAFCol.expr.argString)
  // Complete, false, ExprId(157,09df636c-5c81-4025-b5dd-83236905bb0c)
  println(countUDAFCol.expr.treeString)
  /*
  countudaf('id, 'name, CountUDAF@ce19c86, 0, 0)
  +- CountUDAF('id,'name)
     :- 'id
     +- 'name
   */
  println(countUDAFCol.expr.numberedTreeString)
  /*
  00 countudaf('id, 'name, CountUDAF@ce19c86, 0, 0)
  01 +- CountUDAF('id,'name)
  02    :- 'id
  03    +- 'name
   */

  val countExpressionUDAF = countUDAFCol.expr.asInstanceOf[AggregateExpression]
  println(countExpressionUDAF.toString)
  // countudaf('id, 'name, CountUDAF@ce19c86, 0, 0)

  val countScalaUDAF = countUDAFCol.expr.children.head.asInstanceOf[ScalaUDAF]
  println(countScalaUDAF.toString)
  // CountUDAF('id,'name)

  val countUDAFColDistinct = countUDAF.distinct($"id", $"name")
  countUDAFColDistinct.explain(extended = true)
  // countudaf(distinct 'id, 'name, CountUDAF@ce19c86, 0, 0)

  println(countUDAFColDistinct.expr.argString)
  // Complete, true, ExprId(160,3adeaff4-a22f-4e30-9ec4-52275de70e47)

  println(countUDAFColDistinct.expr.numberedTreeString)
  /*
  00 countudaf(distinct 'id, 'name, CountUDAF@ce19c86, 0, 0)
  01 +- CountUDAF('id,'name)
  02    :- 'id
  03    +- 'name
   */

  println(countUDAFColDistinct.expr.asInstanceOf[AggregateExpression].isDistinct)
  // true

}
