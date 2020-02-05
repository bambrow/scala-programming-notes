package scalatest.basics

import org.scalatest.FunSuite

class ExampleFunSuite extends FunSuite {

  test("assert example") {
    assert(2 == 2)
    assert("hello".startsWith("h") && "world".endsWith("d"))
    assert(1.0.isInstanceOf[Double])
    assert(None.isEmpty)
  }

  test("assertResult example") {
    assertResult(2) {
      3 - 1
    }
    assertResult((1, 2)) {
      List(1).zip(List(2)).head
    }
  }

  test("exceptions example") {
    assertThrows[IndexOutOfBoundsException]{
      "a".charAt(-1)
    }
    val caught = intercept[IndexOutOfBoundsException] {
      "b".charAt(-1)
    }
    println(caught.getMessage)
    assert(caught.getMessage.indexOf("-1") != -1)
  }

  test("compile failure example") {
    assertDoesNotCompile {
      "val a: String = 1"
    }
    assertTypeError {
      "val a: String = 1"
    }
    assertCompiles {
      "val a: Int = 1"
    }
  }

  test("assumptions example") {
    assume(true) // assume(false) will ignore the test
    assert(1 == 1)
  }

  test("withClue example") {
    // assert(1 + 1 === 3, "clue example")
    // assertResult(3, "clue example") { 1 + 1 }
    withClue("clue example") {
      assertThrows[IndexOutOfBoundsException] {
        "a".charAt(-1)
      }
    }
  }

  // use fail to manually fail the test
  // use cancel to manually cancel the test
  // use succeed to end test body with Assertion type in async style tests

}
