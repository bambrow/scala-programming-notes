package spark

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{Dataset, Row, SparkSession}

object SparkSessionTest extends App {

  implicit val ss: SparkSession = SparkSession.builder.master("local").getOrCreate

  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)

  import ss.implicits._

  println(ss.version)
  // 2.4.4

  ss.emptyDataFrame.printSchema
  /*
  root

   */
  ss.emptyDataset[String].printSchema
  /*
  root
   |-- value: string (nullable = true)
   */

  // for udf registration please check UDFTest

  ss.range(0, 5).createOrReplaceTempView("view1")
  ss.range(5, 10).createOrReplaceGlobalTempView("view2")

  ss.sql("select * from view1").show
  /*
  +---+
  | id|
  +---+
  |  0|
  |  1|
  |  2|
  |  3|
  |  4|
  +---+
   */

  ss.sql("show databases").show
  /*
  +------------+
  |databaseName|
  +------------+
  |     default|
  +------------+
   */

  ss.sql("show tables").show
  /*
  +--------+---------+-----------+
  |database|tableName|isTemporary|
  +--------+---------+-----------+
  |        |    view1|       true|
  +--------+---------+-----------+
   */

  val df1: Dataset[Row] = ss.table("view1")
  df1.show
  /*
  +---+
  | id|
  +---+
  |  0|
  |  1|
  |  2|
  |  3|
  |  4|
  +---+
   */

  ss.catalog.listDatabases.show
  /*
  +-------+----------------+--------------------+
  |   name|     description|         locationUri|
  +-------+----------------+--------------------+
  |default|default database|file:/Users/xxxxx...|
  +-------+----------------+--------------------+
   */

  ss.catalog.listTables.show
  /*
  +-----+--------+-----------+---------+-----------+
  | name|database|description|tableType|isTemporary|
  +-----+--------+-----------+---------+-----------+
  |view1|    null|       null|TEMPORARY|       true|
  +-----+--------+-----------+---------+-----------+
   */

  ss.catalog.listFunctions.show
  /*
  +----------+--------+-----------+--------------------+-----------+
  |      name|database|description|           className|isTemporary|
  +----------+--------+-----------+--------------------+-----------+
  |         !|    null|       null|org.apache.spark....|       true|
  |         %|    null|       null|org.apache.spark....|       true|
  |         &|    null|       null|org.apache.spark....|       true|
  |         *|    null|       null|org.apache.spark....|       true|
  |         +|    null|       null|org.apache.spark....|       true|
  |         -|    null|       null|org.apache.spark....|       true|
  |         /|    null|       null|org.apache.spark....|       true|
  |         <|    null|       null|org.apache.spark....|       true|
  |        <=|    null|       null|org.apache.spark....|       true|
  |       <=>|    null|       null|org.apache.spark....|       true|
  |         =|    null|       null|org.apache.spark....|       true|
  |        ==|    null|       null|org.apache.spark....|       true|
  |         >|    null|       null|org.apache.spark....|       true|
  |        >=|    null|       null|org.apache.spark....|       true|
  |         ^|    null|       null|org.apache.spark....|       true|
  |       abs|    null|       null|org.apache.spark....|       true|
  |      acos|    null|       null|org.apache.spark....|       true|
  |add_months|    null|       null|org.apache.spark....|       true|
  | aggregate|    null|       null|org.apache.spark....|       true|
  |       and|    null|       null|org.apache.spark....|       true|
  +----------+--------+-----------+--------------------+-----------+
  only showing top 20 rows
   */

  println(ss.catalog.tableExists("view1")) // true
  println(ss.catalog.tableExists("global_temp", "view2")) // true

  println(ss.catalog.getTable("view1")) // Table[name='view1', tableType='TEMPORARY', isTemporary='true']
  println(ss.catalog.getTable("global_temp", "view2")) // Table[name='view2', database='global_temp', tableType='TEMPORARY', isTemporary='true']

  val ss2 = ss.newSession
  println(ss2.catalog.tableExists("view1")) // false
  println(ss2.catalog.tableExists("global_temp", "view2")) // true

  println(ss.catalog.currentDatabase) // default

  ss.catalog.dropTempView("view1")
  ss.catalog.dropGlobalTempView("view2")
  println(ss.catalog.tableExists("view1")) // false
  // org.apache.spark.sql.catalyst.analysis.NoSuchDatabaseException: Database 'global_temp' not found;
  // println(ss.catalog.tableExists("global_temp", "view2"))
  println(ss.catalog.databaseExists("global_temp")) // false
  println(ss.catalog.databaseExists("default")) // true

  // for createDataFrame please check DataFrame3 or RowTest

  val ds1Seq: Seq[PersonWithAge] = Seq(
    PersonWithAge("Alice", 18),
    PersonWithAge("Bob", 23),
    PersonWithAge("Cathy", 20),
    PersonWithAge("David", 27),
    PersonWithAge("Eva", 22),
    PersonWithAge("Fred", 29)
  )

  val ds1: Dataset[PersonWithAge] = ss createDataset ds1Seq
  ds1.show
  /*
  +-----+---+
  | name|age|
  +-----+---+
  |Alice| 18|
  |  Bob| 23|
  |Cathy| 20|
  |David| 27|
  |  Eva| 22|
  | Fred| 29|
  +-----+---+
   */

  val ds2: Dataset[Int] = Seq(1,2,3).toDS
  ds2.show
  /*
  +-----+
  |value|
  +-----+
  |    1|
  |    2|
  |    3|
  +-----+
   */

  // for createDataFrame please check DataFrame3

  ss.conf.getAll foreach println
  /*
  (spark.driver.host,xxx.xxx.xxx.xxx)
  (spark.driver.port,xxxxx)
  (spark.app.name,xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx)
  (spark.executor.id,driver)
  (spark.master,local)
  (spark.app.id,local-xxxxxxxxxxxxxxxx)
   */

  println(ss.conf.isModifiable("spark.app.name")) // false

  // ss.conf.set("key", "value")
  // ss.conf.unset("key")

  ss.streams.active foreach println
  // (nothing)

  ss.time[Dataset[Long]] {
    ss.range(1,10,2).as[Long]
  }
  // Time taken: 10 ms

  ss.time {
    ss.range(1,10,2).as[Long] map (_ + 1)
  }
  // Time taken: 20 ms

  ss.stop
  ss2.close

}
