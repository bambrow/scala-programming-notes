package concurrent

import java.io.File
import java.util.concurrent.{BlockingQueue, ExecutorService, Executors, LinkedBlockingQueue}

import scala.concurrent.{ExecutionContext, Future}
import scala.io.{BufferedSource, Source}
import scala.util.{Failure, Success}

/**
 * Producer-consumer model, using numThread to control termination.
 */

object ProducerConsumerTest {

  lazy val threadPool: ExecutorService = Executors.newFixedThreadPool(10)
  implicit val xc: ExecutionContext = new ExecutionContext {
    override def execute(runnable: Runnable): Unit = threadPool.submit(runnable)
    override def reportFailure(cause: Throwable): Unit = { }
  }

  val queue: BlockingQueue[String] = new LinkedBlockingQueue[String]()

  def process(line: String): Unit = {
    println("length = " + line.trim.split("\\s+").length + " from line: [" + line.trim + "]")
  }

  def main(args: Array[String]): Unit = {
    val projectDir: String = new File(".").getCanonicalPath
    val path: String = projectDir + "/src/main/resources/producer_consumer.txt"
    val source: BufferedSource = Source.fromFile(path)
    var numThreads: Int = 0

    val f1 = numThreads synchronized {
      numThreads = numThreads + 1
      Future {
        source.getLines.foreach(queue.offer)
      }
    }

    f1 onComplete {
      case Success(_) => numThreads synchronized {
        println("finished reading file...")
        numThreads = numThreads - 1
      }
      case Failure(exception) => {
        exception.printStackTrace()
        numThreads synchronized {
          numThreads = numThreads - 1
        }
      }
    }

    Thread.sleep(100)

    while (!queue.isEmpty) {
      val f2 = numThreads synchronized {
        numThreads = numThreads + 1
        /* Future {
          val line: String = queue.poll
          // this will cause null pointer exception; need to move poll out of Future block
          process(line)
          // if (line != null) process(line)
          // or do this (not recommended)
        } */
        val line: String = queue.poll
        Future { process(line) }
      }
      f2 onComplete {
        case Success(_) => numThreads synchronized {
          // println("finished processing line...")
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

    while (numThreads > 0) {
      println("threads running, wait for 100ms...")
      Thread.sleep(100L * 1)
    }

    println("shutting down...")
    threadPool.shutdownNow()
  }

}
