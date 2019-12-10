package spark

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{Dataset, Row, SparkSession}
import org.apache.spark.storage.StorageLevel

/**
 * This file tests the following methods:
 *   columns, dtypes, explain, isEmpty, isLocal, persist,
 *   printSchema, rdd, storageLevel, toDF, unpersist,
 *   coalesce, union, distinct, except, exceptAll, filter
 */

object Dataset2 extends App {

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

  df1.columns foreach {
    x => print(x + ", ")
  }
  // name, age,

  println

  df1.dtypes foreach {
    x => print(x + ", ")
  }
  // (name,StringType), (age,IntegerType),

  println

  df1.explain
  /*
  == Physical Plan ==
  LocalTableScan [name#5, age#6]
   */

  println

  println(df1.isEmpty) // false

  println

  println(df1.isLocal) // false

  println

  df1 persist StorageLevel.MEMORY_AND_DISK
  df1 persist StorageLevel.MEMORY_AND_DISK

  df1.printSchema
  /*
  root
   |-- name: string (nullable = true)
   |-- age: integer (nullable = false)
   */

  println

  val rdd1 = df1.rdd

  println(df1.schema) // StructType(StructField(name,StringType,true), StructField(age,IntegerType,false))

  println

  println(df1.storageLevel) // StorageLevel(disk, memory, deserialized, 1 replicas)

  println

  (df1 toDF ("name2", "age2")).dtypes foreach {
    x => print(x + ", ")
  }
  // (name2,StringType), (age2,IntegerType),

  println

  df1.unpersist
  df2.unpersist

  val df3 = df1 repartition 5 coalesce 3
  df3.as[PersonWithAge] foreachPartition {
    x: Iterator[PersonWithAge] => println(x.toList)
  }
  /*
  List(PersonWithAge(David,27))
  List(PersonWithAge(Fred,29), PersonWithAge(Cathy,20), PersonWithAge(Bob,23))
  List(PersonWithAge(Alice,18), PersonWithAge(Eva,22))
   */

  println

  val df4 = df1 union df1
  println(df4.count) // 12
  println(df4.distinct.count) // 6

  val df5 = df4.dropDuplicates
  println(df5.count) // 6

  println

  val df6 = df4 except df1
  df6.show
  /*
  +----+---+
  |name|age|
  +----+---+
  +----+---+
   */
  val df7 = df4 exceptAll df1
  df7.show
  /*
  +-----+---+
  | name|age|
  +-----+---+
  | Fred| 29|
  |David| 27|
  |  Bob| 23|
  |  Eva| 22|
  |Cathy| 20|
  |Alice| 18|
  +-----+---+
   */

  println

  val ds1 = df1.as[PersonWithAge]
  (ds1 filter (_.age > 25)).show
  /*
  +-----+---+
  | name|age|
  +-----+---+
  |David| 27|
  | Fred| 29|
  +-----+---+
   */

  println

}
