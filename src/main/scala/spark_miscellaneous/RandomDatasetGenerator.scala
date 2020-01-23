package spark_miscellaneous

import org.apache.spark.sql.{Dataset, SparkSession}

import scala.util.Random

class RandomDatasetGenerator(implicit ss: SparkSession) extends Serializable {

  import ss.implicits._
  private val r: Random = new Random

  def getIntDS(n: Int): Dataset[Int] = {
    val seq: Seq[Int] = {
      for (_ <- 0 until n) yield r.nextInt
    }
    ss createDataset seq
  }

  def getLongDS(n: Int): Dataset[Long] = {
    val seq: Seq[Long] = {
      for (_ <- 0 until n) yield r.nextLong
    }
    ss createDataset seq
  }

  def getDoubleDS(n: Int): Dataset[Double] = {
    val seq: Seq[Double] = {
      for (_ <- 0 until n) yield r.nextDouble
    }
    ss createDataset seq
  }

  def getStringDS(n: Int, k: Int): Dataset[String] = {
    val seq: Seq[String] = {
      for (_ <- 0 until n) yield r.nextString(k)
    }
    ss createDataset seq
  }

}
