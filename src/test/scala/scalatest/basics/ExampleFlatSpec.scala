package scalatest.basics

import org.scalatest._

import scala.collection.mutable.Stack

class ExampleFlatSpec extends FlatSpec with Matchers {

  "A stack" should "pop values in LIFO order" in {
    val stack: Stack[Int] = new Stack[Int]
    stack.push(1)
    stack.push(2)
    assert(stack.pop() === 2)
    stack.pop() should be (1)
  }

  it should "throw exception if empty when popping" in {
    val stack: Stack[Int] = new Stack[Int]
    assertThrows[NoSuchElementException](stack.pop())
  }

  // this will be ignored
  ignore should "ignore: throw exception if empty when popping" in {
    val stack: Stack[Int] = new Stack[Int]
    intercept[NoSuchElementException] {
      stack.pop()
    }
  }

}
