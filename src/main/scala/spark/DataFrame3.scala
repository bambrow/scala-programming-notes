package spark

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import org.apache.spark.sql.{Column, Dataset, Row, SparkSession}

/**
 * This file tests the following method:
 *   parallelize
 * And the following Column methods:
 *   getItem, getField
 * And the following sql.functions:
 *   collect_list, collect_set
 * And the following data structures:
 *   StructType, StructField
 */

object DataFrame3 extends App {

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

  val df5 = df4 groupBy "name" agg (collect_list("grade") as "grades", collect_list("course") as "courses")
  df5.show
  /*
  +----+--------------------+--------------------+
  |name|              grades|             courses|
  +----+--------------------+--------------------+
  | Amy|[65, 65, 72, 82, ...|[101, 102, 101, 1...|
  | Bob|[85, 85, 85, 95, ...|[101, 103, 102, 1...|
  +----+--------------------+--------------------+
   */

  val df6 = df4 groupBy "name" agg (collect_set("grade") as "grades", collect_set("course") as "courses")
  df6.show
  /*
  +----+--------------------+---------------+
  |name|              grades|        courses|
  +----+--------------------+---------------+
  | Amy|[85, 82, 72, 90, ...|[102, 103, 101]|
  | Bob|    [99, 85, 97, 95]|[102, 103, 101]|
  +----+--------------------+---------------+
   */

  val schema1 = StructType(
    Seq(
      StructField("name", StringType, nullable = true),
      StructField("age", IntegerType, nullable = true),
      StructField("graduated", BooleanType, nullable = true)
    )
  )

  println(schema1("name")) // StructField(name,StringType,true)
  println(schema1("age")) // StructField(age,IntegerType,true)
  println(schema1("graduated")) // StructField(graduated,BooleanType,true)
  println(schema1(Set("name", "age"))) // StructType(StructField(name,StringType,true), StructField(age,IntegerType,true))
  println(Row("Alice", 27, true)) // [Alice,27,true]

  val data1 = Seq(
    Row("Alice", 27, true),
    Row("Bob", 33, false),
    Row("Cathy", 29, true)
  )

  val df1 = ss createDataFrame (ss.sparkContext parallelize data1, schema1)
  df1.show
  /*
  +-----+---+---------+
  | name|age|graduated|
  +-----+---+---------+
  |Alice| 27|     true|
  |  Bob| 33|    false|
  |Cathy| 29|     true|
  +-----+---+---------+
   */

  println(df1.schema)
  // StructType(StructField(name,StringType,true), StructField(age,IntegerType,true), StructField(graduated,BooleanType,true))

  df1.printSchema
  /*
  root
   |-- name: string (nullable = true)
   |-- age: integer (nullable = true)
   |-- graduated: boolean (nullable = true)
   */

  (df5 withColumn ("c1", $"courses" getItem 0)).show
  /*
  +----+--------------------+--------------------+---+
  |name|              grades|             courses| c1|
  +----+--------------------+--------------------+---+
  | Amy|[65, 65, 72, 82, ...|[101, 102, 101, 1...|101|
  | Bob|[85, 85, 85, 95, ...|[101, 103, 102, 1...|101|
  +----+--------------------+--------------------+---+
   */

  val selectExpr1: Seq[Column] = df5.columns.toSeq map df5.col union {
    Seq("c1", "c2", "c3").zipWithIndex map {
      case (col: String, index: Int) => $"courses" getItem index as col
    }
  }

  (df5 select (selectExpr1: _*)).show
  /*
  +----+--------------------+--------------------+---+---+---+
  |name|              grades|             courses| c1| c2| c3|
  +----+--------------------+--------------------+---+---+---+
  | Amy|[65, 65, 72, 82, ...|[101, 102, 101, 1...|101|102|101|
  | Bob|[85, 85, 85, 95, ...|[101, 103, 102, 1...|101|103|102|
  +----+--------------------+--------------------+---+---+---+
   */

  val schema2 = StructType(
    Seq(
      StructField("name", StringType, nullable = true),
      StructField("age", IntegerType, nullable = true)
    )
  )

  val schema3 = StructType(
    Seq(
      StructField("name", StringType, nullable = true),
      StructField("age", IntegerType, nullable = true),
      StructField("friend", schema2, nullable = true)
    )
  )

  val schema4 = StructType(
    Seq(
      StructField("name", StringType, nullable = true),
      StructField("age", IntegerType, nullable = true),
      StructField("friends", ArrayType(schema2), nullable = true)
    )
  )

  val schema5 = StructType(
    Seq(
      StructField("name", StringType, nullable = true),
      StructField("age", IntegerType, nullable = true),
      StructField("friend", MapType(StringType, StringType, valueContainsNull = true), nullable = true)
    )
  )

  schema3.printTreeString
  /*
  root
   |-- name: string (nullable = true)
   |-- age: integer (nullable = true)
   |-- friend: struct (nullable = true)
   |    |-- name: string (nullable = true)
   |    |-- age: integer (nullable = true)
   */

  schema4.printTreeString
  /*
  root
   |-- name: string (nullable = true)
   |-- age: integer (nullable = true)
   |-- friends: array (nullable = true)
   |    |-- element: struct (containsNull = true)
   |    |    |-- name: string (nullable = true)
   |    |    |-- age: integer (nullable = true)
   */

  schema5.printTreeString
  /*
  root
   |-- name: string (nullable = true)
   |-- age: integer (nullable = true)
   |-- friend: map (nullable = true)
   |    |-- key: string
   |    |-- value: string (valueContainsNull = true)
   */

