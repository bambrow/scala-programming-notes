package json

import org.json4s._
import org.json4s.jackson.JsonMethods._

import scala.collection.mutable.ListBuffer

object JsonTest1 extends App {

  /*
  sealed abstract class JValue
  case object JNothing extends JValue // 'zero' for JValue
  case object JNull extends JValue
  case class JString(s: String) extends JValue
  case class JDouble(num: Double) extends JValue
  case class JDecimal(num: BigDecimal) extends JValue
  case class JInt(num: BigInt) extends JValue
  case class JLong(num: Long) extends JValue
  case class JBool(value: Boolean) extends JValue
  case class JObject(obj: List[JField]) extends JValue
  case class JArray(arr: List[JValue]) extends JValue

  type JField = (String, JValue)
   */

  val jsonColors = """{
                     |  "colors": [
                     |    {
                     |      "color": "black",
                     |      "category": "hue",
                     |      "type": "primary",
                     |      "code": {
                     |        "rgba": [255,255,255,1],
                     |        "hex": "#000"
                     |      }
                     |    },
                     |    {
                     |      "color": "white",
                     |      "category": "value",
                     |      "code": {
                     |        "rgba": [0,0,0,1],
                     |        "hex": "#FFF"
                     |      }
                     |    },
                     |    {
                     |      "color": "red",
                     |      "category": "hue",
                     |      "type": "primary",
                     |      "code": {
                     |        "rgba": [255,0,0,1],
                     |        "hex": "#FF0"
                     |      }
                     |    },
                     |    {
                     |      "color": "blue",
                     |      "category": "hue",
                     |      "type": "primary",
                     |      "code": {
                     |        "rgba": [0,0,255,1],
                     |        "hex": "#00F"
                     |      }
                     |    },
                     |    {
                     |      "color": "yellow",
                     |      "category": "hue",
                     |      "type": "primary",
                     |      "code": {
                     |        "rgba": [255,255,0,1],
                     |        "hex": "#FF0"
                     |      }
                     |    },
                     |    {
                     |      "color": "green",
                     |      "category": "hue",
                     |      "type": "secondary",
                     |      "code": {
                     |        "rgba": [0,255,0,1],
                     |        "hex": "#0F0"
                     |      }
                     |    }
                     |  ]
                     |}""".stripMargin

  val json = parse(jsonColors)
  val colors = json \ "colors"

  val colorNames = ListBuffer[String]()

  colors match {
    case JArray(cs) => cs foreach {
      case JObject(xs) => xs foreach {
        case ("color", JString(c)) => colorNames += c
        case _ =>
      }
    }
    case _ =>
  }

  println(colorNames.toList) // List(black, white, red, blue, yellow, green)

  val colorNames2 = colors \\ "color" match {
    case JObject(cs) => cs map {
      case (_, JString(c)) => c
    }
  }

  println(colorNames2) // List(black, white, red, blue, yellow, green)

  val rgba = json \\ "rgba" match {
    case JObject(rs) => rs map {
      case (_, JArray(xs)) => xs map {
        case JInt(x) => x
      }
    }
  }

  println(rgba) // List(List(255, 255, 255, 1), List(0, 0, 0, 1), List(255, 0, 0, 1), List(0, 0, 255, 1), List(255, 255, 0, 1), List(0, 255, 0, 1))

}
