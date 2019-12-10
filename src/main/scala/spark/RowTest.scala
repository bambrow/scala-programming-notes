package spark

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.types._
import org.apache.spark.sql.{Dataset, Row, SparkSession}
import collection.JavaConverters._

/**
 * This file tests the following Row methods:
 *   length, size, anyNull, mkString,
 *   fieldIndex, getAs, getInt, getString,
 *   getMap, getList, getSeq, getStruct
 * And the following methods are not tested:
 *   getBoolean, getByte, getDate, getDecimal, getDouble,
 *   getFloat, getJavaMap, getLong, getShort, getTimestamp,
 *   getValuesMap, isNullAt
 */

object RowTest extends App {

  implicit val ss: SparkSession = SparkSession.builder.master("local").getOrCreate

  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)

  import ss.implicits._

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

  val df3 = ss createDataFrame (ss.sparkContext parallelize data3, schema3)
  val df4 = ss createDataFrame (ss.sparkContext parallelize data4, schema4)
  val df5 = ss createDataFrame (ss.sparkContext parallelize data5, schema5)

  df3.show(false)
  /*
  +-----+---+------------+
  |name |age|friend      |
  +-----+---+------------+
  |Alice|27 |[David, 29] |
  |Bob  |33 |[George, 39]|
  |Cathy|29 |[John, 30]  |
  +-----+---+------------+
   */

  val df3a: Dataset[Row] = df3 map {
    x: Row => {
      val len: Int = x.length // x.size
      val anyNull: Boolean = x.anyNull
      val str: String = x.mkString(",")
      (len, anyNull, str)
    }
  } toDF ("length", "anyNull", "mkString")
  df3a.show(false)
  /*
  +------+-------+-------------------+
  |length|anyNull|mkString           |
  +------+-------+-------------------+
  |3     |false  |Alice,27,[David,29]|
  |3     |false  |Bob,33,[George,39] |
  |3     |false  |Cathy,29,[John,30] |
  +------+-------+-------------------+
   */

  df3 foreach {
    x: Row => println(x.toSeq)
  }
  /*
  WrappedArray(Alice, 27, [David,29])
  WrappedArray(Bob, 33, [George,39])
  WrappedArray(Cathy, 29, [John,30])
   */

  val df3b: Dataset[Row] = df3 map {
    x: Row => (x.getStruct(2).getString(0), x.getStruct(2).getInt(1))
  } toDF ("name", "age")
  df3b.show
  /*
  +------+---+
  |  name|age|
  +------+---+
  | David| 29|
  |George| 39|
  |  John| 30|
  +------+---+
   */

  val df3c: Dataset[Row] = df3 map {
    x: Row => {
      val name: String = x.getStruct(2).getAs[String](0)
      val age: Int = x.getStruct(2).getAs[Int](1)
      (name, age)
    }
  } toDF ("name", "age")
  df3c.show
  /*
  +------+---+
  |  name|age|
  +------+---+
  | David| 29|
  |George| 39|
  |  John| 30|
  +------+---+
   */

  val df3d: Dataset[Row] = df3 map {
    x: Row => (x.getStruct(2).getAs[String](0), x.getStruct(2).getAs[Int](1))
  } toDF ("name", "age")
  df3d.show
  /*
  +------+---+
  |  name|age|
  +------+---+
  | David| 29|
  |George| 39|
  |  John| 30|
  +------+---+
   */

  val df3e: Dataset[Row] = df3 map {
    x: Row => {
      (
        x.fieldIndex("name").toString + "-" + x.getString(x.fieldIndex("name")),
        x.fieldIndex("age").toString + "-" + x.getInt(x.fieldIndex("age")).toString,
        x.fieldIndex("friend").toString + "-" + x.getStruct(x.fieldIndex("friend")).toString
      )
    }
  } toDF ("index_name", "index_age", "index_friend")
  df3e.show(false)
  /*
  +----------+---------+-------------+
  |index_name|index_age|index_friend |
  +----------+---------+-------------+
  |0-Alice   |1-27     |2-[David,29] |
  |0-Bob     |1-33     |2-[George,39]|
  |0-Cathy   |1-29     |2-[John,30]  |
  +----------+---------+-------------+
   */

  df4.show(false)
  /*
  +-----+---+--------------------------------------+
  |name |age|friends                               |
  +-----+---+--------------------------------------+
  |Alice|27 |[[David, 29], [Eva, 19], [Fred, 22]]  |
  |Bob  |33 |[[George, 39], [Helen, 22]]           |
  |Cathy|29 |[[John, 30], [Katie, 39], [Linda, 20]]|
  +-----+---+--------------------------------------+
   */

  val df4a: Dataset[Seq[PersonWithAge]] = (df4 map {
    x: Row => {
      val seq: Seq[Row] = x.getSeq[Row](2)
      seq map {
        case Row(k: String, v: Int) => PersonWithAge(k, v)
      }
    }
  }).as[Seq[PersonWithAge]]
  df4a.show(false)
  /*
  +--------------------------------------+
  |value                                 |
  +--------------------------------------+
  |[[David, 29], [Eva, 19], [Fred, 22]]  |
  |[[George, 39], [Helen, 22]]           |
  |[[John, 30], [Katie, 39], [Linda, 20]]|
  +--------------------------------------+
   */

  val df4b: Dataset[Seq[String]] = (df4 map {
    x: Row => {
      val list: java.util.List[Row] = x.getList[Row](2)
      (list.asScala map {
        y: Row => y.getAs[String](0)
      }).toList
    }
  }).as[Seq[String]]
  df4b.show(false)
  /*
  +--------------------+
  |value               |
  +--------------------+
  |[David, Eva, Fred]  |
  |[George, Helen]     |
  |[John, Katie, Linda]|
  +--------------------+
   */

  df5.show(false)
  /*
  +-----+---+---------------------------+
  |name |age|friend                     |
  +-----+---+---------------------------+
  |Alice|27 |[name -> David, age -> 29] |
  |Bob  |33 |[name -> George, age -> 39]|
  |Cathy|29 |[name -> John, age -> 30]  |
  +-----+---+---------------------------+
   */

  val df5a: Dataset[Row] = df5 map {
    x: Row => {
      val map: scala.collection.Map[String, String] = x.getMap[String, String](2)
      (map.get("name"), map.get("age"))
    }
  } toDF ("name", "age")
  df5a.show
  /*
  +------+---+
  |  name|age|
  +------+---+
  | David| 29|
  |George| 39|
  |  John| 30|
  +------+---+
   */

}
