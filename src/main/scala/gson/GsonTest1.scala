package gson

import com.google.gson.Gson

import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer

object GsonTest1 extends App {

  val gson: Gson = new Gson()

  val p1: Person = Person("Alice", 18, "F", "alice@alice.com")

  println(gson.toJson(p1))
  // {"name":"Alice","age":18,"gender":"F","email":"alice@alice.com"}

  val p2: Person = gson.fromJson("{\"name\":\"Bob\",\"age\":23,\"gender\":\"M\",\"email\":\"bob@bob.com\"}", classOf[Person])

  println(p2)
  // Person(Bob,23,M,bob@bob.com)

  val prof: Person = Person("David", 27, "M", "david@david.com")
  val s1: Person = Person("Zach", 26, "M", "zach@zach.com")
  val s2: Person = Person("Cathy", 22, "F", "cathy@cathy.com")

  val c1: Classroom = Classroom(prof, ListBuffer(s1, s2))
  println(gson.toJson(c1))
  // {"professor":{"name":"David","age":27,"gender":"M","email":"david@david.com"},"students":[{"name":"Zach","age":26,"gender":"M","email":"zach@zach.com"},{"name":"Cathy","age":22,"gender":"F","email":"cathy@cathy.com"}]}

}
