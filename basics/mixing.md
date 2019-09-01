```scala
scala> class C1 { def m = List("C1") }
defined class C1

scala> trait T1 extends C1 { override def m = "T1" :: super.m }
defined trait T1

scala> trait T2 extends C1 { override def m = "T2" :: super.m }
defined trait T2

scala> trait T3 extends C1 { override def m = "T3" :: super.m }
defined trait T3

scala> class C2 extends T2 { override def m = "C2" :: super.m }
defined class C2

scala> class D extends C2 with T1 with T2 with T3 {
     |   override def m = "D" :: super.m
     | }
defined class D

scala> new D m
res24: List[String] = List(D, T3, T1, C2, T2, C1)
```
