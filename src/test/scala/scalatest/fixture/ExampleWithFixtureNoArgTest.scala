package scalatest.fixture

import org.scalatest.{Failed, FlatSpec, Outcome}

class ExampleWithFixtureNoArgTest extends FlatSpec {

  override def withFixture(test: NoArgTest): Outcome = {
    // invoke the test function
    super.withFixture(test) match {
      case failed: Failed => println("failed!"); failed
      case other => println("not failed!"); other
    }
  }

  "example 1" should "succeed" in {
    assert(1 + 1 === 2)
  }

  /*
  "example 2" should "fail" in {
    assert(1 + 1 === 3)
  }
   */

}
