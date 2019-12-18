package concurrent

/**
 * Basic thread test: extends Thread.
 */

class MyThread1 extends Thread {
  override def run(): Unit = {
    println("Thread " + Thread.currentThread.getName + " is running.")
  }
}

object ThreadTest1 {

  def main(args: Array[String]): Unit = {
    for (x <- 1 to 10) {
      val thread = new MyThread1
      thread.setName(x.toString)
      thread.start()
    }
  }

}
