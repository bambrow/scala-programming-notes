```scala
scala> object Color extends Enumeration {
     |   val Red, Green, Blue = Value
     | }
defined object Color

scala> Color.Red.id
res21: Int = 0

scala> Color(0)
res23: Color.Value = Red

scala> object Direction extends Enumeration {
     |   val Up = Value("go up")
     |   val Down = Value("go down")
     | }
defined object Direction

scala> Direction.Up.id
res25: Int = 0

scala> Direction(1)
res26: Direction.Value = go down
```
