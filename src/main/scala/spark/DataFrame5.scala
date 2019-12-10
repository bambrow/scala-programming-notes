package spark

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{Dataset, Row, SparkSession}

/**
 * This file tests the following sql.functions:
 *   array_contains, array_distinct, array_except,
 *   array_intersect, array_join, array_position,
 *   array_repeat, array_sort, array_union, array,
 *   arrays_zip, concat, concat_ws, explode, flatten,
 *   map, map_concat, map_from_arrays, map_keys, map_values,
 *   posexplode, slice, sort_array, struct
 */

object DataFrame5 extends App {

  implicit val ss: SparkSession = SparkSession.builder.master("local").getOrCreate

  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)

  import ss.implicits._

  val df1: Dataset[Row] = Seq(
    ("Amy", "OS", Seq[java.lang.Integer](45,69,79,89,92,92,100), Seq(92,96,100)),
    ("Bob", "OS", Seq[java.lang.Integer](77,77,88,88,99,99), Seq(98,99,99)),
    ("Cathy", "OS", Seq[java.lang.Integer](), Seq(95)),
    ("David", "OS", Seq[java.lang.Integer](null), null),
    ("David", "AI", null, null)
  ) toDF ("name", "course", "grade", "expected")

  val df1a = df1 withColumn ("contains92", array_contains($"grade", 92))
  df1a.show(false)
  /*
  +-----+------+-----------------------------+-------------+----------+
  |name |course|grade                        |expected     |contains92|
  +-----+------+-----------------------------+-------------+----------+
  |Amy  |OS    |[45, 69, 79, 89, 92, 92, 100]|[92, 96, 100]|true      |
  |Bob  |OS    |[77, 77, 88, 88, 99, 99]     |[98, 99, 99] |false     |
  |Cathy|OS    |[]                           |[95]         |false     |
  |David|OS    |[]                           |null         |false     |
  |David|AI    |null                         |null         |null      |
  +-----+------+-----------------------------+-------------+----------+
   */

  val df1b = df1 withColumn ("grade", array_distinct($"grade"))
  df1b.show(false)
  /*
  +-----+------+-------------------------+-------------+
  |name |course|grade                    |expected     |
  +-----+------+-------------------------+-------------+
  |Amy  |OS    |[45, 69, 79, 89, 92, 100]|[92, 96, 100]|
  |Bob  |OS    |[77, 88, 99]             |[98, 99, 99] |
  |Cathy|OS    |[]                       |[95]         |
  |David|OS    |[]                       |null         |
  |David|AI    |null                     |null         |
  +-----+------+-------------------------+-------------+
   */

  val df1c = df1 withColumn ("grade", array_except($"grade", $"expected"))
  df1c.show(false)
  /*
  +-----+------+----------------+-------------+
  |name |course|grade           |expected     |
  +-----+------+----------------+-------------+
  |Amy  |OS    |[45, 69, 79, 89]|[92, 96, 100]|
  |Bob  |OS    |[77, 88]        |[98, 99, 99] |
  |Cathy|OS    |[]              |[95]         |
  |David|OS    |null            |null         |
  |David|AI    |null            |null         |
  +-----+------+----------------+-------------+
   */

  val df1d = df1 withColumn ("grade", array_intersect($"grade", $"expected"))
  df1d.show(false)
  /*
  +-----+------+---------+-------------+
  |name |course|grade    |expected     |
  +-----+------+---------+-------------+
  |Amy  |OS    |[92, 100]|[92, 96, 100]|
  |Bob  |OS    |[99]     |[98, 99, 99] |
  |Cathy|OS    |[]       |[95]         |
  |David|OS    |null     |null         |
  |David|AI    |null     |null         |
  +-----+------+---------+-------------+
   */

  val df1e = df1 withColumn ("grade", array_join($"grade", ","))
  df1e.show(false)
  /*
  +-----+------+---------------------+-------------+
  |name |course|grade                |expected     |
  +-----+------+---------------------+-------------+
  |Amy  |OS    |45,69,79,89,92,92,100|[92, 96, 100]|
  |Bob  |OS    |77,77,88,88,99,99    |[98, 99, 99] |
  |Cathy|OS    |                     |[95]         |
  |David|OS    |                     |null         |
  |David|AI    |null                 |null         |
  +-----+------+---------------------+-------------+
   */

  val df1f = df1 withColumn ("grade", array_join($"grade", ",", "^_^"))
  df1f.show(false)
  /*
  +-----+------+---------------------+-------------+
  |name |course|grade                |expected     |
  +-----+------+---------------------+-------------+
  |Amy  |OS    |45,69,79,89,92,92,100|[92, 96, 100]|
  |Bob  |OS    |77,77,88,88,99,99    |[98, 99, 99] |
  |Cathy|OS    |                     |[95]         |
  |David|OS    |^_^                  |null         |
  |David|AI    |null                 |null         |
  +-----+------+---------------------+-------------+
   */

  val df1g = df1 withColumn ("position92", array_position($"grade", 92))
  df1g.show(false)
  /*
  +-----+------+-----------------------------+-------------+----------+
  |name |course|grade                        |expected     |position92|
  +-----+------+-----------------------------+-------------+----------+
  |Amy  |OS    |[45, 69, 79, 89, 92, 92, 100]|[92, 96, 100]|5         |
  |Bob  |OS    |[77, 77, 88, 88, 99, 99]     |[98, 99, 99] |0         |
  |Cathy|OS    |[]                           |[95]         |0         |
  |David|OS    |[]                           |null         |0         |
  |David|AI    |null                         |null         |null      |
  +-----+------+-----------------------------+-------------+----------+
   */

  val df1h = df1 select ($"name", $"course", array_repeat($"expected", 2))
  df1h.show(false)
  /*
  +-----+------+------------------------------+
  |name |course|array_repeat(expected, 2)     |
  +-----+------+------------------------------+
  |Amy  |OS    |[[92, 96, 100], [92, 96, 100]]|
  |Bob  |OS    |[[98, 99, 99], [98, 99, 99]]  |
  |Cathy|OS    |[[95], [95]]                  |
  |David|OS    |[,]                           |
  |David|AI    |[,]                           |
  +-----+------+------------------------------+
   */

  val df1i = df1 select ($"name", $"course", array_sort($"grade"))
  df1i.show(false)
  /*
  +-----+------+-----------------------------+
  |name |course|array_sort(grade)            |
  +-----+------+-----------------------------+
  |Amy  |OS    |[45, 69, 79, 89, 92, 92, 100]|
  |Bob  |OS    |[77, 77, 88, 88, 99, 99]     |
  |Cathy|OS    |[]                           |
  |David|OS    |[]                           |
  |David|AI    |null                         |
  +-----+------+-----------------------------+
   */

  val df1j = df1 select ($"name", $"course", array_union($"grade", $"expected"))
  df1j.show(false)
  /*
  +-----+------+-----------------------------+
  |name |course|array_union(grade, expected) |
  +-----+------+-----------------------------+
  |Amy  |OS    |[45, 69, 79, 89, 92, 100, 96]|
  |Bob  |OS    |[77, 88, 99, 98]             |
  |Cathy|OS    |[95]                         |
  |David|OS    |null                         |
  |David|AI    |null                         |
  +-----+------+-----------------------------+
   */

  val df1k = df1 select ($"name", $"course", array($"name", $"course"))
  df1k.show(false)
  /*
  +-----+------+-------------------+
  |name |course|array(name, course)|
  +-----+------+-------------------+
  |Amy  |OS    |[Amy, OS]          |
  |Bob  |OS    |[Bob, OS]          |
  |Cathy|OS    |[Cathy, OS]        |
  |David|OS    |[David, OS]        |
  |David|AI    |[David, AI]        |
  +-----+------+-------------------+
   */

  val df1l = df1 select ($"name", $"course", arrays_zip($"grade", $"expected"))
  df1l.show(false)
  /*
  +-----+------+------------------------------------------------------------+
  |name |course|arrays_zip(grade, expected)                                 |
  +-----+------+------------------------------------------------------------+
  |Amy  |OS    |[[45, 92], [69, 96], [79, 100], [89,], [92,], [92,], [100,]]|
  |Bob  |OS    |[[77, 98], [77, 99], [88, 99], [88,], [99,], [99,]]         |
  |Cathy|OS    |[[, 95]]                                                    |
  |David|OS    |null                                                        |
  |David|AI    |null                                                        |
  +-----+------+------------------------------------------------------------+
   */

  df1l.printSchema
  /*
  root
   |-- name: string (nullable = true)
   |-- course: string (nullable = true)
   |-- arrays_zip(grade, expected): array (nullable = true)
   |    |-- element: struct (containsNull = false)
   |    |    |-- grade: integer (nullable = true)
   |    |    |-- expected: integer (nullable = true)
   */

  val df1m = df1 select ($"name", $"course", concat($"name", lit("|"), $"course") as "concat")
  df1m.show(false)
  /*
  +-----+------+--------+
  |name |course|concat  |
  +-----+------+--------+
  |Amy  |OS    |Amy|OS  |
  |Bob  |OS    |Bob|OS  |
  |Cathy|OS    |Cathy|OS|
  |David|OS    |David|OS|
  |David|AI    |David|AI|
  +-----+------+--------+
   */

  val df1n = df1 select ($"name", $"course", concat_ws(",", $"name", $"course") as "concat_ws")
  df1n.show(false)
  /*
  +-----+------+---------+
  |name |course|concat_ws|
  +-----+------+---------+
  |Amy  |OS    |Amy,OS   |
  |Bob  |OS    |Bob,OS   |
  |Cathy|OS    |Cathy,OS |
  |David|OS    |David,OS |
  |David|AI    |David,AI |
  +-----+------+---------+
   */

  val df1o = df1 select ($"name", $"course", explode($"grade"), $"expected")
  df1o.show(false)
  /*
  +-----+------+----+-------------+
  |name |course|col |expected     |
  +-----+------+----+-------------+
  |Amy  |OS    |45  |[92, 96, 100]|
  |Amy  |OS    |69  |[92, 96, 100]|
  |Amy  |OS    |79  |[92, 96, 100]|
  |Amy  |OS    |89  |[92, 96, 100]|
  |Amy  |OS    |92  |[92, 96, 100]|
  |Amy  |OS    |92  |[92, 96, 100]|
  |Amy  |OS    |100 |[92, 96, 100]|
  |Bob  |OS    |77  |[98, 99, 99] |
  |Bob  |OS    |77  |[98, 99, 99] |
  |Bob  |OS    |88  |[98, 99, 99] |
  |Bob  |OS    |88  |[98, 99, 99] |
  |Bob  |OS    |99  |[98, 99, 99] |
  |Bob  |OS    |99  |[98, 99, 99] |
  |David|OS    |null|null         |
  +-----+------+----+-------------+
   */

  val df1p = df1 select ($"name", $"course", flatten(array_repeat($"grade", 2)))
  df1p.show(false)
  /*
  +-----+------+----------------------------------------------------------+
  |name |course|flatten(array_repeat(grade, 2))                           |
  +-----+------+----------------------------------------------------------+
  |Amy  |OS    |[45, 69, 79, 89, 92, 92, 100, 45, 69, 79, 89, 92, 92, 100]|
  |Bob  |OS    |[77, 77, 88, 88, 99, 99, 77, 77, 88, 88, 99, 99]          |
  |Cathy|OS    |[]                                                        |
  |David|OS    |[,]                                                       |
  |David|AI    |null                                                      |
  +-----+------+----------------------------------------------------------+
   */

  val df1q = df1 select ($"name", $"course", map($"name", $"course"), map_keys(map($"name", $"course")), map_values(map($"name", $"course")))
  df1q.show(false)
  /*
  +-----+------+-----------------+---------------------------+-----------------------------+
  |name |course|map(name, course)|map_keys(map(name, course))|map_values(map(name, course))|
  +-----+------+-----------------+---------------------------+-----------------------------+
  |Amy  |OS    |[Amy -> OS]      |[Amy]                      |[OS]                         |
  |Bob  |OS    |[Bob -> OS]      |[Bob]                      |[OS]                         |
  |Cathy|OS    |[Cathy -> OS]    |[Cathy]                    |[OS]                         |
  |David|OS    |[David -> OS]    |[David]                    |[OS]                         |
  |David|AI    |[David -> AI]    |[David]                    |[AI]                         |
  +-----+------+-----------------+---------------------------+-----------------------------+
   */

  val df1r = df1 select ($"name", $"course", map_from_arrays($"expected", $"expected"))
  df1r.show(false)
  /*
  +-----+------+-----------------------------------+
  |name |course|map_from_arrays(expected, expected)|
  +-----+------+-----------------------------------+
  |Amy  |OS    |[92 -> 92, 96 -> 96, 100 -> 100]   |
  |Bob  |OS    |[98 -> 98, 99 -> 99, 99 -> 99]     |
  |Cathy|OS    |[95 -> 95]                         |
  |David|OS    |null                               |
  |David|AI    |null                               |
  +-----+------+-----------------------------------+
   */

  val df1s = df1 select ($"name", $"course", map_concat(map($"name", $"course"), map($"name", $"course")))
  df1s.show(false)
  /*
  +-----+------+------------------------------------------------+
  |name |course|map_concat(map(name, course), map(name, course))|
  +-----+------+------------------------------------------------+
  |Amy  |OS    |[Amy -> OS, Amy -> OS]                          |
  |Bob  |OS    |[Bob -> OS, Bob -> OS]                          |
  |Cathy|OS    |[Cathy -> OS, Cathy -> OS]                      |
  |David|OS    |[David -> OS, David -> OS]                      |
  |David|AI    |[David -> AI, David -> AI]                      |
  +-----+------+------------------------------------------------+
   */

  val df1t = df1 select ($"name", $"course", posexplode($"grade"), $"expected")
  df1t.show(false)
  /*
  +-----+------+---+----+-------------+
  |name |course|pos|col |expected     |
  +-----+------+---+----+-------------+
  |Amy  |OS    |0  |45  |[92, 96, 100]|
  |Amy  |OS    |1  |69  |[92, 96, 100]|
  |Amy  |OS    |2  |79  |[92, 96, 100]|
  |Amy  |OS    |3  |89  |[92, 96, 100]|
  |Amy  |OS    |4  |92  |[92, 96, 100]|
  |Amy  |OS    |5  |92  |[92, 96, 100]|
  |Amy  |OS    |6  |100 |[92, 96, 100]|
  |Bob  |OS    |0  |77  |[98, 99, 99] |
  |Bob  |OS    |1  |77  |[98, 99, 99] |
  |Bob  |OS    |2  |88  |[98, 99, 99] |
  |Bob  |OS    |3  |88  |[98, 99, 99] |
  |Bob  |OS    |4  |99  |[98, 99, 99] |
  |Bob  |OS    |5  |99  |[98, 99, 99] |
  |David|OS    |0  |null|null         |
  +-----+------+---+----+-------------+
   */

  val df1u = df1 select ($"name", $"course", $"grade", slice($"grade", 1, 2))
  df1u.show(false)
  /*
  +-----+------+-----------------------------+------------------+
  |name |course|grade                        |slice(grade, 1, 2)|
  +-----+------+-----------------------------+------------------+
  |Amy  |OS    |[45, 69, 79, 89, 92, 92, 100]|[45, 69]          |
  |Bob  |OS    |[77, 77, 88, 88, 99, 99]     |[77, 77]          |
  |Cathy|OS    |[]                           |[]                |
  |David|OS    |[]                           |[]                |
  |David|AI    |null                         |null              |
  +-----+------+-----------------------------+------------------+
   */

  val df1v = df1 select ($"name", $"course", sort_array($"grade", asc = false))
  df1v.show(false)
  /*
  +-----+------+-----------------------------+
  |name |course|sort_array(grade, false)     |
  +-----+------+-----------------------------+
  |Amy  |OS    |[100, 92, 92, 89, 79, 69, 45]|
  |Bob  |OS    |[99, 99, 88, 88, 77, 77]     |
  |Cathy|OS    |[]                           |
  |David|OS    |[]                           |
  |David|AI    |null                         |
  +-----+------+-----------------------------+
   */

  val df1w = df1 select ($"name", $"course", struct($"name", $"course"))
  df1w.show(false)
  /*
  +-----+------+----------------------------------------+
  |name |course|named_struct(name, name, course, course)|
  +-----+------+----------------------------------------+
  |Amy  |OS    |[Amy, OS]                               |
  |Bob  |OS    |[Bob, OS]                               |
  |Cathy|OS    |[Cathy, OS]                             |
  |David|OS    |[David, OS]                             |
  |David|AI    |[David, AI]                             |
  +-----+------+----------------------------------------+
   */

  df1w.printSchema
  /*
  root
   |-- name: string (nullable = true)
   |-- course: string (nullable = true)
   |-- named_struct(name, name, course, course): struct (nullable = false)
   |    |-- name: string (nullable = true)
   |    |-- course: string (nullable = true)
   */

}
