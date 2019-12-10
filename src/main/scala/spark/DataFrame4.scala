package spark

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{Dataset, Row, SparkSession}

/**
 * This file tests the following agg methods:
 *   approx_count_distinct, count, countDistinct,
 *   avg, max, min, sum, first, last, collect_list, collect_set,
 *   grouping, grouping_id
 */

object DataFrame4 extends App {

  implicit val ss: SparkSession = SparkSession.builder.master("local").getOrCreate

  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)

  import ss.implicits._

  val df4: Dataset[Row] = Seq(
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

  val df4a = df4 groupBy "name" agg (approx_count_distinct($"grade"), count($"grade"), countDistinct($"grade"))
  df4a.show(truncate = false)
  /*
  +----+----------------------------+------------+---------------------+
  |name|approx_count_distinct(grade)|count(grade)|count(DISTINCT grade)|
  +----+----------------------------+------------+---------------------+
  |Amy |6                           |8           |6                    |
  |Bob |4                           |7           |4                    |
  +----+----------------------------+------------+---------------------+
   */

  val df4b = df4 groupBy "name" agg (avg($"grade"), max($"grade"), min($"grade"), sum($"grade"))
  df4b.show(truncate = false)
  /*
  +----+-----------------+----------+----------+----------+
  |name|avg(grade)       |max(grade)|min(grade)|sum(grade)|
  +----+-----------------+----------+----------+----------+
  |Amy |79.875           |95        |65        |639       |
  |Bob |91.57142857142857|99        |85        |641       |
  +----+-----------------+----------+----------+----------+
   */

  val df4c = df4 groupBy "name" agg (first($"grade"), last($"grade"))
  df4c.show(truncate = false)
  /*
  +----+-------------------+------------------+
  |name|first(grade, false)|last(grade, false)|
  +----+-------------------+------------------+
  |Amy |65                 |95                |
  |Bob |85                 |99                |
  +----+-------------------+------------------+
   */

  val df4d = df4 groupBy "name" agg (collect_list($"grade"), collect_set($"grade"))
  df4d.show(truncate = false)
  /*
  +----+--------------------------------+------------------------+
  |name|collect_list(grade)             |collect_set(grade)      |
  +----+--------------------------------+------------------------+
  |Amy |[65, 65, 72, 82, 85, 85, 90, 95]|[85, 82, 72, 90, 65, 95]|
  |Bob |[85, 85, 85, 95, 95, 97, 99]    |[99, 85, 97, 95]        |
  +----+--------------------------------+------------------------+
   */

  val df4e = df4 rollup ("name", "course") agg (grouping("name"), grouping("course")) sort ($"name".desc_nulls_last, $"course".desc_nulls_last)
  df4e.show(truncate = false)
  /*
  +----+------+--------------+----------------+
  |name|course|grouping(name)|grouping(course)|
  +----+------+--------------+----------------+
  |Bob |103   |0             |0               |
  |Bob |102   |0             |0               |
  |Bob |101   |0             |0               |
  |Bob |null  |0             |1               |
  |Amy |103   |0             |0               |
  |Amy |102   |0             |0               |
  |Amy |101   |0             |0               |
  |Amy |null  |0             |1               |
  |null|null  |1             |1               |
  +----+------+--------------+----------------+
   */

  val df4f = df4 cube ("name", "course") agg (grouping("name"), grouping("course")) sort ($"name".desc_nulls_last, $"course".desc_nulls_last)
  df4f.show(truncate = false)
  /*
  +----+------+--------------+----------------+
  |name|course|grouping(name)|grouping(course)|
  +----+------+--------------+----------------+
  |Bob |103   |0             |0               |
  |Bob |102   |0             |0               |
  |Bob |101   |0             |0               |
  |Bob |null  |0             |1               |
  |Amy |103   |0             |0               |
  |Amy |102   |0             |0               |
  |Amy |101   |0             |0               |
  |Amy |null  |0             |1               |
  |null|103   |1             |0               |
  |null|102   |1             |0               |
  |null|101   |1             |0               |
  |null|null  |1             |1               |
  +----+------+--------------+----------------+
   */

  val df4g = df4 rollup ("name", "course") agg grouping_id() sort ($"name".desc_nulls_last, $"course".asc_nulls_last)
  df4g.show(truncate = false)
  /*
  +----+------+-------------+
  |name|course|grouping_id()|
  +----+------+-------------+
  |Bob |101   |0            |
  |Bob |102   |0            |
  |Bob |103   |0            |
  |Bob |null  |1            |
  |Amy |101   |0            |
  |Amy |102   |0            |
  |Amy |103   |0            |
  |Amy |null  |1            |
  |null|null  |3            |
  +----+------+-------------+
   */

  val df4h = df4 cube ("name", "course") agg grouping_id() sort ($"name".desc_nulls_last, $"course".asc_nulls_last)
  df4h.show(truncate = false)
  /*
  +----+------+-------------+
  |name|course|grouping_id()|
  +----+------+-------------+
  |Bob |101   |0            |
  |Bob |102   |0            |
  |Bob |103   |0            |
  |Bob |null  |1            |
  |Amy |101   |0            |
  |Amy |102   |0            |
  |Amy |103   |0            |
  |Amy |null  |1            |
  |null|101   |2            |
  |null|102   |2            |
  |null|103   |2            |
  |null|null  |3            |
  +----+------+-------------+
   */

}
