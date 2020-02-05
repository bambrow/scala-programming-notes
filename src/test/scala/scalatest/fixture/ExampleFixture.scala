package scalatest.fixture

import org.scalatest.FlatSpec

import scala.collection.mutable.ListBuffer

class ExampleFixture extends FlatSpec {

  def fixture = {
    new {
      val builder: StringBuilder = new StringBuilder("hello ")
      val buffer: ListBuffer[String] = ListBuffer.empty
    }
  }

  trait Builder {
    val builder: StringBuilder = new StringBuilder("hello ")
  }

  trait Buffer {
    val buffer: ListBuffer[String] = ListBuffer.empty
  }

  "Testing fixture" should "work the first time" in {
    val f = fixture
    f.builder.append("world!")
    f.buffer += "abc"
    assert(f.builder.toString === "hello world!")
    assert(f.buffer.head === "abc")
  }

  it should "work the second time" in {
    val f = fixture
    f.builder.append("Scala!")
    assert(f.builder.toString === "hello Scala!")
    assert(f.buffer.isEmpty)
  }

  // this test uses trait Builder
  it should "test builder only" in new Builder {
    builder.append("builder!")
    assert(builder.toString === "hello builder!")
  }

  // this test uses trait Buffer
  it should "test buffer only" in new Buffer {
    buffer += "abc"
    assert(buffer.head === "abc")
  }

  // this test uses both trait Builder and trait Buffer
  it should "test both builder and buffer using traits" in new Builder with Buffer {
    builder.append("everyone!")
    buffer += "nice"
    assert(builder.toString === "hello everyone!")
    assert(buffer.head === "nice")
  }

}
