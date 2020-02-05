package scalatest.basics

import org.scalatest.tagobjects.Slow
import org.scalatest.{FlatSpec, Tag}

object MyTag extends Tag("bambrow.MyTag")

class ExampleTag extends FlatSpec {

  "Scala" must "add correctly" taggedAs Slow in {
    Thread.sleep(2000)
    assert(1 + 1 === 2)
  }

  "Scala" must "multiply correctly" taggedAs (Slow, MyTag) in {
    Thread.sleep(1000)
    assert(2 * 4 === 8)
  }

  // for how to use tags
  // see http://www.scalatest.org/user_guide/using_the_runner#filtering
  // and http://www.scalatest.org/user_guide/using_the_scalatest_ant_task#filtering

}
