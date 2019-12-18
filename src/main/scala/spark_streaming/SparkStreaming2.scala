package spark_streaming

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.ReceiverInputDStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
 * Test gracefully shutdown.
 */

object SparkStreaming2 {

  @throws[Exception]
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[5]").setAppName("SparkStreaming2").set("spark.streaming.stopGracefullyOnShutdown", "true")
    val sc: SparkContext = new SparkContext(conf)
    val ssc: StreamingContext = new StreamingContext(sc, Seconds(5))

    Logger.getLogger("org").setLevel(Level.OFF)
    Logger.getLogger("akka").setLevel(Level.OFF)

    try {
      val lines: ReceiverInputDStream[String] = ssc.socketTextStream("localhost", 9999)
      // nc -lk 9999
      lines foreachRDD {
        rdd: RDD[String] => {
          println("before = " + System.currentTimeMillis())
          Thread.sleep(5000)
          println("after = " + System.currentTimeMillis())
          val combined: String = rdd map (_.trim.split("\\s+")) map (_.mkString) reduce (_ + "#" + _)
          println(combined + "\n" + "-" * 20)
        }
      }

      println("start time = " + System.currentTimeMillis())
      ssc.start()
      ssc.awaitTerminationOrTimeout(1000L * 3)
    } catch {
      case e: Exception => println(e.getStackTrace)
    }
  }

}
