package spark

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.functions.udf
import org.apache.spark.sql.{Dataset, Row, SparkSession}

object UDFTest extends App {

  implicit val ss: SparkSession = SparkSession.builder.master("local").getOrCreate

  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)

  import ss.implicits._

  val df1: Dataset[Row] = Seq[(String, java.lang.Integer, Int)](
    ("Amy", 65, 101),
    ("Amy", null, 102),
    ("Amy", 72, 101),
    ("Amy", 82, 103),
    ("Amy", null, 101),
    ("Amy", 85, 102),
    ("Amy", 90, 103),
    ("Amy", 95, 101),
    ("Bob", 85, 101),
    ("Bob", null, 103),
    ("Bob", 85, 102),
    ("Bob", 95, 102),
    ("Bob", null, 101),
    ("Bob", 97, 102),
    ("Bob", 99, 103)
  ) toDF ("name", "grade", "course")

  val toLetterGrade: Int => String = (x: Int) => {
    if (x < 60) "F"
    else if (x < 70) "D"
    else if (x < 80) "C"
    else if (x < 90) "B"
    else "A"
  }

  ss.udf.register("toLetterGrade", toLetterGrade)

  ss.range(start = 55, end = 105, step = 10).createOrReplaceTempView("test1")

  ss.sql("select * from test1").show
  /*
  +---+
  | id|
  +---+
  | 55|
  | 65|
  | 75|
  | 85|
  | 95|
  +---+
   */

  ss.sql("select id as grade, toLetterGrade(id) as letter_grade from test1").show
  /*
  +-----+------------+
  |grade|letter_grade|
  +-----+------------+
  |   55|           F|
  |   65|           D|
  |   75|           C|
  |   85|           B|
  |   95|           A|
  +-----+------------+
   */

  val toLetterGradeUDF: UserDefinedFunction = udf {
    x: Int => {
      if (x < 60) "F"
      else if (x < 70) "D"
      else if (x < 80) "C"
      else if (x < 90) "B"
      else "A"
    }
  }
  val toLetterGradeUDF2: UserDefinedFunction = udf(toLetterGrade)

  ss.range(start = 55, end = 105, step = 10).select($"id" as "grade", toLetterGradeUDF($"id") as "letter_grade").show
  /*
  +-----+------------+
  |grade|letter_grade|
  +-----+------------+
  |   55|           F|
  |   65|           D|
  |   75|           C|
  |   85|           B|
  |   95|           A|
  +-----+------------+
   */

  // this magically works
  df1.withColumn("letter_grade", toLetterGradeUDF($"grade")).show
  /*
  +----+-----+------+------------+
  |name|grade|course|letter_grade|
  +----+-----+------+------------+
  | Amy|   65|   101|           D|
  | Amy| null|   102|        null|
  | Amy|   72|   101|           C|
  | Amy|   82|   103|           B|
  | Amy| null|   101|        null|
  | Amy|   85|   102|           B|
  | Amy|   90|   103|           A|
  | Amy|   95|   101|           A|
  | Bob|   85|   101|           B|
  | Bob| null|   103|        null|
  | Bob|   85|   102|           B|
  | Bob|   95|   102|           A|
  | Bob| null|   101|        null|
  | Bob|   97|   102|           A|
  | Bob|   99|   103|           A|
  +----+-----+------+------------+
   */

  val toLetterGradeNullSafe: java.lang.Integer => Option[String] = (x: java.lang.Integer) => {
    if (x == null) None else Some(toLetterGrade(x))
  }

  val toLetterGradeUDFNullSafe: UserDefinedFunction = udf(toLetterGradeNullSafe)

  // common case to handle null values
  df1.withColumn("letter_grade", toLetterGradeUDFNullSafe($"grade")).show
  /*
  +----+-----+------+------------+
  |name|grade|course|letter_grade|
  +----+-----+------+------------+
  | Amy|   65|   101|           D|
  | Amy| null|   102|        null|
  | Amy|   72|   101|           C|
  | Amy|   82|   103|           B|
  | Amy| null|   101|        null|
  | Amy|   85|   102|           B|
  | Amy|   90|   103|           A|
  | Amy|   95|   101|           A|
  | Bob|   85|   101|           B|
  | Bob| null|   103|        null|
  | Bob|   85|   102|           B|
  | Bob|   95|   102|           A|
  | Bob| null|   101|        null|
  | Bob|   97|   102|           A|
  | Bob|   99|   103|           A|
  +----+-----+------+------------+
   */

  def toLetterGradeUDF3(f: Int, d: Int, c: Int, b: Int): UserDefinedFunction = udf {
    x: Int => {
      if (x < f) "F"
      else if (x < d) "D"
      else if (x < c) "C"
      else if (x < b) "B"
      else "A"
    }
  }

  df1.withColumn("letter_grade", toLetterGradeUDF3(70, 80, 90, 95)('grade)).show
  /*
  +----+-----+------+------------+
  |name|grade|course|letter_grade|
  +----+-----+------+------------+
  | Amy|   65|   101|           F|
  | Amy| null|   102|        null|
  | Amy|   72|   101|           D|
  | Amy|   82|   103|           C|
  | Amy| null|   101|        null|
  | Amy|   85|   102|           C|
  | Amy|   90|   103|           B|
  | Amy|   95|   101|           A|
  | Bob|   85|   101|           C|
  | Bob| null|   103|        null|
  | Bob|   85|   102|           C|
  | Bob|   95|   102|           A|
  | Bob| null|   101|        null|
  | Bob|   97|   102|           A|
  | Bob|   99|   103|           A|
  +----+-----+------+------------+
   */

  val toLetterGradeUDF4: UserDefinedFunction = udf {
    (x: Int, y: Int) => {
      def toLetterGrade2(f: Int, d: Int, c: Int, b: Int): String = {
        if (x < f) "F"
        else if (x < d) "D"
        else if (x < c) "C"
        else if (x < b) "B"
        else "A"
      }
      if (y == 101) toLetterGrade2(60, 70, 80, 90)
      else if (y == 102) toLetterGrade2(65, 75, 85, 95)
      else toLetterGrade2(70, 80, 90, 95)
    }
  }

  df1.withColumn("letter_grade", toLetterGradeUDF4($"grade", $"course")).show
  /*
  +----+-----+------+------------+
  |name|grade|course|letter_grade|
  +----+-----+------+------------+
  | Amy|   65|   101|           D|
  | Amy| null|   102|        null|
  | Amy|   72|   101|           C|
  | Amy|   82|   103|           C|
  | Amy| null|   101|        null|
  | Amy|   85|   102|           B|
  | Amy|   90|   103|           B|
  | Amy|   95|   101|           A|
  | Bob|   85|   101|           B|
  | Bob| null|   103|        null|
  | Bob|   85|   102|           B|
  | Bob|   95|   102|           A|
  | Bob| null|   101|        null|
  | Bob|   97|   102|           A|
  | Bob|   99|   103|           A|
  +----+-----+------+------------+
   */

  def minusAndPlus: Int => (Int, Int) = {
    x: Int => (x - 1, x + 1)
  }

  val minusAndPlusUDF: UserDefinedFunction = udf(minusAndPlus)

  ss.range(1, 10).withColumn("minus_plus", minusAndPlusUDF($"id")).show
  /*
  +---+----------+
  | id|minus_plus|
  +---+----------+
  |  1|    [0, 2]|
  |  2|    [1, 3]|
  |  3|    [2, 4]|
  |  4|    [3, 5]|
  |  5|    [4, 6]|
  |  6|    [5, 7]|
  |  7|    [6, 8]|
  |  8|    [7, 9]|
  |  9|   [8, 10]|
  +---+----------+
   */

  ss.range(1, 10).withColumn("minus_plus", minusAndPlusUDF($"id")).printSchema
  /*
  root
   |-- id: long (nullable = false)
   |-- minus_plus: struct (nullable = true)
   |    |-- _1: integer (nullable = false)
   |    |-- _2: integer (nullable = false)
   */

}
