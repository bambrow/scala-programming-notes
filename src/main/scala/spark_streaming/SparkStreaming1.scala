package spark_streaming

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
 * Basic spark streaming test.
 */

object SparkStreaming1 {

  @throws[Exception]
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[5]").setAppName("SparkStreaming1").set("spark.streaming.stopGracefullyOnShutdown", "true")
    val sc: SparkContext = new SparkContext(conf)
    val ssc: StreamingContext = new StreamingContext(sc, Seconds(5))

    Logger.getLogger("org").setLevel(Level.OFF)
    Logger.getLogger("akka").setLevel(Level.OFF)

    try {
      val lines: ReceiverInputDStream[String] = ssc.socketTextStream("localhost", 9999)
      // nc -lk 9999
      val words: DStream[String] = lines.flatMap(_.trim.split("\\s+"))
      val pairs: DStream[(String, Int)] = words.map(w => (w, 1))
      val wordCounts: DStream[(String, Int)] = pairs.reduceByKey(_ + _)
      wordCounts.print()

      println("start time = " + System.currentTimeMillis())
      ssc.start()
      ssc.awaitTerminationOrTimeout(1000L * 30)
    } catch {
      case e: Exception => println(e.getStackTrace)
    }
  }

}
