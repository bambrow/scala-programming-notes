package concurrent

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Random, Success}

/**
 * Test for-comprehension.
 */

object FutureLoopTest {

  def main(args: Array[String]): Unit = {
    val startTime: Long = System.currentTimeMillis()
    val endTime: Long = startTime + 1000L * 60

    println("countdown starts...")
    while (System.currentTimeMillis() < endTime) {
      val line: String = scala.io.StdIn.readLine()
      val sumLineFuture: Future[Int] = sumLine(line)
      val prodLineFuture: Future[Long] = prodLine(line)
      val concatLineFuture: Future[String] = concatLine(line)
      val ff: Future[Unit] = for {
        f1 <- sumLineFuture
        f2 <- prodLineFuture
        f3 <- concatLineFuture
      } yield {
        println(f1)
        println(f2)
        println(f3)
      }
      ff onComplete {
        case Success(_) =>
        case Failure(exception) => println(exception.getMessage)
      }
    }
    println("countdown ends...")

    Thread.sleep(2000)
    println("exiting main...")
  }

  def sumLine(line: String): Future[Int] = Future {
    Thread.sleep(Random.nextInt(5000))
    line.trim.split("\\s+").map(_.toInt).sum
  }

  def prodLine(line: String): Future[Long] = Future {
    Thread.sleep(Random.nextInt(5000))
    line.trim.split("\\s+").map(_.toInt).product
  }

  def concatLine(line: String): Future[String] = Future {
    Thread.sleep(Random.nextInt(5000))
    line.trim.split("\\s+").mkString
  }

}
