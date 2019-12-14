package concurrent

import java.util.concurrent.{ExecutorService, Executors}

import org.apache.log4j.{Level, Logger}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}

import scala.concurrent.{ExecutionContext, Future}

object SparkStreamingFutureTest2 {

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
      lines foreachRDD {
        rdd: RDD[String] => {
          val combined: String = rdd map (_.trim.split("\\s+")) map (_.mkString) reduce (_ + "#" + _)
          println(combined + "\n" + "-" * 20)
        }
      }

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
