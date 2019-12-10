package examples.myoption

object MyOption {

  sealed trait Option[+A] {

    def map[B](f: A => B): Option[B] = this match {
      case None => None
      case Some(x) => Some(f(x))
    }

    def flatMap[B](f: A => Option[B]): Option[B] = map(f) getOrElse None

    /*
    def flatMap[B](f: A => Option[B]): Option[B] = this match {
      case None => None
      case Some(x) => f(x)
    }
    */

    def getOrElse[B >: A](default: => B): B = this match {
      case None => default
      case Some(x) => x
    }

    def orElse[B >: A](ob: => Option[B]): Option[B] = this map (Some(_)) getOrElse ob

    /*
    def orElse[B >: A](ob: => Option[B]): Option[B] = this match {
      case None => ob
      case _ => this
    }
    */

    def filter(f: A => Boolean): Option[A] = flatMap(x => if (f(x)) Some(x) else None)

    /*
    def filter(f: A => Boolean): Option[A] = this match {
      case Some(x) if f(x) => this
      case _ => None
    }
    */

  }

  case class Some[+A](get: A) extends Option[A]
  case object None extends Option[Nothing]

}
