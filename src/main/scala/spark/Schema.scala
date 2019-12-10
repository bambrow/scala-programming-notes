package spark

import org.apache.spark.sql.Row
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types.{DataType, LongType, StructType}

case class PersonWithAge(name: String, age: Int)
case class PersonWithSalary(name: String, salary: Int)
case class Person(name: String, age: Int, salary: Int)
case class Grade(name: String, grade: Int)
case class Student(name: String, grades: Seq[Int])

case class PersonWithAgeOption(name: String, age: Option[Int])
case class PersonWithSalaryOption(name: String, salary: Option[Int])
case class PersonOption(name: String, age: Option[Int], salary: Option[Int])

class CountUDAF extends UserDefinedAggregateFunction {
  override def inputSchema: StructType = {
    new StructType().add("id", LongType, nullable = true)
  }

  override def bufferSchema: StructType = {
    new StructType().add("count", LongType, nullable = true)
  }

  override def dataType: DataType = LongType

  override def deterministic: Boolean = true

  override def initialize(buffer: MutableAggregationBuffer): Unit = {
    println(s"--- initialize(buffer: $buffer)")
    buffer(0) = 0L
  }

  override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
    println(s"--- update(buffer: $buffer, input: $input)")
    buffer(0) = buffer.getLong(0) + 1
  }

  override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
    println(s"--- merge(buffer1: $buffer1, buffer2: $buffer2)")
    buffer1(0) = buffer1.getLong(0) + buffer2.getLong(0)
  }

  override def evaluate(buffer: Row): Any = {
    println(s"--- evaluate(buffer: $buffer)")
    buffer.getLong(0)
  }
}
