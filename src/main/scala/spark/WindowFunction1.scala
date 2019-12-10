package spark

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.expressions.{Window, WindowSpec}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{Column, Dataset, Row, SparkSession}

/**
 * This file tests the following Window functions:
 *   partitionBy, orderBy
 * And the following Column method:
 *   over
 * And the following sql.functions:
 *   rank, dense_rank, lag, lead, cume_dist,
 *   row_number, ntile, percent_rank
 */

object WindowFunction1 extends App {

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

  // calculate the average grade per course
  val byCourse: WindowSpec = Window partitionBy 'course
  df1.withColumn("avg", avg('grade) over byCourse).show
  /*
  +----+-----+------+-----------------+
  |name|grade|course|              avg|
  +----+-----+------+-----------------+
  | Amy|   65|   101|82.83333333333333|
  | Amy|   72|   101|82.83333333333333|
  | Amy|   85|   101|82.83333333333333|
  | Amy|   95|   101|82.83333333333333|
  | Bob|   85|   101|82.83333333333333|
  | Bob|   95|   101|82.83333333333333|
  | Amy|   82|   103|             89.0|
  | Amy|   90|   103|             89.0|
  | Bob|   85|   103|             89.0|
  | Bob|   99|   103|             89.0|
  | Amy|   65|   102|             85.4|
  | Amy|   85|   102|             85.4|
  | Bob|   85|   102|             85.4|
  | Bob|   95|   102|             85.4|
  | Bob|   97|   102|             85.4|
  +----+-----+------+-----------------+
   */

  // collect the courses per odd/even grade
  val byOddEvenGrade: WindowSpec = Window partitionBy 'grade.mod(2)
  df1.withColumn("course_with_odd_even_grade", collect_set('course) over byOddEvenGrade).show
  /*
  +----+-----+------+--------------------------+
  |name|grade|course|course_with_odd_even_grade|
  +----+-----+------+--------------------------+
  | Amy|   65|   101|           [102, 103, 101]|
  | Amy|   65|   102|           [102, 103, 101]|
  | Amy|   85|   101|           [102, 103, 101]|
  | Amy|   85|   102|           [102, 103, 101]|
  | Amy|   95|   101|           [102, 103, 101]|
  | Bob|   85|   101|           [102, 103, 101]|
  | Bob|   85|   103|           [102, 103, 101]|
  | Bob|   85|   102|           [102, 103, 101]|
  | Bob|   95|   102|           [102, 103, 101]|
  | Bob|   95|   101|           [102, 103, 101]|
  | Bob|   97|   102|           [102, 103, 101]|
  | Bob|   99|   103|           [102, 103, 101]|
  | Amy|   72|   101|                [103, 101]|
  | Amy|   82|   103|                [103, 101]|
  | Amy|   90|   103|                [103, 101]|
  +----+-----+------+--------------------------+
   */

  // calculate the rank of grade by course
  val byCourseGradeDesc: WindowSpec = Window partitionBy 'course orderBy 'grade.desc
  df1.withColumn("rank_by_course", rank over byCourseGradeDesc).orderBy('course, 'rank_by_course).show
  /*
  +----+-----+------+--------------+
  |name|grade|course|rank_by_course|
  +----+-----+------+--------------+
  | Bob|   95|   101|             1|
  | Amy|   95|   101|             1|
  | Amy|   85|   101|             3|
  | Bob|   85|   101|             3|
  | Amy|   72|   101|             5|
  | Amy|   65|   101|             6|
  | Bob|   97|   102|             1|
  | Bob|   95|   102|             2|
  | Amy|   85|   102|             3|
  | Bob|   85|   102|             3|
  | Amy|   65|   102|             5|
  | Bob|   99|   103|             1|
  | Amy|   90|   103|             2|
  | Bob|   85|   103|             3|
  | Amy|   82|   103|             4|
  +----+-----+------+--------------+
   */

  // calculate the top 2 grade by course
  df1.withColumn("rank", dense_rank over byCourseGradeDesc).where('rank <= 2).show
  /*
  +----+-----+------+----+
  |name|grade|course|rank|
  +----+-----+------+----+
  | Amy|   95|   101|   1|
  | Bob|   95|   101|   1|
  | Amy|   85|   101|   2|
  | Bob|   85|   101|   2|
  | Bob|   99|   103|   1|
  | Amy|   90|   103|   2|
  | Bob|   97|   102|   1|
  | Bob|   95|   102|   2|
  +----+-----+------+----+
   */

  // calculate the difference from the best grade per course
  df1.withColumn("grade_diff", (max('grade) over byCourseGradeDesc) - 'grade).show
  /*
  +----+-----+------+----------+
  |name|grade|course|grade_diff|
  +----+-----+------+----------+
  | Amy|   95|   101|         0|
  | Bob|   95|   101|         0|
  | Amy|   85|   101|        10|
  | Bob|   85|   101|        10|
  | Amy|   72|   101|        23|
  | Amy|   65|   101|        30|
  | Bob|   99|   103|         0|
  | Amy|   90|   103|         9|
  | Bob|   85|   103|        14|
  | Amy|   82|   103|        17|
  | Bob|   97|   102|         0|
  | Bob|   95|   102|         2|
  | Amy|   85|   102|        12|
  | Bob|   85|   102|        12|
  | Amy|   65|   102|        32|
  +----+-----+------+----------+
   */

  // calculate the difference between this grade and the next grade per person
  //   and when there is no next grade, use 100 as the next grade
  val byNameGradeAsc: WindowSpec = Window partitionBy 'name orderBy 'grade
  df1.withColumn("diff", (lead('grade, 1, 100) over byNameGradeAsc) - 'grade).show
  /*
  +----+-----+------+----+
  |name|grade|course|diff|
  +----+-----+------+----+
  | Amy|   65|   101|   0|
  | Amy|   65|   102|   7|
  | Amy|   72|   101|  10|
  | Amy|   82|   103|   3|
  | Amy|   85|   101|   0|
  | Amy|   85|   102|   5|
  | Amy|   90|   103|   5|
  | Amy|   95|   101|   5|
  | Bob|   85|   101|   0|
  | Bob|   85|   103|   0|
  | Bob|   85|   102|  10|
  | Bob|   95|   102|   0|
  | Bob|   95|   101|   2|
  | Bob|   97|   102|   2|
  | Bob|   99|   103|   1|
  +----+-----+------+----+
   */

  // calculate the running average between this grade, the previous grade and the next grade per person
  //   and when there is no previous grade or next grade, return null
  df1.withColumn("running_avg", (lag('grade, 1).over(byNameGradeAsc) + 'grade + lead('grade, 1).over(byNameGradeAsc)) / 3).show
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

  // calculate the grade running total per course
  val byCourseGradeNameAsc: WindowSpec = Window partitionBy 'course orderBy ('grade, 'name)
  val runningTotalByCourse1: Column = sum('grade) over byCourseGradeNameAsc as "running_total"
  df1.select('*, runningTotalByCourse1).show
  /*
  +----+-----+------+-------------+
  |name|grade|course|running_total|
  +----+-----+------+-------------+
  | Amy|   65|   101|           65|
  | Amy|   72|   101|          137|
  | Amy|   85|   101|          222|
  | Bob|   85|   101|          307|
  | Amy|   95|   101|          402|
  | Bob|   95|   101|          497|
  | Amy|   82|   103|           82|
  | Bob|   85|   103|          167|
  | Amy|   90|   103|          257|
  | Bob|   99|   103|          356|
  | Amy|   65|   102|           65|
  | Amy|   85|   102|          150|
  | Bob|   85|   102|          235|
  | Bob|   95|   102|          330|
  | Bob|   97|   102|          427|
  +----+-----+------+-------------+
   */

  // compare the previous result with the following 2 examples
  val byCourseGradeAsc: WindowSpec = Window partitionBy 'course orderBy 'grade
  val runningTotalByCourse2: Column = sum('grade) over byCourseGradeAsc as "running_total"
  df1.select('*, runningTotalByCourse2).show
  /*
  +----+-----+------+-------------+
  |name|grade|course|running_total|
  +----+-----+------+-------------+
  | Amy|   65|   101|           65|
  | Amy|   72|   101|          137|
  | Amy|   85|   101|          307|
  | Bob|   85|   101|          307|
  | Amy|   95|   101|          497|
  | Bob|   95|   101|          497|
  | Amy|   82|   103|           82|
  | Bob|   85|   103|          167|
  | Amy|   90|   103|          257|
  | Bob|   99|   103|          356|
  | Amy|   65|   102|           65|
  | Amy|   85|   102|          235|
  | Bob|   85|   102|          235|
  | Bob|   95|   102|          330|
  | Bob|   97|   102|          427|
  +----+-----+------+-------------+
   */

  val byCourseNameAsc: WindowSpec = Window partitionBy 'course orderBy 'name
  val runningTotalByCourse3: Column = sum('grade) over byCourseNameAsc as "running_total"
  df1.select('*, runningTotalByCourse3).show
  /*
  +----+-----+------+-------------+
  |name|grade|course|running_total|
  +----+-----+------+-------------+
  | Amy|   65|   101|          317|
  | Amy|   72|   101|          317|
  | Amy|   85|   101|          317|
  | Amy|   95|   101|          317|
  | Bob|   85|   101|          497|
  | Bob|   95|   101|          497|
  | Amy|   82|   103|          172|
  | Amy|   90|   103|          172|
  | Bob|   85|   103|          356|
  | Bob|   99|   103|          356|
  | Amy|   65|   102|          150|
  | Amy|   85|   102|          150|
  | Bob|   85|   102|          427|
  | Bob|   95|   102|          427|
  | Bob|   97|   102|          427|
  +----+-----+------+-------------+
   */

  df1.withColumn("cume_dist", cume_dist over byNameGradeAsc).show
  /*
  +----+-----+------+-------------------+
  |name|grade|course|          cume_dist|
  +----+-----+------+-------------------+
  | Amy|   65|   101|               0.25|
  | Amy|   65|   102|               0.25|
  | Amy|   72|   101|              0.375|
  | Amy|   82|   103|                0.5|
  | Amy|   85|   101|               0.75|
  | Amy|   85|   102|               0.75|
  | Amy|   90|   103|              0.875|
  | Amy|   95|   101|                1.0|
  | Bob|   85|   101|0.42857142857142855|
  | Bob|   85|   103|0.42857142857142855|
  | Bob|   85|   102|0.42857142857142855|
  | Bob|   95|   102| 0.7142857142857143|
  | Bob|   95|   101| 0.7142857142857143|
  | Bob|   97|   102| 0.8571428571428571|
  | Bob|   99|   103|                1.0|
  +----+-----+------+-------------------+
   */

  df1.withColumn("row_number", row_number over byNameGradeAsc).show
  /*
  +----+-----+------+----------+
  |name|grade|course|row_number|
  +----+-----+------+----------+
  | Amy|   65|   101|         1|
  | Amy|   65|   102|         2|
  | Amy|   72|   101|         3|
  | Amy|   82|   103|         4|
  | Amy|   85|   101|         5|
  | Amy|   85|   102|         6|
  | Amy|   90|   103|         7|
  | Amy|   95|   101|         8|
  | Bob|   85|   101|         1|
  | Bob|   85|   103|         2|
  | Bob|   85|   102|         3|
  | Bob|   95|   102|         4|
  | Bob|   95|   101|         5|
  | Bob|   97|   102|         6|
  | Bob|   99|   103|         7|
  +----+-----+------+----------+
   */

  df1.withColumn("ntile", ntile(3) over byNameGradeAsc).show
  /*
  +----+-----+------+-----+
  |name|grade|course|ntile|
  +----+-----+------+-----+
  | Amy|   65|   101|    1|
  | Amy|   65|   102|    1|
  | Amy|   72|   101|    1|
  | Amy|   82|   103|    2|
  | Amy|   85|   101|    2|
  | Amy|   85|   102|    2|
  | Amy|   90|   103|    3|
  | Amy|   95|   101|    3|
  | Bob|   85|   101|    1|
  | Bob|   85|   103|    1|
  | Bob|   85|   102|    1|
  | Bob|   95|   102|    2|
  | Bob|   95|   101|    2|
  | Bob|   97|   102|    3|
  | Bob|   99|   103|    3|
  +----+-----+------+-----+
   */

  df1.withColumn("percent_rank", percent_rank over byNameGradeAsc).show
  /*
  +----+-----+------+-------------------+
  |name|grade|course|       percent_rank|
  +----+-----+------+-------------------+
  | Amy|   65|   101|                0.0|
  | Amy|   65|   102|                0.0|
  | Amy|   72|   101| 0.2857142857142857|
  | Amy|   82|   103|0.42857142857142855|
  | Amy|   85|   101| 0.5714285714285714|
  | Amy|   85|   102| 0.5714285714285714|
  | Amy|   90|   103| 0.8571428571428571|
  | Amy|   95|   101|                1.0|
  | Bob|   85|   101|                0.0|
  | Bob|   85|   103|                0.0|
  | Bob|   85|   102|                0.0|
  | Bob|   95|   102|                0.5|
  | Bob|   95|   101|                0.5|
  | Bob|   97|   102| 0.8333333333333334|
  | Bob|   99|   103|                1.0|
  +----+-----+------+-------------------+
   */

}
