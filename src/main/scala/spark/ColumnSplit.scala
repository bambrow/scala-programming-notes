package spark

import org.apache.spark.sql.functions._
import org.apache.spark.sql.{Column, Dataset, Row}

/**
 * This file uses an implicit class to extend the functionality of DataFrame.
 */

object ColumnSplit {

  implicit class ColumnSplitHandler(df: Dataset[Row]) extends Serializable {

    def splitArray(arrayColName: String, colNames: Seq[String]): Dataset[Row] = splitArray(df col arrayColName, colNames)
    def splitArray(arrayCol: Column, colNames: Seq[String]): Dataset[Row] = {
      val remainingColNames: Seq[String] = df.columns filter (_ != arrayCol.toString)
      require(colNames.nonEmpty && (remainingColNames.isEmpty || (remainingColNames intersect colNames).isEmpty))

      val selectExpr: Seq[Column] = remainingColNames map df.col union {
        colNames.zipWithIndex map {
          case (colName: String, index: Int) => arrayCol getItem index as colName
        }
      }

      df select (selectExpr: _*)
    }

    def splitColumn(colName: String, colNames: Seq[String], pattern: String): Dataset[Row] = splitColumn(df col colName, colNames, pattern)
    def splitColumn(col: Column, colNames: Seq[String], pattern: String): Dataset[Row] = {
      lazy val randomColName: String = scala.util.Random.alphanumeric.take(20).mkString
      val df1: Dataset[Row] = df withColumn (randomColName, split(col, pattern)) drop col
      df1 splitArray (df1 col randomColName, colNames)
    }

  }

}
