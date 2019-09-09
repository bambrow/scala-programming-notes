class C1 { def m = List("C1") }
trait T1 extends C1 { override def m = "T1" :: super.m }
trait T2 extends C1 { override def m = "T2" :: super.m }
trait T3 extends C1 { override def m = "T3" :: super.m }
class C2 extends T2 { override def m = "C2" :: super.m }
class D extends C2 with T1 with T2 with T3 {
  override def m = "D" :: super.m
}

(new D).m
// res0: List[String] = List(D, T3, T1, C2, T2, C1)