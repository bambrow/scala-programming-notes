package json

import org.json4s._
import org.json4s.jackson.JsonMethods._

object JsonTest3 extends App {

  /*
  def map(f: JValue => JValue): JValue
  def mapField(f: JField => JField): JValue
  def transform(f: PartialFunction[JValue, JValue]): JValue
  def transformField(f: PartialFunction[JField, JField]): JValue
  def find(p: JValue => Boolean): Option[JValue]
  def findField(p: JField => Boolean): Option[JField]
   */

  val jsonString = """{ "name": "Bob",
                     |  "children": [
                     |  {
                     |    "name": "Mary",
                     |    "age": 5
                     |  },
                     |  {
                     |    "name": "Alice",
                     |    "age": 3
                     |  }
                     |  ]
                     |}""".stripMargin

  val json = parse(jsonString)

  val ages = for {
    JObject(child) <- json
    JField("age", JInt(age)) <- child
  } yield age

  println(ages) // List(5, 3)

  val namesAndAges = for {
    JObject(child) <- json
    JField("name", JString(name)) <- child
    JField("age", JInt(age)) <- child
    if age > 4
  } yield (name, age)

  println(namesAndAges) // List((Mary,5))

  println(compact(json)) // {"name":"Bob","children":[{"name":"Mary","age":5},{"name":"Alice","age":3}]}

  val additionalJson = parse(""" { "children": [ { "name": "Eva" } ] } """)

  println(compact(json merge additionalJson)) // {"name":"Bob","children":[{"name":"Mary","age":5},{"name":"Alice","age":3},{"name":"Eva"}]}

  val json1 = json ++ additionalJson
  println(compact(render(json1))) // [{"name":"Bob","children":[{"name":"Mary","age":5},{"name":"Alice","age":3}]},{"children":[{"name":"Eva"}]}]

  println(compact(render(json \ "children"))) // [{"name":"Mary","age":5},{"name":"Alice","age":3}]
  println(compact(render(json \ "children" \ "name"))) // ["Mary","Alice"]
  println(compact(render(json \\ "name"))) // {"name":"Bob","name":"Mary","name":"Alice"}

  val json2 = json removeField {
    _ == JField("name", JString("Alice"))
  }
  println(compact(render(json2))) // {"name":"Bob","children":[{"name":"Mary","age":5},{"age":3}]}

  val json3 = json findField {
    case JField("name", _) => true
    case _ => false
  } match {
    case Some(x) => JObject(x)
    case _ => JNothing
  }
  println(compact(render(json3))) // {"name":"Bob"}

  val json4 = JArray(
    json filterField {
      case JField("name", _) => true
      case _ => false
    } map (JObject(_))
  )
  println(compact(render(json4))) // [{"name":"Bob"},{"name":"Mary"},{"name":"Alice"}]

  val json5 = json transformField {
    case JField("age", JInt(age)) => ("age", JInt(age + 1))
  }
  println(compact(render(json5))) // {"name":"Bob","children":[{"name":"Mary","age":6},{"name":"Alice","age":4}]}

  println(json.values) // Map(name -> Bob, children -> List(Map(name -> Mary, age -> 5), Map(name -> Alice, age -> 3)))

  println((json \ "children")(0)) // JObject(List((name,JString(Mary)), (age,JInt(5))))
  println((json \ "children")(1)) // JObject(List((name,JString(Alice)), (age,JInt(3))))
  println(json \\ classOf[JInt]) // List(5, 3)
  println(json \\ classOf[JString]) // List(Bob, Mary, Alice)

}
