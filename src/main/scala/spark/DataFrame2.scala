package spark

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{Column, Dataset, Row, SparkSession}

/**
 * This file tests the following Column methods:
 *   alias, as, name, =!=, ===, <=>, asc, asc_nulls_first, asc_nulls_last,
 *   desc, desc_nulls_first, desc_nulls_last, cast, contains, startsWith,
 *   endsWith, unary_!, unary_-, isInCollection, isin, isNaN, isNull, isNotNull,
 *   substr, length, lit, like, rlike
 * And the following techniques:
 *   selectExpr, joinExpr
 */

object DataFrame2 extends App {

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

  println(df1 col "name") // name
  println(df1("name"))
  println(df1.col("name") === df2.col("name")) // (name = name)
  println(col("name") === col("name")) // (name = name)
  println(df1.col("name") === col("name")) // (name = name)
  println(df1.col("name") == df2.col("name")) // false
  println(col("name") == col("name")) // true
  println(df1.col("name") == col("name")) // false
  println(df1.col("name").toString) // name
  println(df1.col("name").toString == df2.col("name").toString) // true

  println

  println(df1 col "name" alias "name2") // name AS `name2`
  println(df1 col "name" as "name2") // name AS `name2`
  println(df1 col "name" name "name2") // name AS `name2`

  println

  val columns: Seq[String] = Seq("name", "age")
  val selectExpr1: Seq[Column] = columns zip (columns map (_ + "2")) map {
    case (c1: String, c2: String) => df1(c1) as c2
  }

  df1.select(selectExpr1: _*).show
  /*
  +-----+----+
  |name2|age2|
  +-----+----+
  |Alice|  18|
  |  Bob|  23|
  |Cathy|  20|
  |David|  27|
  |  Eva|  22|
  | Fred|  29|
  +-----+----+
   */

  println

  val joinExpr1: Column = (columns map df1.col) zip (columns map df3.col) map {
    case (c1: Column, c2: Column) => c1 === c2
  } reduce (_ && _)

  val df4 = df1.join(df3, joinExpr1, "outer")
  df4.show
  /*
  +-----+----+------+----+------+
  | name| age|  name| age|salary|
  +-----+----+------+----+------+
  | Fred|  29|  null|null|  null|
  |David|  27|  null|null|  null|
  | null|null| David|  25| 10000|
  |  Bob|  23|  null|null|  null|
  |  Eva|  22|  null|null|  null|
  | null|null|  Fred|  33|  2000|
  | null|null|George|  23|  9000|
  |Cathy|  20| Cathy|  20|  4500|
  |Alice|  18| Alice|  18|  3000|
  +-----+----+------+----+------+
   */

  println

  val selectExpr2: Seq[Column] = columns zip (columns map (_ + "_df1")) map {
    case (c1: String, c2: String) => df1(c1) as c2
  } union {
    columns zip (columns map (_ + "_df3")) map {
      case (c1: String, c2: String) => df3(c1) as c2
    }
  }

  df4.select(selectExpr2: _*).show
  /*
  +--------+-------+--------+-------+
  |name_df1|age_df1|name_df3|age_df3|
  +--------+-------+--------+-------+
  |    Fred|     29|    null|   null|
  |   David|     27|    null|   null|
  |    null|   null|   David|     25|
  |     Bob|     23|    null|   null|
  |     Eva|     22|    null|   null|
  |    null|   null|    Fred|     33|
  |    null|   null|  George|     23|
  |   Cathy|     20|   Cathy|     20|
  |   Alice|     18|   Alice|     18|
  +--------+-------+--------+-------+
   */

  println

  val joinExpr2: Column = (columns map df1.col) zip (columns map df3.col) map {
    case (c1: Column, c2: Column) => c1 === c2
  } reduce (_ || _)

  val df5 = df1.join(df3, joinExpr2, "outer")
  df5.show
  /*
  +-----+---+------+----+------+
  | name|age|  name| age|salary|
  +-----+---+------+----+------+
  |Alice| 18| Alice|  18|  3000|
  |  Bob| 23|George|  23|  9000|
  |Cathy| 20| Cathy|  20|  4500|
  |David| 27| David|  25| 10000|
  |  Eva| 22|  null|null|  null|
  | Fred| 29|  Fred|  33|  2000|
  +-----+---+------+----+------+
   */

  println

