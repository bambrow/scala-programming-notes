package spark

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{Column, Dataset, Row, SparkSession}

/**
 * This file tests the following methods:
 *   drop, withColumn, withColumnRenamed, select, join, groupBy,
 *   sort, union, rollup, filter, createOrReplaceTempView,
 *   sql, cube, agg
 * And the following sql.functions:
 *   col, lit, grouping_id
 */

object DataFrame1 extends App {

  implicit val ss: SparkSession = SparkSession.builder.master("local").getOrCreate

  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)

  import ss.implicits._

  val df1: Dataset[Row] = Seq(
    ("Alice", 18),
    ("Bob", 23),
    ("Cathy", 20),
    ("David", 27),
    ("Eva", 22),
    ("Fred", 29)
  ) toDF ("name", "age")

  val df2: Dataset[Row] = Seq(
    ("Alice", 3000),
    ("Cathy", 4500),
    ("David", 10000),
    ("Fred", 2000),
    ("George", 9000)
  ) toDF ("name", "salary")

  val df3: Dataset[Row] = Seq(
    ("Alice", 18, 3000),
    ("Cathy", 20, 4500),
    ("David", 25, 10000),
    ("Fred", 33, 2000),
    ("George", 23, 9000)
  ) toDF ("name", "age", "salary")

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

  (df1 drop "age").show
  /*
  +-----+
  | name|
  +-----+
  |Alice|
  |  Bob|
  |Cathy|
  |David|
  |  Eva|
  | Fred|
  +-----+
   */

  (df1 withColumn ("age2", col("age") + 10)).show
  /*
  +-----+---+----+
  | name|age|age2|
  +-----+---+----+
  |Alice| 18|  28|
  |  Bob| 23|  33|
  |Cathy| 20|  30|
  |David| 27|  37|
  |  Eva| 22|  32|
  | Fred| 29|  39|
  +-----+---+----+
   */

  (df1 withColumn ("age", col("age") + 10)).show
  /*
  +-----+---+
  | name|age|
  +-----+---+
  |Alice| 28|
  |  Bob| 33|
  |Cathy| 30|
  |David| 37|
  |  Eva| 32|
  | Fred| 39|
  +-----+---+
   */

  (df1 withColumnRenamed ("age", "age2")).show
  /*
  +-----+----+
  | name|age2|
  +-----+----+
  |Alice|  18|
  |  Bob|  23|
  |Cathy|  20|
  |David|  27|
  |  Eva|  22|
  | Fred|  29|
  +-----+----+
   */

  (df1 withColumn ("age2", $"age" + 1) select ("age", "age2")).show
  /*
  +---+----+
  |age|age2|
  +---+----+
  | 18|  19|
  | 23|  24|
  | 20|  21|
  | 27|  28|
  | 22|  23|
  | 29|  30|
  +---+----+
   */

  val df1a = df1 join (df2, "name")
  df1a.show
  /*
  +-----+---+------+
  | name|age|salary|
  +-----+---+------+
  |Alice| 18|  3000|
  |Cathy| 20|  4500|
  |David| 27| 10000|
  | Fred| 29|  2000|
  +-----+---+------+
   */

  // val df1b = df1 join (df2, df1.col("name") === df2.col("name").as("name2"), "left") drop "name2"
  // df1b.show

  // this does not work as expected
  // column name should be changed prior to joining

  val df1b = df1 join (df2, df1.col("name") === df2.col("name"), "left") drop df2.col("name")
  df1b.show
  /*
  +-----+---+------+
  | name|age|salary|
  +-----+---+------+
  |Alice| 18|  3000|
  |  Bob| 23|  null|
  |Cathy| 20|  4500|
  |David| 27| 10000|
  |  Eva| 22|  null|
  | Fred| 29|  2000|
  +-----+---+------+
   */

  val df1c = df1 join (df3, Seq("name", "age"))
  df1c.show
  /*
  +-----+---+------+
  | name|age|salary|
  +-----+---+------+
  |Alice| 18|  3000|
  |Cathy| 20|  4500|
  +-----+---+------+
   */

  val joinCols: Seq[Column] = Seq("name", "age") map df3.col
  val allCols: Seq[Column] = (df1.columns map df1.col) ++ (df3.columns map df3.col)
  val remainingCols: Seq[Column] = allCols diff joinCols
  val df1d = df1 join (df3, df1("name") === df3("name") && df1("age") === df3("age"), "left") select (remainingCols: _*)
  df1d.show
  /*
  +-----+---+------+
  | name|age|salary|
  +-----+---+------+
  |Alice| 18|  3000|
  |  Bob| 23|  null|
  |Cathy| 20|  4500|
  |David| 27|  null|
  |  Eva| 22|  null|
  | Fred| 29|  null|
  +-----+---+------+
   */

  // val df1e = df1 join (df3, df1("name") === df3("name") && df1("age") === df3("age"), "left") drop (joinCols reduce (_ && _))
  // df1e.show

  // this does not work as expected
  // column expr should not be used when column itself is required
  // although column expr and column both have same type: Column

  val df1e = df1 join (df3, df1("name") === df3("name") || df1("age") === df3("age"))
  df1e.show
  /*
  +-----+---+------+---+------+
  | name|age|  name|age|salary|
  +-----+---+------+---+------+
  |Alice| 18| Alice| 18|  3000|
  |  Bob| 23|George| 23|  9000|
  |Cathy| 20| Cathy| 20|  4500|
  |David| 27| David| 25| 10000|
  | Fred| 29|  Fred| 33|  2000|
  +-----+---+------+---+------+
   */

  (df4 groupBy "name" agg (avg($"grade"), max($"course"))).show

  (df4 groupBy "name" agg Map("grade" -> "avg", "course" -> "max")).show

  (df4 groupBy "name" agg ("grade" -> "avg", "course" -> "max")).show

  /*
  +----+-----------------+-----------+
  |name|       avg(grade)|max(course)|
  +----+-----------------+-----------+
  | Amy|           79.875|        103|
  | Bob|91.57142857142857|        103|
  +----+-----------------+-----------+
   */

  val df4a = df4 groupBy ("name", "course") agg (avg("grade") as "grade")
  df4a.show
  /*
  +----+------+-----------------+
  |name|course|            grade|
  +----+------+-----------------+
  | Amy|   103|             86.0|
  | Amy|   102|             75.0|
  | Bob|   102|92.33333333333333|
  | Bob|   101|             90.0|
  | Bob|   103|             92.0|
  | Amy|   101|            79.25|
  +----+------+-----------------+
   */

  val df4b = df4 groupBy ("name", "course") agg (sum("grade") as "grade") union {
    df4 groupBy "name" agg (sum("grade") as "grade") select ($"name", lit(null) as "course", $"grade")
  } sort ($"name".desc_nulls_last, $"course".asc_nulls_last)
  df4b.show
  /*
  +----+------+-----+
  |name|course|grade|
  +----+------+-----+
  | Bob|   101|  180|
  | Bob|   102|  277|
  | Bob|   103|  184|
  | Bob|  null|  641|
  | Amy|   101|  317|
  | Amy|   102|  150|
  | Amy|   103|  172|
  | Amy|  null|  639|
  +----+------+-----+
   */

  // rollup operator is equivalent to GROUP BY ... WITH ROLLUP in SQL
  // (which in turn is equivalent to GROUP BY ... GROUPING SETS ((a,b,c),(a,b),(a),()) when used with 3 columns: a, b, and c)

  val df4c = df4 rollup ("name", "course") agg (sum("grade") as "grade", grouping_id() as "gid") sort ($"name".desc_nulls_last, $"course".asc_nulls_last)
  df4c.show
  /*
  +----+------+-----+---+
  |name|course|grade|gid|
  +----+------+-----+---+
  | Bob|   101|  180|  0|
  | Bob|   102|  277|  0|
  | Bob|   103|  184|  0|
  | Bob|  null|  641|  1|
  | Amy|   101|  317|  0|
  | Amy|   102|  150|  0|
  | Amy|   103|  172|  0|
  | Amy|  null|  639|  1|
  |null|  null| 1280|  3|
  +----+------+-----+---+
   */

  (df4c filter ($"gid" =!= 3) drop "gid").show
  /*
  +----+------+-----+
  |name|course|grade|
  +----+------+-----+
  | Bob|   101|  180|
  | Bob|   102|  277|
  | Bob|   103|  184|
  | Bob|  null|  641|
  | Amy|   101|  317|
  | Amy|   102|  150|
  | Amy|   103|  172|
  | Amy|  null|  639|
  +----+------+-----+
   */

  df4 createOrReplaceTempView "df4"
  val df4d = ss sql
    """ select name, course, sum(grade) as grade
      |  from df4
      |  group by name, course
      |  grouping sets ((name, course), (name))
      |  order by name desc nulls last, course asc nulls last
      |""".stripMargin
  df4d.show
  /*
  +----+------+-----+
  |name|course|grade|
  +----+------+-----+
  | Bob|   101|  180|
  | Bob|   102|  277|
  | Bob|   103|  184|
  | Bob|  null|  641|
  | Amy|   101|  317|
  | Amy|   102|  150|
  | Amy|   103|  172|
  | Amy|  null|  639|
  +----+------+-----+
   */

  // cube is more than rollup operator, i.e. cube does rollup with aggregation over all the missing combinations given the columns.

  val df4e = df4 cube ("name", "course") agg (sum("grade") as "grade", grouping_id() as "gid") sort ($"name".desc_nulls_last, $"course".asc_nulls_last)
  df4e.show
  /*
  +----+------+-----+---+
  |name|course|grade|gid|
  +----+------+-----+---+
  | Bob|   101|  180|  0|
  | Bob|   102|  277|  0|
  | Bob|   103|  184|  0|
  | Bob|  null|  641|  1|
  | Amy|   101|  317|  0|
  | Amy|   102|  150|  0|
  | Amy|   103|  172|  0|
  | Amy|  null|  639|  1|
  |null|   101|  497|  2|
  |null|   102|  427|  2|
  |null|   103|  356|  2|
  |null|  null| 1280|  3|
  +----+------+-----+---+
   */

  val df4f = ss sql
    """ select name, course, sum(grade) as grade
      |  from df4
      |  group by name, course
      |  grouping sets ((name, course), (name), (course), ())
      |  order by name desc nulls last, course asc nulls last
      |""".stripMargin
  df4f.show
  /*
  +----+------+-----+
  |name|course|grade|
  +----+------+-----+
  | Bob|   101|  180|
  | Bob|   102|  277|
  | Bob|   103|  184|
  | Bob|  null|  641|
  | Amy|   101|  317|
  | Amy|   102|  150|
  | Amy|   103|  172|
  | Amy|  null|  639|
  |null|   101|  497|
  |null|   102|  427|
  |null|   103|  356|
  |null|  null| 1280|
  +----+------+-----+
   */

}
