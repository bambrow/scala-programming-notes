package currying


object Currying {

  // the following two are equivalent
  def partial1[A, B, C](a: A)(f: (A, B) => C): B => C = (b: B) => f(a, b)
  def partial2[A, B, C](a: A)(f: (A, B) => C): B => C = b => f(a, b)

  // the following two are equivalent
  def curry1[A, B, C](f: (A, B) => C): A => (B => C) = (a: A) => ((b: B) => f(a, b))
  def curry2[A, B, C](f: (A, B) => C): A => B => C = a => b => f(a, b)

  // the following two are equivalent
  def uncurry1[A, B, C](f: A => (B => C)): (A, B) => C = (a: A, b: B) => f(a)(b)
  def uncurry2[A, B, C](f: A => B => C): (A, B) => C = (a, b) => f(a)(b)

  def compose[A, B, C](f: A => B)(g: B => C): A => C = (a: A) => g(f(a))

}