  (df5 filter (df1("name") =!= df3("name"))).show
  /*
  +----+---+------+---+------+
  |name|age|  name|age|salary|
  +----+---+------+---+------+
  | Bob| 23|George| 23|  9000|
  +----+---+------+---+------+
   */

  // <=> is safe for null values
  (df5 filter (df1("name") <=> df3("name"))).show
  /*
  +-----+---+-----+---+------+
  | name|age| name|age|salary|
  +-----+---+-----+---+------+
  |Alice| 18|Alice| 18|  3000|
  |Cathy| 20|Cathy| 20|  4500|
  |David| 27|David| 25| 10000|
  | Fred| 29| Fred| 33|  2000|
  +-----+---+-----+---+------+
   */

  (df5 filter (df1("name") === df3("name"))).show
  /*
  +-----+---+-----+---+------+
  | name|age| name|age|salary|
  +-----+---+-----+---+------+
  |Alice| 18|Alice| 18|  3000|
  |Cathy| 20|Cathy| 20|  4500|
  |David| 27|David| 25| 10000|
  | Fred| 29| Fred| 33|  2000|
  +-----+---+-----+---+------+
   */

  (df4 sort df1("name").asc).show
  /*
  +-----+----+------+----+------+
  | name| age|  name| age|salary|
  +-----+----+------+----+------+
  | null|null| David|  25| 10000|
  | null|null|  Fred|  33|  2000|
  | null|null|George|  23|  9000|
  |Alice|  18| Alice|  18|  3000|
  |  Bob|  23|  null|null|  null|
  |Cathy|  20| Cathy|  20|  4500|
  |David|  27|  null|null|  null|
  |  Eva|  22|  null|null|  null|
  | Fred|  29|  null|null|  null|
  +-----+----+------+----+------+
   */

  (df4 sort df1("name").asc_nulls_first).show
  // same as asc

  (df4 sort df1("name").asc_nulls_last).show
  /*
  +-----+----+------+----+------+
  | name| age|  name| age|salary|
  +-----+----+------+----+------+
  |Alice|  18| Alice|  18|  3000|
  |  Bob|  23|  null|null|  null|
  |Cathy|  20| Cathy|  20|  4500|
  |David|  27|  null|null|  null|
  |  Eva|  22|  null|null|  null|
  | Fred|  29|  null|null|  null|
  | null|null| David|  25| 10000|
  | null|null|  Fred|  33|  2000|
  | null|null|George|  23|  9000|
  +-----+----+------+----+------+
   */

  (df4 sort df1("name").desc).show
  /*
  +-----+----+------+----+------+
  | name| age|  name| age|salary|
  +-----+----+------+----+------+
  | Fred|  29|  null|null|  null|
  |  Eva|  22|  null|null|  null|
  |David|  27|  null|null|  null|
  |Cathy|  20| Cathy|  20|  4500|
  |  Bob|  23|  null|null|  null|
  |Alice|  18| Alice|  18|  3000|
  | null|null| David|  25| 10000|
  | null|null|  Fred|  33|  2000|
  | null|null|George|  23|  9000|
  +-----+----+------+----+------+
   */

  (df4 sort df1("name").desc_nulls_first).show
  /*
  +-----+----+------+----+------+
  | name| age|  name| age|salary|
  +-----+----+------+----+------+
  | null|null| David|  25| 10000|
  | null|null|  Fred|  33|  2000|
  | null|null|George|  23|  9000|
  | Fred|  29|  null|null|  null|
  |  Eva|  22|  null|null|  null|
  |David|  27|  null|null|  null|
  |Cathy|  20| Cathy|  20|  4500|
  |  Bob|  23|  null|null|  null|
  |Alice|  18| Alice|  18|  3000|
  +-----+----+------+----+------+
   */

  (df4 sort df1("name").desc_nulls_last).show
  // same as desc

  (df1 withColumn ("age2", $"age" cast "string")).show
  /*
  +-----+---+----+
  | name|age|age2|
  +-----+---+----+
  |Alice| 18|  18|
  |  Bob| 23|  23|
  |Cathy| 20|  20|
  |David| 27|  27|
  |  Eva| 22|  22|
  | Fred| 29|  29|
  +-----+---+----+
   */

  (df1 withColumn ("name2", $"name" contains "a")).show
  /*
  +-----+---+-----+
  | name|age|name2|
  +-----+---+-----+
  |Alice| 18|false|
  |  Bob| 23|false|
  |Cathy| 20| true|
  |David| 27| true|
  |  Eva| 22| true|
  | Fred| 29|false|
  +-----+---+-----+
   */

