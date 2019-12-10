package json

import org.json4s._
import org.json4s.jackson.JsonMethods._

case class Address(country: String, city: String)
case class Child(name: String, age: Int, birthplace: Option[String])
case class Parent(name: String, address: Address, children: List[Child])

case class Bike(brand: String, price: Int) {
  def this(price: Int) = this("random-brand", price)
}

case class PersonWithAddress(name: String, addresses: Map[String, Address])

object JsonTest4 extends App {

  val jsonString1 = """ { "name": "Bob",
                       |  "address": {
                       |    "country": "Canada",
                       |    "city": "Toronto"
                       |  },
                       |  "children": [
                       |  {
                       |    "name": "Mary",
                       |    "age": 5,
                       |    "birthplace": "Toronto"
                       |  },
                       |  {
                       |    "name": "Alice",
                       |    "age": 3
                       |  }
                       |  ]
                       |} """.stripMargin

  val jsonString2 = """ { "first-name": "Bob",
                       |  "address": {
                       |    "country-name": "Canada",
                       |    "city-name": "Toronto"
                       |  },
                       |  "children": [
                       |  {
                       |    "first-name": "Mary",
                       |    "age": 5,
                       |    "birth_place": "Toronto"
                       |  },
                       |  {
                       |    "first-name": "Alice",
                       |    "age": 3
                       |  }
                       |  ]
                       |} """.stripMargin

  implicit val formats: DefaultFormats.type = DefaultFormats

  val json1 = parse(jsonString1)
  val p1 = json1.extract[Parent]
  println(p1) // Parent(Bob,Address(Canada,Toronto),List(Child(Mary,5,Some(Toronto)), Child(Alice,3,None)))

  val a1 = (json1 \ "address").extract[Address]
  println(a1) // Address(Canada,Toronto)

  val c1 = (json1 \ "children").extract[List[Child]]
  println(c1) // List(Child(Mary,5,Some(Toronto)), Child(Alice,3,None))

  val json2 = parse(jsonString2) transformField {
    case ("first-name", x) => ("name", x)
    case ("country-name", x) => ("country", x)
    case ("city-name", x) => ("city", x)
  }
  val p2 = json2.camelizeKeys.extract[Parent]
  println(p2) // Parent(Bob,Address(Canada,Toronto),List(Child(Mary,5,None), Child(Alice,3,None)))

  val bike = parse(""" { "price": 100 } """).extract[Bike]
  println(bike) // Bike(random-brand,100)

  val jsonString3 = """ { "name": "Bob",
                       |  "addresses": {
                       |    "home": {
                       |      "country": "Canada",
                       |      "city": "Toronto"
                       |    },
                       |    "work": {
                       |      "country": "US",
                       |      "city": "Boston"
                       |    }
                       |  }
                       |} """.stripMargin

  val json3 = parse(jsonString3)
  val p3 = json3.extract[PersonWithAddress]
  println(p3) // PersonWithAddress(Bob,Map(home -> Address(Canada,Toronto), work -> Address(US,Boston)))

}
