package spark

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{Dataset, Row, SparkSession}

/**
 * This file tests the following methods:
 *   groupByKey, mapGroups, flatMap, intersect, intersectAll,
 *   joinWith, limit, orderBy, randomSplit, sample, select,
 *   sort, sortWithinPartitions, where
 */

object Dataset3 extends App {

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

  val df3: Dataset[Row] = Seq(
    ("Amy", 65),
    ("Amy", 65),
    ("Amy", 72),
    ("Amy", 82),
    ("Amy", 85),
    ("Amy", 85),
    ("Amy", 90),
    ("Amy", 95),
    ("Bob", 85),
    ("Bob", 85),
    ("Bob", 85),
    ("Bob", 95),
    ("Bob", 95),
    ("Bob", 97),
    ("Bob", 99)
  ) toDF ("name", "grade")

  val ds3 = df3.as[Grade] groupByKey (_.name) mapGroups {
    (k: String, vs: Iterator[Grade]) => Student(k, (vs map (_.grade)).toSeq)
  }

  ds3.show
  /*
  +----+--------------------+
  |name|              grades|
  +----+--------------------+
  | Amy|[65, 65, 72, 82, ...|
  | Bob|[85, 85, 85, 95, ...|
  +----+--------------------+
   */

  println

  val ds3b = (ds3 flatMap {
    x: Student => x.grades map {
      Grade(x.name, _)
    }
  }).distinct

  ds3b.show
  /*
  +----+-----+
  |name|grade|
  +----+-----+
  | Amy|   82|
  | Bob|   95|
  | Bob|   99|
  | Amy|   90|
  | Amy|   72|
  | Bob|   97|
  | Amy|   95|
  | Bob|   85|
  | Amy|   65|
  | Amy|   85|
  +----+-----+
   */

  println

  val ds1 = df1.as[PersonWithAge]
  val ds2 = df2.as[PersonWithSalary]

  ((ds1 map (_.name)) intersect (ds2 map (_.name))).collect foreach println
  /*
  Fred
  Alice
  David
  Cathy
   */

  println

  ((ds3 map (_.name)) intersectAll (ds1 map (_.name))).collect foreach println
  // Bob

  println

  val ds4 = ds1 joinWith (ds2, ds1.col("name") === ds2.col("name")) map {
    case (x, y) => Person(x.name, x.age, y.salary)
  }

  ds4.show
  /*
  +-----+---+------+
  | name|age|salary|
  +-----+---+------+
  |Alice| 18|  3000|
  |Cathy| 20|  4500|
  |David| 27| 10000|
  | Fred| 29|  2000|
  +-----+---+------+
   */

  println

  val ds1a = ds1 limit 3
  ds1a.show
  /*
  +-----+---+
  | name|age|
  +-----+---+
  |Alice| 18|
  |  Bob| 23|
  |Cathy| 20|
  +-----+---+
   */

  println

  val ds1b = ds1 repartition 3 mapPartitions {
    x => x map {
      y => PersonWithAge(y.name, y.age + 10)
    }
  }

  ds1b.show
  /*
  +-----+---+
  | name|age|
  +-----+---+
  |Alice| 28|
  |Cathy| 30|
  | Fred| 39|
  |  Eva| 32|
  |  Bob| 33|
  |David| 37|
  +-----+---+
   */

  println

  val ds1c = ds1 orderBy "age"
  ds1c.show
  /*
  +-----+---+
  | name|age|
  +-----+---+
  |Alice| 18|
  |Cathy| 20|
  |  Eva| 22|
  |  Bob| 23|
  |David| 27|
  | Fred| 29|
  +-----+---+
   */

  val ds1d = ds1 orderBy ("age", "name")
  ds1d.show

  println

  val darr = ds1 randomSplit Array(0.1, 0.1, 0.8)
  darr foreach {
    dsx => {
      dsx.collect foreach {
        x => print(x + " | ")
      }
      println
    }
  }
  /*
  PersonWithAge(Bob,23) |

  PersonWithAge(Alice,18) | PersonWithAge(Cathy,20) | PersonWithAge(David,27) | PersonWithAge(Eva,22) | PersonWithAge(Fred,29) |
   */

  println

  val ds1e = ds1 sample 0.5
  ds1e.show
  /*
  +-----+---+
  | name|age|
  +-----+---+
  |  Bob| 23|
  |Cathy| 20|
  | Fred| 29|
  +-----+---+
   */

  val ds1f = ds1 sample (true, 0.7)
  ds1f.show
  /*
  +-----+---+
  | name|age|
  +-----+---+
  |Alice| 18|
  |Alice| 18|
  |Alice| 18|
  |  Bob| 23|
  |  Bob| 23|
  |David| 27|
  +-----+---+
   */

  println

  val ds4a = ds4 select ("name", "age")
  ds4a.show
  /*
  +-----+---+
  | name|age|
  +-----+---+
  |Alice| 18|
  |Cathy| 20|
  |David| 27|
  | Fred| 29|
  +-----+---+
   */

  println

  val ds4b = ds4 sort ("age", "salary")
  ds4b.show
  /*
  +-----+---+------+
  | name|age|salary|
  +-----+---+------+
  |Alice| 18|  3000|
  |Cathy| 20|  4500|
  |David| 27| 10000|
  | Fred| 29|  2000|
  +-----+---+------+
   */

  println

  val ds1g = ds1 repartition 3 sortWithinPartitions "age"
  ds1g.show
  /*
  +-----+---+
  | name|age|
  +-----+---+
  |Alice| 18|
  |Cathy| 20|
  |  Eva| 22|
  | Fred| 29|
  |  Bob| 23|
  |David| 27|
  +-----+---+
   */

  val ds1h = ds1 where "age > 25"
  ds1h.show
  /*
  +-----+---+
  | name|age|
  +-----+---+
  |David| 27|
  | Fred| 29|
  +-----+---+
   */

  val ds1i = ds1 where ($"age" > 25)
  ds1i.show

  println

}
