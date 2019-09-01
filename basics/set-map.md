```scala
scala> var set = Set(1,2,3)
set: scala.collection.immutable.Set[Int] = Set(1, 2, 3)

scala> set + 4
res0: scala.collection.immutable.Set[Int] = Set(1, 2, 3, 4)

scala> set - 3
res1: scala.collection.immutable.Set[Int] = Set(1, 2)

scala> set ++ List(4, 5)
res2: scala.collection.immutable.Set[Int] = Set(5, 1, 2, 3, 4)

scala> set -- List(1, 2)
res3: scala.collection.immutable.Set[Int] = Set(3)

scala> set & Set(1, 3, 5)
res5: scala.collection.immutable.Set[Int] = Set(1, 3)

scala> set.size
res6: Int = 3

scala> set contains 2
res7: Boolean = true

scala> val map = Map(1 -> 'a', 2 -> 'b', 3 -> 'c')
map: scala.collection.immutable.Map[Int,Char] = Map(1 -> a, 2 -> b, 3 -> c)

scala> map + (4 -> 'd')
res9: scala.collection.immutable.Map[Int,Char] = Map(1 -> a, 2 -> b, 3 -> c, 4 -> d)

scala> map - 2
res10: scala.collection.immutable.Map[Int,Char] = Map(1 -> a, 3 -> c)

scala> map ++ List(4 -> 'd', 5 -> 'e')
res11: scala.collection.immutable.Map[Int,Char] = Map(5 -> e, 1 -> a, 2 -> b, 3 -> c, 4 -> d)

scala> map -- List(1,2)
res12: scala.collection.immutable.Map[Int,Char] = Map(3 -> c)

scala> map.size
res13: Int = 3

scala> map contains 2
res14: Boolean = true

scala> map(2)
res15: Char = b

scala> import scala.language.postfixOps
import scala.language.postfixOps

scala> map keys
res17: Iterable[Int] = Set(1, 2, 3)

scala> map keySet
res18: scala.collection.immutable.Set[Int] = Set(1, 2, 3)

scala> map values
res19: Iterable[Char] = MapLike.DefaultValuesIterable(a, b, c)

scala> map isEmpty
res20: Boolean = false

scala> import scala.collection.immutable.SortedSet
import scala.collection.immutable.SortedSet

scala> import scala.collection.immutable.TreeSet
import scala.collection.immutable.TreeSet

scala> val ts = TreeSet(4,6,2,3,5,1,9,8,7,0)
ts: scala.collection.immutable.TreeSet[Int] = TreeSet(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)

scala> import scala.collection.immutable.SortedMap
import scala.collection.immutable.SortedMap

scala> import scala.collection.immutable.TreeMap
import scala.collection.immutable.TreeMap

scala> var tm = TreeMap(2 -> 'b', 1 -> 'a', 3 -> 'c')
tm: scala.collection.immutable.TreeMap[Int,Char] = Map(1 -> a, 2 -> b, 3 -> c)
```
