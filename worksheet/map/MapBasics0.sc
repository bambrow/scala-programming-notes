val set = Set(1,2,3)

set + 4
set - 3
set ++ List(4, 5)
set -- List(1, 2)
set & Set(1, 3, 5)
// res0: scala.collection.immutable.Set[Int] = Set(1, 2, 3, 4)
// res1: scala.collection.immutable.Set[Int] = Set(1, 2)
// res2: scala.collection.immutable.Set[Int] = HashSet(5, 1, 2, 3, 4)
// res3: scala.collection.immutable.Set[Int] = Set(3)
// res4: scala.collection.immutable.Set[Int] = Set(1, 3)

set.size
set contains 2
// res5: Int = 3
// res6: Boolean = true

val map = Map(1 -> 'a', 2 -> 'b', 3 -> 'c')

map + (4 -> 'd')
map - 2
map ++ List(4 -> 'd', 5 -> 'e')
map -- List(1,2)
// res7: scala.collection.immutable.Map[Int,Char] = Map(1 -> a, 2 -> b, 3 -> c, 4 -> d)
// res8: scala.collection.immutable.Map[Int,Char] = Map(1 -> a, 3 -> c)
// res9: scala.collection.immutable.Map[Int,Char] = HashMap(5 -> e, 1 -> a, 2 -> b, 3 -> c, 4 -> d)
// res10: scala.collection.immutable.Map[Int,Char] = Map(3 -> c)

map.size
map contains 2
map(2)
// res11: Int = 3
// res12: Boolean = true
// res13: Char = b

map.keys.toSeq
map.keySet
map.values.toSeq
map.isEmpty
// res14: Seq[Int] = List(1, 2, 3)
// res15: scala.collection.immutable.Set[Int] = Set(1, 2, 3)
// res16: Seq[Char] = List(a, b, c)
// res17: Boolean = false

import scala.collection.immutable.SortedSet
import scala.collection.immutable.TreeSet

val ts = TreeSet(4,6,2,3,5,1,9,8,7,0)
// ts: scala.collection.immutable.TreeSet[Int] = TreeSet(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)

import scala.collection.immutable.SortedMap
import scala.collection.immutable.TreeMap

val tm = TreeMap(2 -> 'b', 1 -> 'a', 3 -> 'c')
// tm: scala.collection.immutable.TreeMap[Int,Char] = TreeMap(1 -> a, 2 -> b, 3 -> c)
