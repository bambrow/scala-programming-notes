package spark_streaming

import java.util.concurrent.{ExecutorService, Executors}

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.ReceiverInputDStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

/**
 * Test gracefully shutdown with Future.
 */

object SparkStreaming4 {

  @throws[Exception]
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[10]").setAppName("SparkStreaming4").set("spark.streaming.stopGracefullyOnShutdown", "true")
    val sc: SparkContext = new SparkContext(conf)
    val ssc: StreamingContext = new StreamingContext(sc, Seconds(5))
    /*
    lazy val threadPool: ExecutorService = Executors.newFixedThreadPool(10)
    implicit val xc: ExecutionContext = new ExecutionContext {
      override def execute(runnable: Runnable): Unit = threadPool.submit(runnable)
      override def reportFailure(cause: Throwable): Unit = { }
    }
     */
    implicit val xc: ExecutionContext = ExecutionContext.Implicits.global

    Logger.getLogger("org").setLevel(Level.OFF)
    Logger.getLogger("akka").setLevel(Level.OFF)

    val duration: Long = 1000L * 20
    val startTime: Long = System.currentTimeMillis()
    val endTime: Long = startTime + duration

    var numThreads: Int = 0

    try {
      val lines: ReceiverInputDStream[String] = ssc.socketTextStream("localhost", 9999)
      // nc -lk 9999
      lines foreachRDD {
        rdd: RDD[String] => rdd.collect.foreach {
          line: String => {
            val f = numThreads synchronized {
              numThreads = numThreads + 1
              process(line)
            }
            f onComplete {
              case Success(_) => numThreads synchronized {
                numThreads = numThreads - 1
              }
              case Failure(exception) => {
                exception.printStackTrace()
                numThreads synchronized {
                  numThreads = numThreads - 1
                }
              }
            }
          }
        }
      }

      ssc.start()
      // ssc.awaitTerminationOrTimeout(duration)
      val termination = new Thread() {
        override def run(): Unit = {
          Thread.sleep(duration)
          ssc.stop(stopSparkContext = true, stopGracefully = true)
        }
      }
      termination.start()
      // termination.join()
    } catch {
      case e: Exception => println(e.getStackTrace)
    } finally {
      while (System.currentTimeMillis() < endTime || numThreads > 0) {
        // println("threads running, wait for 100ms...")
        Thread.sleep(100L * 1)
      }
      println("final numThreads = " + numThreads)
      // threadPool.shutdownNow()
    }
  }

  private def process(line: String)(implicit xc: ExecutionContext): Future[Unit] = Future {
    Thread.sleep(5000)
    println("length = " + line.trim.split("\\s+").length + " from line: [" + line.trim + "]")
  }

}
