package spark

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{Dataset, Row, SparkSession}

/**
 * This file shows how to handle null values in
 *   DataFrame when converting them to case class
 *   in Dataset using Option.
 */

object Dataset4 extends App {

  implicit val ss: SparkSession = SparkSession.builder.master("local").getOrCreate

  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)

  import ss.implicits._

  val df1: Dataset[Row] = Seq[(String, java.lang.Integer)](
    ("Alice", null),
    ("Bob", 23),
    ("Cathy", null),
    ("David", 27),
    ("Eva", null),
    ("Fred", 29)
  ) toDF ("name", "age")

  val df2: Dataset[Row] = Seq[(String, java.lang.Integer)](
    ("Alice", 3000),
    ("Cathy", null),
    ("David", 10000),
    ("Fred", null),
    ("George", null)
  ) toDF ("name", "salary")

  df1.show
  /*
  +-----+----+
  | name| age|
  +-----+----+
  |Alice|null|
  |  Bob|  23|
  |Cathy|null|
  |David|  27|
  |  Eva|null|
  | Fred|  29|
  +-----+----+
   */

  df1.printSchema
  /*
  root
   |-- name: string (nullable = true)
   |-- age: integer (nullable = true)
   */

  val ds1 = df1.as[PersonWithAge]
  ds1.show
  /*
  +-----+----+
  | name| age|
  +-----+----+
  |Alice|null|
  |  Bob|  23|
  |Cathy|null|
  |David|  27|
  |  Eva|null|
  | Fred|  29|
  +-----+----+
   */

  // note that as[T] does not affect the schema
  // even scala.Int cannot be null
  ds1.printSchema
  /*
  root
   |-- name: string (nullable = true)
   |-- age: integer (nullable = true)
   */

  val ds2 = df1.as[PersonWithAgeOption]
  ds2.show
  /*
  +-----+----+
  | name| age|
  +-----+----+
  |Alice|null|
  |  Bob|  23|
  |Cathy|null|
  |David|  27|
  |  Eva|null|
  | Fred|  29|
  +-----+----+
   */

  ds2.printSchema
  /*
  root
   |-- name: string (nullable = true)
   |-- age: integer (nullable = true)
   */

  // error
  // Caused by: java.lang.NullPointerException: Null value appeared in non-nullable field:
  // - field (class: "scala.Int", name: "age")
  // - root class: "PersonWithAge"
  // If the schema is inferred from a Scala tuple/case class, or a Java bean,
  //   please try to use scala.Option[_] or other nullable types (e.g. java.lang.Integer instead of int/scala.Int).
  // ds1.filter(_.age > 25).show

  // same error
  // also warning message:
  //   Comparing unrelated types: Int and Null
  // ds1.filter(_.age != null).filter(_.age > 25).show

  // this is because when using statically typed Dataset API
  // Spark has to deserialize the object
  // and scala.Int cannot be null

  // query Dataset with SQL or DataFrame API will not deserialize the object
  // therefore the following code works
  ds1.filter($"age" > 25).show
  /*
  +-----+---+
  | name|age|
  +-----+---+
  |David| 27|
  | Fred| 29|
  +-----+---+
   */

  // using Option types requires a modification of filter
  ds2.filter(_.age.exists(_ > 25)).show
  /*
  +-----+---+
  | name|age|
  +-----+---+
  |David| 27|
  | Fred| 29|
  +-----+---+
   */

  // or use pattern matching
  (ds2 filter {
    _.age match {
      case Some(age) => age > 25
      case _ => false
    }
  }).show
  /*
  +-----+---+
  | name|age|
  +-----+---+
  |David| 27|
  | Fred| 29|
  +-----+---+
   */

  // error
  // ds1.map( x => x.copy(age = x.age + 1) ).show

  ds2.map( x => x.copy(age = x.age.map(_ + 1)) ).show
  /*
  +-----+----+
  | name| age|
  +-----+----+
  |Alice|null|
  |  Bob|  24|
  |Cathy|null|
  |David|  28|
  |  Eva|null|
  | Fred|  30|
  +-----+----+
   */

  ds2.map(_.age.getOrElse(-1)).show
  /*
  +-----+
  |value|
  +-----+
  |   -1|
  |   23|
  |   -1|
  |   27|
  |   -1|
  |   29|
  +-----+
   */

}
