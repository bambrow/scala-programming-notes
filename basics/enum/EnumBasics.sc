object Color extends Enumeration {
  val Red, Green, Blue = Value
}

Color.Red.id
Color(0)
// res0: Int = 0
// res1: Color.Value = Red

object Direction extends Enumeration {
  val Up = Value("go up")
  val Down = Value("go down")
}

Direction.Up.id
Direction(1)
// res2: Int = 0
// res3: Direction.Value = go down