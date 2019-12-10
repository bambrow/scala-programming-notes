package spark

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.expressions.{Window, WindowSpec}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{Dataset, Row, SparkSession}

/**
 * This file tests the following Window functions:
 *   rowsBetween, rangeBetween, currentRow, unboundedPreceding, unboundedFollowing
 */


object WindowFunction2 extends App {

  implicit val ss: SparkSession = SparkSession.builder.master("local").getOrCreate

  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)

  import ss.implicits._

  val df1: Dataset[Row] = Seq(
    ("Amy", 65, 101),
    ("Amy", 65, 102),
    ("Amy", 72, 101),
    ("Amy", 82, 103),
    ("Amy", 85, 101),
    ("Amy", 85, 102),
    ("Amy", 90, 103),
    ("Amy", 95, 101),
    ("Bob", 85, 101),
    ("Bob", 85, 103),
    ("Bob", 85, 102),
    ("Bob", 95, 102),
    ("Bob", 95, 101),
    ("Bob", 97, 102),
    ("Bob", 99, 103)
  ) toDF ("name", "grade", "course")

  // calculate the running average grade per course
  val byCourse1: WindowSpec = Window partitionBy 'course orderBy 'grade rowsBetween (Window.unboundedPreceding, Window.currentRow)
  df1.withColumn("avg_up_to_now", avg('grade) over byCourse1).show
  /*
  +----+-----+------+-----------------+
  |name|grade|course|    avg_up_to_now|
  +----+-----+------+-----------------+
  | Amy|   65|   101|             65.0|
  | Amy|   72|   101|             68.5|
  | Amy|   85|   101|             74.0|
  | Bob|   85|   101|            76.75|
  | Amy|   95|   101|             80.4|
  | Bob|   95|   101|82.83333333333333|
  | Amy|   82|   103|             82.0|
  | Bob|   85|   103|             83.5|
  | Amy|   90|   103|85.66666666666667|
  | Bob|   99|   103|             89.0|
  | Amy|   65|   102|             65.0|
  | Amy|   85|   102|             75.0|
  | Bob|   85|   102|78.33333333333333|
  | Bob|   95|   102|             82.5|
  | Bob|   97|   102|             85.4|
  +----+-----+------+-----------------+
   */

  // compare the previous result with the following 2 examples
  val byCourseTest1: WindowSpec = Window partitionBy 'course orderBy 'grade
  df1.withColumn("avg_up_to_now", avg('grade) over byCourseTest1).show
  /*
  +----+-----+------+-----------------+
  |name|grade|course|    avg_up_to_now|
  +----+-----+------+-----------------+
  | Amy|   65|   101|             65.0|
  | Amy|   72|   101|             68.5|
  | Amy|   85|   101|            76.75|
  | Bob|   85|   101|            76.75|
  | Amy|   95|   101|82.83333333333333|
  | Bob|   95|   101|82.83333333333333|
  | Amy|   82|   103|             82.0|
  | Bob|   85|   103|             83.5|
  | Amy|   90|   103|85.66666666666667|
  | Bob|   99|   103|             89.0|
  | Amy|   65|   102|             65.0|
  | Amy|   85|   102|78.33333333333333|
  | Bob|   85|   102|78.33333333333333|
  | Bob|   95|   102|             82.5|
  | Bob|   97|   102|             85.4|
  +----+-----+------+-----------------+
   */

  val byCourseTest2: WindowSpec = Window partitionBy 'course orderBy ('grade, 'name)
  df1.withColumn("avg_up_to_now", avg('grade) over byCourseTest2).show
  /*
  +----+-----+------+-----------------+
  |name|grade|course|    avg_up_to_now|
  +----+-----+------+-----------------+
  | Amy|   65|   101|             65.0|
  | Amy|   72|   101|             68.5|
  | Amy|   85|   101|             74.0|
  | Bob|   85|   101|            76.75|
  | Amy|   95|   101|             80.4|
  | Bob|   95|   101|82.83333333333333|
  | Amy|   82|   103|             82.0|
  | Bob|   85|   103|             83.5|
  | Amy|   90|   103|85.66666666666667|
  | Bob|   99|   103|             89.0|
  | Amy|   65|   102|             65.0|
  | Amy|   85|   102|             75.0|
  | Bob|   85|   102|78.33333333333333|
  | Bob|   95|   102|             82.5|
  | Bob|   97|   102|             85.4|
  +----+-----+------+-----------------+
   */

  // calculate the running average grade per course backwards
  val byCourse2: WindowSpec = Window partitionBy 'course orderBy 'grade rowsBetween (Window.currentRow, Window.unboundedFollowing)
  df1.withColumn("avg_down_to_end", avg('grade) over byCourse2).show
  /*
  +----+-----+------+-----------------+
  |name|grade|course|  avg_down_to_end|
  +----+-----+------+-----------------+
  | Amy|   65|   101|82.83333333333333|
  | Amy|   72|   101|             86.4|
  | Amy|   85|   101|             90.0|
  | Bob|   85|   101|91.66666666666667|
  | Amy|   95|   101|             95.0|
  | Bob|   95|   101|             95.0|
  | Amy|   82|   103|             89.0|
  | Bob|   85|   103|91.33333333333333|
  | Amy|   90|   103|             94.5|
  | Bob|   99|   103|             99.0|
  | Amy|   65|   102|             85.4|
  | Amy|   85|   102|             90.5|
  | Bob|   85|   102|92.33333333333333|
  | Bob|   95|   102|             96.0|
  | Bob|   97|   102|             97.0|
  +----+-----+------+-----------------+
   */

  // calculate the running average between this grade, the previous grade and the next grade per person
  //   and when there is no previous grade or next grade, ignore
  val byName1: WindowSpec = Window partitionBy 'name orderBy 'grade rowsBetween (-1, 1)
  df1.withColumn("running_avg", avg('grade) over byName1).show
  /*
  +----+-----+------+-----------------+
  |name|grade|course|      running_avg|
  +----+-----+------+-----------------+
  | Amy|   65|   101|             65.0|
  | Amy|   65|   102|67.33333333333333|
  | Amy|   72|   101|             73.0|
  | Amy|   82|   103|79.66666666666667|
  | Amy|   85|   101|             84.0|
  | Amy|   85|   102|86.66666666666667|
  | Amy|   90|   103|             90.0|
  | Amy|   95|   101|             92.5|
  | Bob|   85|   101|             85.0|
  | Bob|   85|   103|             85.0|
  | Bob|   85|   102|88.33333333333333|
  | Bob|   95|   102|91.66666666666667|
  | Bob|   95|   101|95.66666666666667|
  | Bob|   97|   102|             97.0|
  | Bob|   99|   103|             98.0|
  +----+-----+------+-----------------+
   */

  // compare the previous result with the following example
  val byNameTest1: WindowSpec = Window partitionBy 'name orderBy 'grade
  df1.withColumn("running_avg", (lag('grade, 1).over(byNameTest1) + 'grade + lead('grade, 1).over(byNameTest1)) / 3).show
  /*
  +----+-----+------+-----------------+
  |name|grade|course|      running_avg|
  +----+-----+------+-----------------+
  | Amy|   65|   101|             null|
  | Amy|   65|   102|67.33333333333333|
  | Amy|   72|   101|             73.0|
  | Amy|   82|   103|79.66666666666667|
  | Amy|   85|   101|             84.0|
  | Amy|   85|   102|86.66666666666667|
  | Amy|   90|   103|             90.0|
  | Amy|   95|   101|             null|
  | Bob|   85|   101|             null|
  | Bob|   85|   103|             85.0|
  | Bob|   85|   102|88.33333333333333|
  | Bob|   95|   102|91.66666666666667|
  | Bob|   95|   101|95.66666666666667|
  | Bob|   97|   102|             97.0|
  | Bob|   99|   103|             null|
  +----+-----+------+-----------------+
   */

  // this example shows the difference between rowsBetween and rangeBetween
  val byCourse3: WindowSpec = Window partitionBy 'course orderBy 'grade rangeBetween (Window.unboundedPreceding, Window.currentRow)
  df1.withColumn("avg_up_to_now", avg('grade) over byCourse3).show
  /*
  +----+-----+------+-----------------+
  |name|grade|course|    avg_up_to_now|
  +----+-----+------+-----------------+
  | Amy|   65|   101|             65.0|
  | Amy|   72|   101|             68.5|
  | Amy|   85|   101|            76.75| // 74.0 for rowsBetween
  | Bob|   85|   101|            76.75|
  | Amy|   95|   101|82.83333333333333| // 80.4 for rowsBetween
  | Bob|   95|   101|82.83333333333333|
  | Amy|   82|   103|             82.0|
  | Bob|   85|   103|             83.5|
  | Amy|   90|   103|85.66666666666667|
  | Bob|   99|   103|             89.0|
  | Amy|   65|   102|             65.0|
  | Amy|   85|   102|78.33333333333333| // 75.0 for rowsBetween
  | Bob|   85|   102|78.33333333333333|
  | Bob|   95|   102|             82.5|
  | Bob|   97|   102|             85.4|
  +----+-----+------+-----------------+
   */
  // this is because rangeBetween uses actual values of data rather than row numbers
  //   rangeBetween(Window.unboundedPreceding, Window.currentRow) for course 101 means
  //      (first_grade - current_grade, 0) for each row, for example, for the second row,
  //      it is (-7, 0), for the third row, it is (-20, 0)
  // therefore we have some differences of avg_up_to_now, as rangeBetween only considers
  //    actual values, so it takes all values eligible into consideration

  // the next example better shows the difference
  val byCourse4: WindowSpec = Window partitionBy 'course orderBy 'grade rangeBetween (-10, Window.currentRow)
  df1.withColumn("avg_up_to_now", avg('grade) over byCourse4).show
  /*
  +----+-----+------+-----------------+
  |name|grade|course|    avg_up_to_now|
  +----+-----+------+-----------------+
  | Amy|   65|   101|             65.0| // 65
  | Amy|   72|   101|             68.5| // 65, 72
  | Amy|   85|   101|             85.0| // 85, 85
  | Bob|   85|   101|             85.0| // 85, 85
  | Amy|   95|   101|             90.0| // 85, 85, 95, 95
  | Bob|   95|   101|             90.0| // 85, 85, 95, 95
  | Amy|   82|   103|             82.0|
  | Bob|   85|   103|             83.5|
  | Amy|   90|   103|85.66666666666667|
  | Bob|   99|   103|             94.5|
  | Amy|   65|   102|             65.0|
  | Amy|   85|   102|             85.0|
  | Bob|   85|   102|             85.0|
  | Bob|   95|   102|88.33333333333333|
  | Bob|   97|   102|             96.0|
  +----+-----+------+-----------------+
   */
  // the comments shows the actual values that take into account

}
