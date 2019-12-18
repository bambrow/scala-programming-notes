package concurrent

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.Success

/**
 * Test variable read/write without synchronized.
 */

object SynchronizedTest1 {

  def main(args: Array[String]): Unit = {
    var count: Int = 0
    @transient implicit val xc: ExecutionContext = ExecutionContext.global
    val strFuture: Future[String] = Future {
      val str: String = "a" * 4 + "b" * 3 + "c" * 2 + "d" * 1
      println(str)
      str
    }
    println(s"count before = $count")
    Await.result(strFuture, Duration.Inf)
    strFuture onComplete {
      case Success(str) => {
        println("counting a...")
        val cnt = str.count(_ == 'a')
        count += cnt
        println(s"a count: $cnt")
      }
    }
    println(s"count after a = $count")
    strFuture onComplete {
      case Success(str) => {
        println("counting b...")
        val cnt = str.count(_ == 'b')
        count += cnt
        println(s"b count: $cnt")
      }
    }
    println(s"count after b = $count")
    strFuture onComplete {
      case Success(str) => {
        println("counting c...")
        val cnt = str.count(_ == 'c')
        count += cnt
        println(s"c count: $cnt")
      }
    }
    println(s"count after c = $count")
    strFuture onComplete {
      case Success(str) => {
        println("counting d...")
        val cnt = str.count(_ == 'd')
        count += cnt
        println(s"d count: $cnt")
      }
    }
    println(s"count after d = $count")
    Thread.sleep(2000)
    println(s"final count = $count")
  }

  /*
  count before = 0
  aaaabbbccd
  count after a = 0
  counting a...
  count after b = 0
  counting b...
  a count: 4
  count after c = 4
  counting c...
  b count: 3
  c count: 2
  count after d = 9
  counting d...
  d count: 1
  final count = 10
   */

}
