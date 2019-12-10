package json

import org.json4s.JsonDSL._
import org.json4s._
import org.json4s.jackson.JsonMethods._

case class Student(name: String, age: Int)
case class Person(name: String, age: Int, tags: List[String])

object JsonTest2 extends App {

  val json0 = "hello"
  println(compact(render(json0))) // "hello"

  val json1 = List(1,2,3,4,5)
  println(compact(render(json1))) // [1,2,3,4,5]

  val json2 = "name" -> "Alice"
  println(compact(render(json2)))

  val json3 = ("name" -> "Alice") ~ ("age" -> 18)
  println(compact(render(json3))) // {"name":"Alice"}

  val json4 = ("name" -> "Alice") ~ ("age" -> Some(18))
  println(compact(render(json4))) // {"name":"Alice","age":18}

  val json5 = ("name" -> "Alice") ~ ("age" -> (None: Option[Int]))
  println(compact(render(json5))) // {"name":"Alice"}

  implicit def studentDSLConversion(s: Student): JValue = {
    JObject(List(("name", JString(s.name)), ("age", JInt(s.age))))
  }

  val json6 = Student("Alice", 18)
  println(compact(render(json6))) // {"name":"Alice","age":18}

  val json7 = Person("Bob", 22, List("java", "scala"))
  val json8 = Person("Bob", 22, List("c++"))

  implicit def personDSLConversion(p: Person): JValue = {
    JObject(List(("name", JString(p.name)), ("age", JInt(p.age)), ("tags", JArray(p.tags map (JString(_))))))
  }

  println(compact(json7)) // {"name":"Bob","age":22,"tags":["java","scala"]}
  println(render(json7) merge render(json8)) // JObject(List((name,JString(Bob)), (age,JInt(22)), (tags,JArray(List(JString(java), JString(scala), JString(c++))))))

  val json9 = render(json7) merge render(json8)
  println(compact(json9)) // {"name":"Bob","age":22,"tags":["java","scala","c++"]}

  val Diff(changed1, added1, deleted1) = json9 diff render(json7)
  println(compact(changed1)) //
  println(compact(added1)) //
  println(compact(deleted1)) // {"tags":["c++"]}

  val Diff(changed2, added2, deleted2) = json9 diff render(json8)
  println(compact(changed2)) // {"tags":"c++"}
  println(compact(added2)) //
  println(compact(deleted2)) // {"tags":["scala","c++"]}

  val Diff(changed3, added3, deleted3) = render(json7) diff render(json8)
  println(compact(changed3)) // {"tags":"c++"}
  println(compact(added3)) //
  println(compact(deleted3)) // {"tags":["scala"]}

  val json10 = """ { "tags" : ["javascript"] } """
  println(compact(json9 merge parse(json10))) // {"name":"Bob","age":22,"tags":["java","scala","c++","javascript"]}

}