package spark_miscellaneous

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{Dataset, SparkSession}

import scala.util.Random

/**
 * This file tests reusing SparkSession inside loops.
 */

object SparkForeachTest extends App {

  implicit val ss: SparkSession = SparkSession.builder.master("local[5]").getOrCreate

  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)

  private val r: Random = new Random
  private val generator: RandomDatasetGenerator = new RandomDatasetGenerator

  /*
  private val arr: Array[Int] = {
    for (_ <- 0 until 5) yield r.nextInt
  }.toArray

  arr foreach {
    i => {
      val ds: Dataset[Int] = generator.getIntDS(5)
      ds.show
      println(s"i = $i")
      ds.filter(_ > i).show
    }
  }
   */

  /*
  private val str: String = r.nextString(20)
  private val charArr: Array[Char] = str.toCharArray
  var x: Int = 0

  while (x < 5) {
    val ds: Dataset[String] = generator.getStringDS(n = 5, k = 20)
    val filtered: Dataset[String] = ds.filter(_.toCharArray.intersect(charArr).length > 0)
    if (filtered.count > 0) {
      filtered.collect foreach println
      x = x + 1
      println
    }
  }
   */

  val strDS: Dataset[String] = generator.getStringDS(n = 5, k = 10)

  // strDS foreach will give java.lang.NullPointerException
  strDS.collect foreach {
    s => {
      val startTime: Long = System.currentTimeMillis()
      var foundMatch: Boolean = false
      var cnt: Int = 0
      val cArr: Array[Char] = s.toCharArray
      while (!foundMatch) {
        cnt = cnt + 1
        val ds: Dataset[String] = generator.getStringDS(n = 5, k = 10)
        val filtered: Dataset[String] = ds.filter(_.toCharArray.intersect(cArr).length > 0)
        if (filtered.count > 0) {
          filtered.collect foreach println
          foundMatch = true
          println(s"count = $cnt")
        }
      }
      val endTime: Long = System.currentTimeMillis()
      println(s"time = ${endTime - startTime}")
      println(s"thread = ${Thread.currentThread.getName}")
      println
    }
  }

}
