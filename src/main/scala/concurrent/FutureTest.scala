package concurrent

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

/**
 * Basic Future test.
 */

object FutureTest {

  def sampleFunc(x: Int, time: Long): Unit = {
    println(s"starting sampleFunc$x...")
    Thread.sleep(time)
    println(s"completing sampleFunc$x...")
  }

  def sampleFuncError(x: Int, time: Long): Unit = {
    println(s"starting sampleFuncError$x...")
    Thread.sleep(time)
    println(s"completing sampleFuncError$x...")
    throw new Exception(s"sampleFuncError$x throws exception!")
  }

  def main(args: Array[String]): Unit = {
    println("starting main...")
    try {
      val f1: Future[Unit] = Future {
        sampleFunc(1, 5000)
      }
      println("f1 created...")
      val f2: Future[Unit] = Future {
        sampleFunc(2, 4000)
      }
      println("f2 created...")
      val f3: Future[Unit] = Future {
        sampleFuncError(3, 3000)
      }
      println("f3 created...")
      val f4: Future[Unit] = Future {
        sampleFuncError(4, 2000)
      } recover {
        case e: Exception => println(e.getMessage)
      }
      println("f4 created...")
      Await.result(Future.sequence(Seq(f1, f2, f3, f4)), Duration.Inf)
      f1 onComplete {
        case Success(_) => println("f1 completed successfully...")
        case Failure(exception) => println(s"f1 completed with exception ${exception.getMessage}...")
      }
      f2 onComplete {
        case Success(_) => println("f2 completed successfully...")
        case Failure(exception) => println(s"f2 completed with exception ${exception.getMessage}...")
      }
      f3 onComplete {
        case Success(_) => println("f3 completed successfully...")
        case Failure(exception) => println(s"f3 completed with exception ${exception.getMessage}...")
      }
      f4 onComplete {
        case Success(_) => println("f4 completed successfully...")
        case Failure(exception) => println(s"f4 completed with exception ${exception.getMessage}...")
      }
    } catch {
      case e: Exception => println(e.getMessage)
    }
    println("completing main...")
  }

  /*
  starting main...
  f1 created...
  f2 created...
  starting sampleFunc2...
  starting sampleFunc1...
  f3 created...
  starting sampleFuncError3...
  starting sampleFuncError4...
  f4 created...
  completing sampleFuncError4...
  sampleFuncError4 throws exception!
  f4 completed successfully...
  completing sampleFuncError3...
  f3 completed with exception sampleFuncError3 throws exception!...
  completing sampleFunc2...
  f2 completed successfully...
  completing sampleFunc1...
  f1 completed successfully...
  sampleFuncError3 throws exception!
  completing main...
   */

}