  (df1 withColumn ("name2", $"name" startsWith "C")).show
  /*
  +-----+---+-----+
  | name|age|name2|
  +-----+---+-----+
  |Alice| 18|false|
  |  Bob| 23|false|
  |Cathy| 20| true|
  |David| 27|false|
  |  Eva| 22|false|
  | Fred| 29|false|
  +-----+---+-----+
   */

  (df1 withColumn ("name2", $"name" endsWith "y")).show
  /*
  +-----+---+-----+
  | name|age|name2|
  +-----+---+-----+
  |Alice| 18|false|
  |  Bob| 23|false|
  |Cathy| 20| true|
  |David| 27|false|
  |  Eva| 22|false|
  | Fred| 29|false|
  +-----+---+-----+
   */

  (df1 withColumn ("name2", ($"name" startsWith "C").unary_!)).show
  /*
  +-----+---+-----+
  | name|age|name2|
  +-----+---+-----+
  |Alice| 18| true|
  |  Bob| 23| true|
  |Cathy| 20|false|
  |David| 27| true|
  |  Eva| 22| true|
  | Fred| 29| true|
  +-----+---+-----+
   */

  (df1 withColumn ("age2", $"age".unary_-)).show
  /*
  +-----+---+----+
  | name|age|age2|
  +-----+---+----+
  |Alice| 18| -18|
  |  Bob| 23| -23|
  |Cathy| 20| -20|
  |David| 27| -27|
  |  Eva| 22| -22|
  | Fred| 29| -29|
  +-----+---+----+
   */

  val df2Names = (df2 select "name").collect map (_.toString) map { s => s substring (1, s.length -1) }

  (df1 withColumn ("name2", $"name" isInCollection df2Names)).show
  /*
  +-----+---+-----+
  | name|age|name2|
  +-----+---+-----+
  |Alice| 18| true|
  |  Bob| 23|false|
  |Cathy| 20| true|
  |David| 27| true|
  |  Eva| 22|false|
  | Fred| 29| true|
  +-----+---+-----+
   */

  (df1 withColumn ("name2", $"name" isin (df2Names: _*))).show
  /*
  +-----+---+-----+
  | name|age|name2|
  +-----+---+-----+
  |Alice| 18| true|
  |  Bob| 23|false|
  |Cathy| 20| true|
  |David| 27| true|
  |  Eva| 22|false|
  | Fred| 29| true|
  +-----+---+-----+
   */

  // skipped: isNaN, isNull, isNotNull

  (df1 withColumn ("name2", $"name" substr (2, 2))).show
  /*
  +-----+---+-----+
  | name|age|name2|
  +-----+---+-----+
  |Alice| 18|   li|
  |  Bob| 23|   ob|
  |Cathy| 20|   at|
  |David| 27|   av|
  |  Eva| 22|   va|
  | Fred| 29|   re|
  +-----+---+-----+
   */
  // substr is one-indexed

  val df6 = df1 withColumn ("name_length", length($"name")) withColumn ("name_start", lit(length($"name") % 2))
  (df6 withColumn ("name_sub", $"name" substr ($"name_start" + 1, $"name_length" - 1))).show
  /*
  +-----+---+-----------+----------+--------+
  | name|age|name_length|name_start|name_sub|
  +-----+---+-----------+----------+--------+
  |Alice| 18|          5|         1|    lice|
  |  Bob| 23|          3|         1|      ob|
  |Cathy| 20|          5|         1|    athy|
  |David| 27|          5|         1|    avid|
  |  Eva| 22|          3|         1|      va|
  | Fred| 29|          4|         0|     Fre|
  +-----+---+-----------+----------+--------+
   */

  (df1 withColumn ("name2", $"name" like "%a%")).show
  /*
  +-----+---+-----+
  | name|age|name2|
  +-----+---+-----+
  |Alice| 18|false|
  |  Bob| 23|false|
  |Cathy| 20| true|
  |David| 27| true|
  |  Eva| 22| true|
  | Fred| 29|false|
  +-----+---+-----+
   */

  (df1 withColumn ("name2", $"name" rlike ".*a.*")).show
  /*
  +-----+---+-----+
  | name|age|name2|
  +-----+---+-----+
  |Alice| 18|false|
  |  Bob| 23|false|
  |Cathy| 20| true|
  |David| 27| true|
  |  Eva| 22| true|
  | Fred| 29|false|
  +-----+---+-----+
   */

}
