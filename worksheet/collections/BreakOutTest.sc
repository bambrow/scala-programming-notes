import scala.collection.breakOut

// When possible, use scala.collection.breakOut
// to avoid producing and converting intermediate collections
// with .toMap, .toSeq, .toSet, etc.
val map: Map[String, Int] = {
  List("1", "12", "123").map(x => (x, x.length))(breakOut)
}
// map: Map[String,Int] = Map(1 -> 1, 12 -> 2, 123 -> 3)

// Please note that the type annotation is required
// or breakOut will not be able to infer and use the proper builder.

/*
  def breakOut[From, T, To](implicit b: CanBuildFrom[Nothing, T, To]): CanBuildFrom[From, T, To] =
    new CanBuildFrom[From, T, To] {
      def apply(from: From) = b.apply()
      def apply()           = b.apply()
    }
 */
