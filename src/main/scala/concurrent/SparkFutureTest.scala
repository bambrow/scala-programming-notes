package concurrent

import java.util.concurrent.{ExecutorService, Executors, TimeUnit}

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{Dataset, SparkSession}

import scala.concurrent.{Await, ExecutionContext, Future}
// import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

/**
 * Test Future with SparkSession.
 */

object SparkFutureTest {

  def main(args: Array[String]): Unit = {
    implicit val ss: SparkSession = SparkSession.builder.master("local[5]").getOrCreate
    lazy val threadPool: ExecutorService = Executors.newFixedThreadPool(5)
    implicit val xc: ExecutionContext = new ExecutionContext {
      override def execute(runnable: Runnable): Unit = threadPool.submit(runnable)
      override def reportFailure(cause: Throwable): Unit = { }
    }

    Logger.getLogger("org").setLevel(Level.OFF)
    Logger.getLogger("akka").setLevel(Level.OFF)

    import ss.implicits._

    val ds1 = ss.range(0, 5).as[Long]
    val ds2 = ss.range(5, 10).as[Long]
    val ds3 = ss.range(0, 3).as[Long]
    val ds4 = ss.range(2, 5).as[Long]

    test1(ds1, ds2)
    test2(ds1, ds2)
    test3(ds3, ds4)
    test4(ds1)

    threadPool.shutdownNow()
  }

  def test1(ds1: Dataset[Long], ds2: Dataset[Long])(implicit ss: SparkSession, xc: ExecutionContext): Unit = {
    val f1 = Future {
      ds1.collect foreach (x => print(x + " "))
    }
    val f2 = Future {
      ds2.collect foreach (x => print(x + " "))
    }
    Await.result(Future.sequence(Seq(f1, f2)), Duration(10, TimeUnit.SECONDS))
    println
  }
  // 5 0 6 1 7 2 8 3 9 4

  def test2(ds1: Dataset[Long], ds2: Dataset[Long])(implicit ss: SparkSession, xc: ExecutionContext): Unit = {
    import ss.implicits._
    val f3 = Future {
      val ds1c = ds1.map(_.toString + "#").collect
      ds1c foreach (x => print(x + " "))
    }
    val f4 = Future {
      val ds2c = ds2.map(_.toString + "#").collect
      ds2c foreach (x => print(x + " "))
    }
    Await.result(Future.sequence(Seq(f3, f4)), Duration(10, TimeUnit.SECONDS))
    println
  }
  // 0# 5# 1# 6# 7# 8# 9# 2# 3# 4#

  def test3(ds3: Dataset[Long], ds4: Dataset[Long])(implicit ss: SparkSession, xc: ExecutionContext): Unit = {
    val f5 = Future {
      ds3.joinWith(ds4, ds3("id") === ds4("id"), "left").show
    }
    val f6 = Future {
      ds3.joinWith(ds4, ds3("id") === ds4("id"), "right").show
    }
    Await.result(Future.sequence(Seq(f5, f6)), Duration(10, TimeUnit.SECONDS))
  }

  def test4(ds1: Dataset[Long])(implicit ss: SparkSession, xc: ExecutionContext): Unit = {
    import ss.implicits._
    val f7a = Future {
      ds1.map(_.toString + "a").collect foreach (x => print(x + " "))
    }
    val f7b = Future {
      ds1.map(_.toString + "b").collect foreach (x => print(x + " "))
    }
    val f7c = Future {
      ds1.map(_.toString + "c").collect foreach (x => print(x + " "))
    }
    val f7d = Future {
      ds1.map(_.toString + "d").collect foreach (x => print(x + " "))
    }
    Await.result(Future.sequence(Seq(f7a, f7b, f7c, f7d)), Duration(10, TimeUnit.SECONDS))
    println
  }
  // 0a 0c 0b 1c 1a 2a 3a 4a 2c 3c 4c 1b 2b 3b 4b 0d 1d 2d 3d 4d

}
