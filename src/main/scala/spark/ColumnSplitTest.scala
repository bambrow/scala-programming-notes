package spark

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{Dataset, Row, SparkSession}
import ColumnSplit._

object ColumnSplitTest extends App {

  implicit val ss: SparkSession = SparkSession.builder.master("local").getOrCreate

  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)

  import ss.implicits._

  val df1: Dataset[Row] = Seq(
    ("Alice", Array(1,2,3)),
    ("Bob", Array(4,5,6)),
    ("Cathy", Array(7,8,9))
  ) toDF ("name", "numbers")

  val df2: Dataset[Row] = Seq(
    ("Alice", "1.2.3"),
    ("Bob", "4.5.6"),
    ("Cathy", "7.8.9")
  ) toDF ("name", "numbers")

  val df3 = df1 splitArray ("numbers", Seq("col1", "col2", "col3"))
  df3.show
  /*
  +-----+----+----+----+
  | name|col1|col2|col3|
  +-----+----+----+----+
  |Alice|   1|   2|   3|
  |  Bob|   4|   5|   6|
  |Cathy|   7|   8|   9|
  +-----+----+----+----+
   */

  val df4 = df2 splitColumn ("numbers", Seq("col1", "col2", "col3"), "\\.")
  df4.show
  /*
  +-----+----+----+----+
  | name|col1|col2|col3|
  +-----+----+----+----+
  |Alice|   1|   2|   3|
  |  Bob|   4|   5|   6|
  |Cathy|   7|   8|   9|
  +-----+----+----+----+
   */

}
