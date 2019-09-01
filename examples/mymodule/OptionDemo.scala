package mymodule

object OptionDemo {

  def mean(as: Seq[Double]): Option[Double] = if (as.isEmpty) None else Some(as.sum / as.length)

  def variance(as: Seq[Double]): Option[Double] = {
    mean(as) flatMap (m => mean(as.map(x => math.pow(x - m, 2))))
  }

  def lift[A, B](f: A => B): Option[A] => Option[B] = _ map f

  val absOption = lift(math.abs)

  /*
  def map2[A, B, C](a: Option[A], b: Option[B])(f: (A, B) => C): Option[C] = {
    a flatMap (aa => b map (bb => f(aa, bb)))
  }
  */

  def map2[A, B, C](a: Option[A], b: Option[B])(f: (A, B) => C): Option[C] = {
    for {
      aa <- a
      bb <- b
    } yield f(aa, bb)
  }

  def sequence[A](as: List[Option[A]]): Option[List[A]] = as match {
    case Nil => Some(Nil)
    case x :: xs => x flatMap (xx => sequence(xs) map (xx :: _))
  }

  /*
  def sequence[A](as: List[Option[A]]): Option[List[A]] = {
    as.foldRight[Option[List[A]]](Some(Nil))((x, y) => map2(x, y)(_ :: _))
  }
  */

  def traverse[A, B](as: List[A])(f: A => Option[B]): Option[List[B]] = as match {
    case Nil => Some(Nil)
    case x :: xs => map2(f(x), traverse(xs)(f))(_ :: _)
  }

  /*
  def traverse[A, B](as: List[A])(f: A => Option[B]): Option[List[B]] = {
    as.foldRight[Option[List[B]]](Some(Nil))((x, y) => map2(f(x), y)(_ :: _))
  }
  */

  // def sequence[A](as: List[Option[A]]): Option[List[A]] = traverse(as)(x => x)

}
