```scala
scala> abstract class Expr
defined class Expr

scala> case class Var(name: String) extends Expr
defined class Var

scala> case class Number(num: Double) extends Expr
defined class Number

scala> case class UnOp(operator: String, arg: Expr) extends Expr
defined class UnOp

scala> case class BinOp(operator: String, left: Expr, right: Expr) extends Expr
defined class BinOp

scala> def wildcardPattern(x: Expr) = x match {
     |   case BinOp(_, _, _) => println("expr")
     |   case _ => println("something else")
     | }
wildcardPattern: (x: Expr)Unit

scala> def constantPattern(x: Any) = x match {
     |   case 1 => "one"
     |   case true => "true"
     |   case "hello" | "welcome" => "hi"
     |   case i: Int => "int"
     |   case Nil => "Nil"
     |   case _ => "something else"
     | }
constantPattern: (x: Any)String

scala> def variablePattern(x: Any) = x match {
     |   case 0 => "zero"
     |   case <tag>{ t }</tag> => t
     |   case somethingElse => "something else"
     | }
variablePattern: (x: Any)Object

scala> def sequencePattern(x: List[Int]) = x match {
     |   case List(0, _*) => "found it"
     |   case _ =>
     | }
sequencePattern: (x: List[Int])Any

scala> def tuplePattern(x: Any) = x match {
     |   case (a, b, c) => println("matched")
     |   case _ =>
     | }
tuplePattern: (x: Any)Unit

scala> def typedPattern(x: Any) = x match {
     |   case s: String => s.length
     |   case m: Map[_, _] => m.size
     |   case _ => -1
     | }
typedPattern: (x: Any)Int

scala> def variableBindingPattern(x: Expr) = x match {
     |   case o @ BinOp(_, _, _) => o
     |   case o @ Number(_) => -1
     |   case _ => new AnyRef
     | }
variableBindingPattern: (x: Expr)Any
```
