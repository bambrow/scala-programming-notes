package concurrent

import java.util.concurrent.{ExecutorService, Executors}

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.concurrent.{ExecutionContext, Future}

object SparkStreamingFutureTest1 {

  @throws[Exception]
  def main(args: Array[String]): Unit = {
    val ss: SparkSession = SparkSession.builder.master("local[5]").getOrCreate
    val ssc: StreamingContext = new StreamingContext(ss.sparkContext, Seconds(10))
    lazy val threadPool: ExecutorService = Executors.newFixedThreadPool(5)
    implicit val xc: ExecutionContext = new ExecutionContext {
      override def execute(runnable: Runnable): Unit = threadPool.submit(runnable)
      override def reportFailure(cause: Throwable): Unit = { }
    }

    Logger.getLogger("org").setLevel(Level.OFF)
    Logger.getLogger("akka").setLevel(Level.OFF)

    val startTime: Long = System.currentTimeMillis()
    val endTime: Long = startTime + 1000L * 30

    try {
      val lines: ReceiverInputDStream[String] = ssc.socketTextStream("localhost", 9999)
      // nc -lk 9999
      val words: DStream[String] = lines.flatMap(_.trim.split("\\s+"))
      val pairs: DStream[(String, Int)] = words.map(w => (w, 1))
      val wordCounts: DStream[(String, Int)] = pairs.reduceByKey(_ + _)
      wordCounts.print()

      ssc.start()
      Future { ssc.awaitTermination() }

      while (System.currentTimeMillis() < endTime) { Thread.sleep(1000) }

      Future { ssc.stop(stopSparkContext = true, stopGracefully = true) }
      threadPool.shutdownNow()
    } catch {
      case e: Exception => println(e.getStackTrace)
    }
  }

}
