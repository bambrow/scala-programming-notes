package concurrent

import java.util.concurrent.Executor

/**
 * Basic thread test: extends (implements) Runnable.
 */

class MyThread2 extends Runnable {
  override def run(): Unit = {
    println("Thread " + Thread.currentThread.getName + " is running.")
  }
}

object ThreadTest2 {

  def main(args: Array[String]): Unit = {

    val executor = new Executor {
      override def execute(command: Runnable): Unit = new Thread(command).start()
    }

    for (_ <- 1 to 10) {
      executor.execute(new MyThread2)
    }

    /*
    for (x <- 1 to 10) {
      val thread = new Thread(new MyThread2)
      thread.setName(x.toString)
      thread.start()
    }
     */
  }

}
