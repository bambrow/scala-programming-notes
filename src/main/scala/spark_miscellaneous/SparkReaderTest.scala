package spark_miscellaneous

import java.io.File

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import org.json4s._
import org.json4s.jackson.JsonMethods._

import scala.util.{Failure, Success, Try}

/**
 * This file tests reading text file with " and \".
 */

object SparkReaderTest extends App {

  implicit val ss: SparkSession = SparkSession.builder.master("local[5]").getOrCreate

  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)

  import ss.implicits._

  val projectDir: String = new File(".").getCanonicalPath
  println(projectDir)

  val df0: DataFrame = ss.read.text("file:///" + projectDir + "/src/main/resources/spark_sample_read.txt")
  df0.show(truncate = false)
  /*
  +---------------------------------------------------------+
  |value                                                    |
  +---------------------------------------------------------+
  |{"key":"content"}                                        |
  |"{\"key\":\"content\"}"                                  |
  |{"key1":"content1","key2":"content2"}                    |
  |"{\"key1\":\"content1\",\"key2\":\"content2\"}"          |
  |{"key":"content"}|something                              |
  |"{\"key\":\"content\"}"|something                        |
  |{"key1":"content1","key2":"content2"}|something          |
  |"{\"key1\":\"content1\",\"key2\":\"content2\"}"|something|
  |{"key1":"content1"}|{"key2":"content2"}                  |
  |"{\"key1\":\"content1\"}"|{"key2":"content2"}            |
  |{"key1":"content1"}|"{\"key2\":\"content2\"}"            |
  |"{\"key1\":\"content1\"}"|"{\"key2\":\"content2\"}"      |
  +---------------------------------------------------------+
   */

  val df1: DataFrame = df0.filter(!$"value".contains("|"))
  val df2: DataFrame = {
    df0.filter($"value".contains("|")).withColumn(
      "_tmp", split($"value", "\\|")
    ).select(
      $"_tmp" getItem 0 as "first",
      $"_tmp" getItem 1 as "second"
    ).drop("_tmp")
  }

  df1.show(truncate = false)
  /*
  +-----------------------------------------------+
  |value                                          |
  +-----------------------------------------------+
  |{"key":"content"}                              |
  |"{\"key\":\"content\"}"                        |
  |{"key1":"content1","key2":"content2"}          |
  |"{\"key1\":\"content1\",\"key2\":\"content2\"}"|
  +-----------------------------------------------+
   */

  df2.show(truncate = false)
  /*
  +-----------------------------------------------+-------------------------+
  |first                                          |second                   |
  +-----------------------------------------------+-------------------------+
  |{"key":"content"}                              |something                |
  |"{\"key\":\"content\"}"                        |something                |
  |{"key1":"content1","key2":"content2"}          |something                |
  |"{\"key1\":\"content1\",\"key2\":\"content2\"}"|something                |
  |{"key1":"content1"}                            |{"key2":"content2"}      |
  |"{\"key1\":\"content1\"}"                      |{"key2":"content2"}      |
  |{"key1":"content1"}                            |"{\"key2\":\"content2\"}"|
  |"{\"key1\":\"content1\"}"                      |"{\"key2\":\"content2\"}"|
  +-----------------------------------------------+-------------------------+
   */

  val df3: Dataset[String] = df1.as[String] map {
    parse(_).toString
  }
  df3.show(truncate = false)
  /*
  +-----------------------------------------------------------------+
  |value                                                            |
  +-----------------------------------------------------------------+
  |JObject(List((key,JString(content))))                            |
  |JString({"key":"content"})                                       |
  |JObject(List((key1,JString(content1)), (key2,JString(content2))))|
  |JString({"key1":"content1","key2":"content2"})                   |
  +-----------------------------------------------------------------+
   */

  val df4: Dataset[String] = df1.as[String] map {
    parse(_) match {
      case JString(x) => x
      case y: JValue => compact(render(y))
    }
  }
  df4.show(truncate = false)
  /*
  +-------------------------------------+
  |value                                |
  +-------------------------------------+
  |{"key":"content"}                    |
  |{"key":"content"}                    |
  |{"key1":"content1","key2":"content2"}|
  |{"key1":"content1","key2":"content2"}|
  +-------------------------------------+
   */

  val df5: Dataset[(String, String)] = df2.as[(String, String)] map {
    case (x, y) => {
      Try(parse(y)) match {
        case Success(_) => (parse(x).toString, parse(y).toString)
        case Failure(_) => (parse(x).toString, y)
      }
    }
  }
  df5.show(truncate = false)
  /*
  +-----------------------------------------------------------------+---------------------------------------+
  |_1                                                               |_2                                     |
  +-----------------------------------------------------------------+---------------------------------------+
  |JObject(List((key,JString(content))))                            |something                              |
  |JString({"key":"content"})                                       |something                              |
  |JObject(List((key1,JString(content1)), (key2,JString(content2))))|something                              |
  |JString({"key1":"content1","key2":"content2"})                   |something                              |
  |JObject(List((key1,JString(content1))))                          |JObject(List((key2,JString(content2))))|
  |JString({"key1":"content1"})                                     |JObject(List((key2,JString(content2))))|
  |JObject(List((key1,JString(content1))))                          |JString({"key2":"content2"})           |
  |JString({"key1":"content1"})                                     |JString({"key2":"content2"})           |
  +-----------------------------------------------------------------+---------------------------------------+
   */

  val df6: Dataset[(String, String)] = df2.as[(String, String)] map {
    case (x, y) => {
      val x0: String = parse(x) match {
        case JString(x1) => x1
        case x2: JValue => compact(render(x2))
      }
      val y0: String = Try(parse(y)) match {
        case Success(_) => parse(y) match {
          case JString(y1) => y1
          case y2: JValue => compact(render(y2))
        }
        case Failure(_) => y
      }
      (x0, y0)
    }
  }
  df6.show(truncate = false)
  /*
  +-------------------------------------+-------------------+
  |_1                                   |_2                 |
  +-------------------------------------+-------------------+
  |{"key":"content"}                    |something          |
  |{"key":"content"}                    |something          |
  |{"key1":"content1","key2":"content2"}|something          |
  |{"key1":"content1","key2":"content2"}|something          |
  |{"key1":"content1"}                  |{"key2":"content2"}|
  |{"key1":"content1"}                  |{"key2":"content2"}|
  |{"key1":"content1"}                  |{"key2":"content2"}|
  |{"key1":"content1"}                  |{"key2":"content2"}|
  +-------------------------------------+-------------------+
   */

}
