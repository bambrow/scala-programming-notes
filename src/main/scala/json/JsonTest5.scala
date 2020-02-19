package json

import org.json4s.DefaultFormats
import org.json4s.jackson.Serialization.write

case class PersonWithEmail(name: String, age: Int, email_address: String)

object JsonTest5 extends App {

  implicit val formats: DefaultFormats.type = DefaultFormats

  val json1 = Student("Alice", 18)
  val json2 = Person("Bob", 22, List("java", "scala"))
  val json3 = Person("Bob", 22, List("c++"))

  println(write(json1))
  println(write(json2))
  println(write(json3))
  // {"name":"Alice","age":18}
  // {"name":"Bob","age":22,"tags":["java","scala"]}
  // {"name":"Bob","age":22,"tags":["c++"]}

  val address: Address = Address("Canada", "Toronto")
  val child1: Child = Child("Mary", 5, Some("Toronto"))
  val child2: Child = Child("Alice", 3, None)
  val json4 = Parent("Bob", address, List(child1, child2))
  println(write(json4))
  // {"name":"Bob","address":{"country":"Canada","city":"Toronto"},"children":[{"name":"Mary","age":5,"birthplace":"Toronto"},{"name":"Alice","age":3}]}

  val json5 = PersonWithEmail("Bob", 27, "bob@bob.com")
  println(write(json5))
  // {"name":"Bob","age":27,"email_address":"bob@bob.com"}

  val json6 = PersonWithEmail("Bob", 27, null)
  println(write(json6))
  // {"name":"Bob","age":27,"email_address":null}

  val json7 = Parent("Bob", null, List.empty)
  println(write(json7))
  // {"name":"Bob","address":null,"children":[]}

  val json8 = PersonWithEmail("Bob", 27, """"bob@bob.com"""")
  println(write(json8))
  // {"name":"Bob","age":27,"email_address":"\"bob@bob.com\""}

  val json9 = Parent("Bob", Address(""""China"""", """"Beijing""""), null)
  println(write(json9))
  // {"name":"Bob","address":{"country":"\"China\"","city":"\"Beijing\""},"children":null}

}
