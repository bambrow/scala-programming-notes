package spark

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{Dataset, Row, SparkSession}

/**
 * This file shows a common practice of how to
 *   handling nulls when using joinWith.
 */

object Dataset5 extends App {

  implicit val ss: SparkSession = SparkSession.builder.master("local").getOrCreate

  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)

  import ss.implicits._

  val df1: Dataset[Row] = Seq(
    ("Alice", 18),
    ("Bob", 23),
    ("Cathy", 20),
    ("David", 27),
    ("Eva", 22),
    ("Fred", 29)
  ) toDF ("name", "age")

  val df2: Dataset[Row] = Seq(
    ("Alice", 3000),
    ("Cathy", 4500),
    ("David", 10000),
    ("Fred", 2000),
    ("George", 9000)
  ) toDF ("name", "salary")

  val ds1 = df1.as[PersonWithAge]
  val ds2 = df2.as[PersonWithSalary]

  val ds3 = ds1.joinWith(ds2, ds1("name") === ds2("name"), "full")
  ds3.show
  /*
  +-----------+--------------+
  |         _1|            _2|
  +-----------+--------------+
  | [Fred, 29]|  [Fred, 2000]|
  |  [Eva, 22]|          null|
  |  [Bob, 23]|          null|
  |[Alice, 18]| [Alice, 3000]|
  |       null|[George, 9000]|
  |[David, 27]|[David, 10000]|
  |[Cathy, 20]| [Cathy, 4500]|
  +-----------+--------------+
   */

  // when there is null, explicitly assigning the class will give MatchError
  // scala.MatchError: (PersonWithAge(Eva,22),null) (of class scala.Tuple2)
  /*
  val ds4 = ds3 map {
    case (x: PersonWithAge, y: PersonWithSalary) => {
      if (x == null) Person(y.name, -1, y.salary)
      else if (y == null) Person(x.name, x.age, -1)
      else Person(x.name, x.age, y.salary)
    }
  }
  ds4.show
  */

  // removing the class names works
  val ds4 = ds3 map {
    case (x, y) => {
      if (x == null) Person(y.name, -1, y.salary)
      else if (y == null) Person(x.name, x.age, -1)
      else Person(x.name, x.age, y.salary)
    }
  }
  ds4.show
  /*
  +------+---+------+
  |  name|age|salary|
  +------+---+------+
  |  Fred| 29|  2000|
  |   Eva| 22|    -1|
  |   Bob| 23|    -1|
  | Alice| 18|  3000|
  |George| -1|  9000|
  | David| 27| 10000|
  | Cathy| 20|  4500|
  +------+---+------+
   */

  // or use multiple cases
  val ds5 = ds3 map {
    case (null, y: PersonWithSalary) => Person(y.name, -1, y.salary)
    case (x: PersonWithAge, null) => Person(x.name, x.age, -1)
    case (x: PersonWithAge, y: PersonWithSalary) => Person(x.name, x.age, y.salary)
  }
  ds5.show
  /*
  +------+---+------+
  |  name|age|salary|
  +------+---+------+
  |  Fred| 29|  2000|
  |   Eva| 22|    -1|
  |   Bob| 23|    -1|
  | Alice| 18|  3000|
  |George| -1|  9000|
  | David| 27| 10000|
  | Cathy| 20|  4500|
  +------+---+------+
   */


}
