package mylist

import scala.annotation.tailrec

object MyList {

  sealed trait List[+A]
  case object Nil extends List[Nothing]
  case class Cons[+A](head: A, tail: List[A]) extends List[A]

  object List {
    def apply[A](as: A*): List[A] = {
      if (as.isEmpty) Nil
      else Cons(as.head, apply(as.tail: _*))
    }

    def head[A](as: List[A]): A = as match {
      case Nil => sys.error("empty list")
      case Cons(x, _) => x
    }

    def tail[A](as: List[A]): List[A] = as match {
      case Nil => sys.error("empty list")
      case Cons(_, xs) => xs
    }

    def drop[A](as: List[A], n: Int): List[A] = {
      if (n <= 0) as
      else as match {
        case Nil => Nil
        case Cons(_, xs) => drop(xs, n-1)
      }
    }

    def dropWhile[A](as: List[A])(f: A => Boolean): List[A] = as match {
      // case Cons(x, xs) => if(f(x)) dropWhile(xs)(f) else Cons(x, dropWhile(xs)(f))
      case Cons(x, xs) if f(x) => dropWhile(xs)(f)
      case _ => as
    }

    /*
    def foldRight[A, B](as: List[A], z: B)(f: (A, B) => B): B = as match {
      case Nil => z
      case Cons(x, xs) => f(x, foldRight(xs, z)(f))
    }
    */

    @tailrec
    def foldLeft[A, B](as: List[A], z: B)(f: (B, A) => B): B = as match {
      case Nil => z
      case Cons(x, xs) => foldLeft(xs, f(z, x))(f)
    }

    // def foldRight[A, B](as: List[A], z: B)(f: (A, B) => B): B = foldLeft(reverse(as), z)((b, a) => f(a, b))

    def foldRight[A, B](as: List[A], z: B)(f: (A, B) => B): B = {
      foldLeft(as, (b: B) => b)((g, a) => b => g(f(a, b)))(z)
    }

    /*
    def foldLeft[A, B](as: List[A], z: B)(f: (B, A) => B): B = {
      foldRight(as, (b: B) =>b)((a, g) => b => g(f(b, a)))(z)
    }
    */

    def sum(ints: List[Int]): Int = foldLeft(ints, 0)(_ + _)

    def product(ds: List[Double]): Double = foldLeft(ds, 1.0)(_ * _)

    def length[A](as: List[A]): Int = foldLeft(as, 0)((z, _) => z + 1)

    def reverse[A](as: List[A]): List[A] = foldLeft(as, List[A]())((z, x) => Cons(x, z))

    def append[A](as: List[A], bs: List[A]): List[A] = foldRight(as, bs)(Cons(_, _))

    def concat[A](ls: List[List[A]]): List[A] = foldRight(ls, Nil: List[A])(append)

    def increment(ints: List[Int]): List[Int] = foldRight(ints, Nil: List[Int])((x, z) => Cons(x + 1, z))

    def makeStringList[A](as: List[A]): List[String] = foldRight(as, Nil: List[String])((x, z) => Cons(x.toString, z))

    def map[A, B](as: List[A])(f: A => B): List[B] = foldRight(as, Nil: List[B])((x, z) => Cons(f(x), z))

    def filter[A](as: List[A])(f: A => Boolean): List[A] = foldRight(as, Nil: List[A])((x, z) => if (f(x)) Cons(x, z) else z)

    def flatMap[A, B](as: List[A])(f: A => List[B]): List[B] = concat(map(as)(f))

    // def filter[A](as: List[A])(f: A => Boolean): List[A] = flatMap(as)(x => if (f(x)) List(x) else Nil)

    def zip[A, B ,C](as: List[A], bs: List[B])(f: (A, B) => C): List[C] = (as, bs) match {
      case (Nil, _) => Nil
      case (_, Nil) => Nil
      case (Cons(x, xs), Cons(y, ys)) => Cons(f(x, y), zip(xs, ys)(f))
    }

    @tailrec
    def startsWith[A](as: List[A], prefix: List[A]): Boolean = (as, prefix) match {
      case (_, Nil) => true
      case (Cons(x, xs), Cons(y, ys)) if x == y => startsWith(xs, ys)
      case _ => false
    }

    @tailrec
    def containsSequence[A](as: List[A], sub: List[A]): Boolean = as match {
      case Nil => sub == Nil
      case _ if startsWith(as, sub) => true
      case Cons(_, xs) => containsSequence(xs, sub)
    }

    def test() = {
      println(drop(List(1,2,3,4,5), 3))
      println(dropWhile(List(1,2,3,4,5))(x => x < 3))
      println(sum(List(1,2,3,4,5)))
      println(product(List(1.0,1.2,1.4,1.6)))
      println(reverse(List(1,2,3,4,5)))
      println(append(List(1,2), List(4,5)))
      println(concat(List(List(2,1), List(1,2,3), List(5,4), List(4,5))))
      println(increment(List(1,2,3,4,5)))
      println(map(List(1,2,3,4,5))(x => x * 2))
      println(filter(List(1,2,3,4,5))(x => x % 2 == 0))
      println(flatMap(List(1,2,3))(x => List(x,x,x)))
      println(zip(List(2,2,2), List(3,3,3))(_ + _))
      println(containsSequence(List(1,2,3,4,5), List(3,4,5)))
    }
  }

  def main(args: Array[String]): Unit = {
    List.test
  }

}
