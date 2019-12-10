package spark

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{Column, Dataset, Row, SparkSession}

/**
 * This file tests the following sql.functions:
 *   when...otherwise, lit, coalesce
 * And the following techniques:
 *   aggExpr, joinExpr, selectExpr
 */

object DataFrameExtra1 extends App {

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

  val df5: Dataset[Row] = Seq(
    ("Alice", 65, 101),
    ("Alice", 65, 102),
    ("Alice", 72, 101),
    ("Alice", 82, 103),
    ("Alice", 85, 101),
    ("Alice", 85, 102),
    ("Alice", 90, 103),
    ("Alice", 95, 101),
    ("Bob", 85, 101),
    ("Bob", 85, 103),
    ("Bob", 85, 102),
    ("Bob", 95, 102),
    ("Bob", 95, 101),
    ("Bob", 97, 102),
    ("Bob", 99, 103)
  ) toDF ("name", "grade", "course")

  val df1a = df1 join (df3, df1("name") === df3("name"), "outer")
  df1a.show
  /*
  +-----+----+------+----+------+
  | name| age|  name| age|salary|
  +-----+----+------+----+------+
  | Fred|  29|  Fred|  33|  2000|
  |  Eva|  22|  null|null|  null|
  |  Bob|  23|  null|null|  null|
  |Alice|  18| Alice|  18|  3000|
  | null|null|George|  23|  9000|
  |David|  27| David|  25| 10000|
  |Cathy|  20| Cathy|  20|  4500|
  +-----+----+------+----+------+
   */

  // null tests
  // in calculation, any null value will cause the final result to be null
  (df1a withColumn ("name_diff", df1("age") - df3("age"))).show
  /*
  +-----+----+------+----+------+---------+
  | name| age|  name| age|salary|name_diff|
  +-----+----+------+----+------+---------+
  | Fred|  29|  Fred|  33|  2000|       -4|
  |  Eva|  22|  null|null|  null|     null|
  |  Bob|  23|  null|null|  null|     null|
  |Alice|  18| Alice|  18|  3000|        0|
  | null|null|George|  23|  9000|     null|
  |David|  27| David|  25| 10000|        2|
  |Cathy|  20| Cathy|  20|  4500|        0|
  +-----+----+------+----+------+---------+
   */

  // to fix this, use the following two methods
  (df1a withColumn ("name_diff", when(df1("age").isNull, lit(0)).otherwise(df1("age")) - when(df3("age").isNull, lit(0)).otherwise(df3("age")))).show
  /*
  +-----+----+------+----+------+---------+
  | name| age|  name| age|salary|name_diff|
  +-----+----+------+----+------+---------+
  | Fred|  29|  Fred|  33|  2000|       -4|
  |  Eva|  22|  null|null|  null|       22|
  |  Bob|  23|  null|null|  null|       23|
  |Alice|  18| Alice|  18|  3000|        0|
  | null|null|George|  23|  9000|      -23|
  |David|  27| David|  25| 10000|        2|
  |Cathy|  20| Cathy|  20|  4500|        0|
  +-----+----+------+----+------+---------+
   */

  (df1a withColumn ("name_diff", coalesce(df1("age"), lit(0)) - coalesce(df3("age"), lit(0)))).show
  /*
  +-----+----+------+----+------+---------+
  | name| age|  name| age|salary|name_diff|
  +-----+----+------+----+------+---------+
  | Fred|  29|  Fred|  33|  2000|       -4|
  |  Eva|  22|  null|null|  null|       22|
  |  Bob|  23|  null|null|  null|       23|
  |Alice|  18| Alice|  18|  3000|        0|
  | null|null|George|  23|  9000|      -23|
  |David|  27| David|  25| 10000|        2|
  |Cathy|  20| Cathy|  20|  4500|        0|
  +-----+----+------+----+------+---------+
   */

  // custom group and agg method
  def groupAndSumColumns(df: Dataset[Row], keys: Seq[String], aggExpr: Seq[Column]): Dataset[Row] = {
    df groupBy (keys.head, keys.tail: _*) agg (aggExpr.head, aggExpr.tail: _*)
  }

  // wrong method
  // this method will give wrong outputs because of the duplicate column names
  def joinAndSelectAllColumns(left: Dataset[Row], right: Dataset[Row], keys: Seq[String], cols: Seq[String]): Dataset[Row] = {
    val keysLeft = keys map left.col
    val keysRight = keys map right.col
    val colsLeft = cols map left.col
    val colsRight = cols map right.col
    val joinExpr = keysLeft zip keysRight map { case (c1, c2) => c1 === c2 } reduce (_ && _)
    left join(right, joinExpr, "full") select (keysLeft ++ keysRight ++ colsLeft ++ colsRight: _*)
  }

  def addSuffixToAllColumns(df: Dataset[Row], suffix: String): Dataset[Row] = {
    val selectExpr = df.columns zip (df.columns map (_ + suffix)) map { case (c1, c2) => df(c1) as c2 }
    df select (selectExpr: _*)
  }

  // this method will give correct results
  def joinAndSelectAllColumns(left: Dataset[Row], right: Dataset[Row], keysLeft: Seq[String], keysRight: Seq[String], colsLeft: Seq[String], colsRight: Seq[String]): Dataset[Row] = {
    val joinExpr = (keysLeft map left.col) zip (keysRight map right.col) map { case (c1, c2) => c1 === c2 } reduce (_ && _)
    left join (right, joinExpr, "outer") select ((keysLeft ++ keysRight ++ colsLeft ++ colsRight) map col: _*)
  }

  val keys = Seq("name")
  val aggExpr = Seq(sum("grade") as "grade", sum("course") as "course")
  val cols = Seq("grade", "course")

  val df4a = groupAndSumColumns(df4, keys, aggExpr)
  val df5a = groupAndSumColumns(df5, keys, aggExpr)
  df4a.show
  df5a.show
  /*
  +----+-----+------+
  |name|grade|course|
  +----+-----+------+
  | Amy|  639|   814|
  | Bob|  641|   714|
  +----+-----+------+

  +-----+-----+------+
  | name|grade|course|
  +-----+-----+------+
  |  Bob|  641|   714|
  |Alice|  639|   814|
  +-----+-----+------+
   */

  val df6 = joinAndSelectAllColumns(df4a, df5a, keys, cols)
  df6.show
  // wrong outputs
  /*
  +----+-----+-----+------+-----+------+
  |name| name|grade|course|grade|course|
  +----+-----+-----+------+-----+------+
  | Amy| null|  639|   814|  639|   814|
  | Bob|  Bob|  641|   714|  641|   714|
  |null|Alice| null|  null| null|  null|
  +----+-----+-----+------+-----+------+
   */

  val df4b = addSuffixToAllColumns(df4a, suffix = "_1")
  val df5b = addSuffixToAllColumns(df5a, suffix = "_2")
  val df7 = joinAndSelectAllColumns(df4b, df5b, keys map (_ + "_1"), keys map (_ + "_2"), cols map (_ + "_1"), cols map (_ + "_2"))
  df7.show
  // correct outputs
  /*
  +------+------+-------+--------+-------+--------+
  |name_1|name_2|grade_1|course_1|grade_2|course_2|
  +------+------+-------+--------+-------+--------+
  |   Amy|  null|    639|     814|   null|    null|
  |   Bob|   Bob|    641|     714|    641|     714|
  |  null| Alice|   null|    null|    639|     814|
  +------+------+-------+--------+-------+--------+
   */

}
