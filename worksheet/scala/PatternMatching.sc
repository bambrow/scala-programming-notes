abstract class Expr
case class Var(name: String) extends Expr
case class Number(num: Double) extends Expr
case class UnOp(operator: String, arg: Expr) extends Expr
case class BinOp(operator: String, left: Expr, right: Expr) extends Expr

def wildCardPattern(x: Expr): Unit = x match {
  case BinOp(_, _, _) => println("expr")
  case _ => println("something else")
}

def constantPattern(x: Any): String = x match {
  case 1 => "one"
  case true => "true"
  case "hello" | "welcome" => "hi"
  case _: Int => "int"
  case Nil => "nil"
  case _ => "something else"
}

def sequencePattern(x: List[Int]): String = x match {
  case List(0, _*) => "found it"
  case _ => "nothing"
}

def tuplePattern(x: Any): Unit = x match {
  case (_,_,_) => println("matched")
  case _ => println("nothing")
}

def typedPattern(x: Any): Int = x match {
  case s: String => s.length
  case m: Map[_,_] => m.size
  case _ => -1
}

def variableBindingPattern(x: Expr): Expr = x match {
  case o @ BinOp(_,_,_) => o
  case o @ Number(_) => Number(-o.num)
  case _ => Var("x")
}
