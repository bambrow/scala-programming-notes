import org.apache.spark.sql.{Column, Dataset, Row, SparkSession}
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.functions._

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





}
