import org.apache.spark.sql.{Column, Dataset, Row}
import org.apache.spark.sql.functions._

object ColumnSplit {

  implicit class ColumnSplitHandler(df: Dataset[Row]) extends Serializable {

    def splitArray(arrayColName: String, colNames: Seq[String]): Dataset[Row] = splitArray(df col arrayColName, colNames)
    def splitArray(arrayCol: Column, colNames: Seq[String]): Dataset[Row] = {
      val remainingColNames: Seq[String] = df.columns filter (_ != arrayCol.toString)
      require(colNames.nonEmpty && (remainingColNames.isEmpty || (remainingColNames intersect colNames).isEmpty))

      var df2: Dataset[Row] = df
      colNames.zipWithIndex foreach {
        case (colName: String, index: Int) => {
          df2 = df2 withColumn (colName, arrayCol getItem index)
        }
      }

      df2 drop arrayCol
    }

    def splitColumn(colName: String, colNames: Seq[String], pattern: String): Dataset[Row] = splitColumn(df col colName, colNames, pattern)
    def splitColumn(col: Column, colNames: Seq[String], pattern: String): Dataset[Row] = {
      lazy val randomColName: String = scala.util.Random.alphanumeric.take(20).mkString
      val df1: Dataset[Row] = df withColumn (randomColName, split(col, pattern)) drop col
      df1 splitArray (df1 col randomColName, colNames)
    }

  }

}