  val data3 = Seq(
    Row("Alice", 27, Row("David", 29)),
    Row("Bob", 33, Row("George", 39)),
    Row("Cathy", 29, Row("John", 30))
  )

  val data4 = Seq(
    Row("Alice", 27, Seq(
      Row("David", 29),
      Row("Eva", 19),
      Row("Fred", 22)
    )),
    Row("Bob", 33, Seq(
      Row("George", 39),
      Row("Helen", 22)
    )),
    Row("Cathy", 29, Seq(
      Row("John", 30),
      Row("Katie", 39),
      Row("Linda", 20)
    ))
  )

  val data5 = Seq(
    Row("Alice", 27, Map(
      ("name", "David"),
      ("age", "29")
    )),
    Row("Bob", 33, Map(
      ("name", "George"),
      ("age", "39")
    )),
    Row("Cathy", 29, Map(
      ("name", "John"),
      ("age", "30"),
    ))
  )

  val df7 = ss createDataFrame (ss.sparkContext parallelize data3, schema3)
  df7.show(truncate = false)
  /*
  +-----+---+------------+
  | name|age|      friend|
  +-----+---+------------+
  |Alice| 27| [David, 29]|
  |  Bob| 33|[George, 39]|
  |Cathy| 29|  [John, 30]|
  +-----+---+------------+
   */

  val df8 = ss createDataFrame (ss.sparkContext parallelize data4, schema4)
  df8.show(truncate = false)
  /*
  +-----+---+--------------------------------------+
  |name |age|friends                               |
  +-----+---+--------------------------------------+
  |Alice|27 |[[David, 29], [Eva, 19], [Fred, 22]]  |
  |Bob  |33 |[[George, 39], [Helen, 22]]           |
  |Cathy|29 |[[John, 30], [Katie, 39], [Linda, 20]]|
  +-----+---+--------------------------------------+
   */

  val df9 = ss createDataFrame (ss.sparkContext parallelize data5, schema5)
  df9.show(truncate = false)
  /*
  +-----+---+---------------------------+
  |name |age|friend                     |
  +-----+---+---------------------------+
  |Alice|27 |[name -> David, age -> 29] |
  |Bob  |33 |[name -> George, age -> 39]|
  |Cathy|29 |[name -> John, age -> 30]  |
  +-----+---+---------------------------+
   */

  // getItem for ArrayType
  (df8 withColumn ("c1", $"friends" getItem 0)).show(false)
  /*
  +-----+---+--------------------------------------+------------+
  |name |age|friends                               |c1          |
  +-----+---+--------------------------------------+------------+
  |Alice|27 |[[David, 29], [Eva, 19], [Fred, 22]]  |[David, 29] |
  |Bob  |33 |[[George, 39], [Helen, 22]]           |[George, 39]|
  |Cathy|29 |[[John, 30], [Katie, 39], [Linda, 20]]|[John, 30]  |
  +-----+---+--------------------------------------+------------+
   */

  (df8 withColumn ("friend0_name", $"friends"(0)("name")) withColumn ("friend0_age", $"friends"(0)("age"))).show(false)
  /*
  +-----+---+--------------------------------------+------------+-----------+
  |name |age|friends                               |friend0_name|friend0_age|
  +-----+---+--------------------------------------+------------+-----------+
  |Alice|27 |[[David, 29], [Eva, 19], [Fred, 22]]  |David       |29         |
  |Bob  |33 |[[George, 39], [Helen, 22]]           |George      |39         |
  |Cathy|29 |[[John, 30], [Katie, 39], [Linda, 20]]|John        |30         |
  +-----+---+--------------------------------------+------------+-----------+
   */

  // getItem for MapType
  (df9 withColumn ("c1", $"friend" getItem "name")).show(false)
  /*
  +-----+---+---------------------------+------+
  |name |age|friend                     |c1    |
  +-----+---+---------------------------+------+
  |Alice|27 |[name -> David, age -> 29] |David |
  |Bob  |33 |[name -> George, age -> 39]|George|
  |Cathy|29 |[name -> John, age -> 30]  |John  |
  +-----+---+---------------------------+------+
   */

  (df9 withColumn ("friend_name", $"friend"("name")) withColumn ("friend_age", $"friend"("age") cast "int")).show(false)
  /*
  +-----+---+---------------------------+-----------+----------+
  |name |age|friend                     |friend_name|friend_age|
  +-----+---+---------------------------+-----------+----------+
  |Alice|27 |[name -> David, age -> 29] |David      |29        |
  |Bob  |33 |[name -> George, age -> 39]|George     |39        |
  |Cathy|29 |[name -> John, age -> 30]  |John       |30        |
  +-----+---+---------------------------+-----------+----------+
   */

  // getField for StructType
  (df7 withColumn ("c1", $"friend" getField "name")).show(false)
  /*
  +-----+---+------------+------+
  |name |age|friend      |c1    |
  +-----+---+------------+------+
  |Alice|27 |[David, 29] |David |
  |Bob  |33 |[George, 39]|George|
  |Cathy|29 |[John, 30]  |John  |
  +-----+---+------------+------+
   */

  (df7 withColumn ("friend_name", $"friend"("name")) withColumn ("friend_age", $"friend"("age"))).show(false)
  /*
  +-----+---+------------+-----------+----------+
  |name |age|friend      |friend_name|friend_age|
  +-----+---+------------+-----------+----------+
  |Alice|27 |[David, 29] |David      |29        |
  |Bob  |33 |[George, 39]|George     |39        |
  |Cathy|29 |[John, 30]  |John       |30        |
  +-----+---+------------+-----------+----------+
   */

}
