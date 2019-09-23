import org.apache.spark.sql.{Dataset, Row, SparkSession}
import org.apache.log4j.{Level, Logger}

object Dataset1 extends App {

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

  df1.collect foreach println
  /*
  [Alice,18]
  [Bob,23]
  [Cathy,20]
  [David,27]
  [Eva,22]
  [Fred,29]
   */

  println

  println(df1.count) // 6

  println

  (df1 describe ("name", "age")).collect foreach println
  /*
  [count,6,6]
  [mean,null,23.166666666666668]
  [stddev,null,4.167333280008532]
  [min,Alice,18]
  [max,Fred,29]
   */

  println

  println(df1.first) // [Alice,18]
  println(df1.head) // [Alice,18]

  println

  df1 foreach (println(_))

  println

  df1.as[PersonWithAge] repartition 5 foreachPartition {
    x: Iterator[PersonWithAge] => println(x.toList)
  }
  /*
  List(PersonWithAge(David,27))
  List(PersonWithAge(Fred,29), PersonWithAge(Cathy,20))
  List(PersonWithAge(Bob,23))
  List(PersonWithAge(Alice,18))
  List(PersonWithAge(Eva,22))
   */

  println

  df1 head 3 foreach println
  /*
  [Alice,18]
  [Bob,23]
  [Cathy,20]
   */

  println

  println(df1.as[PersonWithAge] map (_.age) reduce (_ + _)) // 139

  println

  df1.show
  /*
  +-----+---+
  | name|age|
  +-----+---+
  |Alice| 18|
  |  Bob| 23|
  |Cathy| 20|
  |David| 27|
  |  Eva| 22|
  | Fred| 29|
  +-----+---+
   */

  println

  df1 take 3 foreach println
  /*
  [Alice,18]
  [Bob,23]
  [Cathy,20]
   */

  println

}
