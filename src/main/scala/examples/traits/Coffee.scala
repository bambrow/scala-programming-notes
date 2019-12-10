package examples.traits

object Coffee {

  trait Coffee {
    val basePrice: Double

    def price: Double = basePrice

    override def toString: String = "coffee"
  }

  trait Sugar extends Coffee {
    override def toString: String = super.toString + " with sugar"
  }

  trait Milk extends Coffee {
    override def price: Double = super.price + 0.5

    override def toString: String = super.toString + " with milk"
  }

  trait Tea extends Sugar {
    override def toString: String = super.toString + " with tea"
  }

  trait A {
    override def toString: String = "A"
  }

  trait Sugar2 {
    override def toString: String = super.toString + " with sugar"
  }

  trait Tea2 extends Sugar2 {
    override def toString: String = super.toString + " with tea"
  }

  trait Milk3 {
    override def toString: String = super.toString + " with milk"
  }

  trait Sugar3 {
    override def toString: String = super.toString + " with sugar"
  }

  trait Tea3 {
    override def toString: String = super.toString + " with tea"
  }

  def main(args: Array[String]): Unit = {
    val c = new Coffee with Milk with Sugar { val basePrice = 1.0 }
    println(c) // coffee with milk with sugar
    // Coffee <- Milk <- Sugar
    val d = new Coffee with Sugar with Milk { val basePrice = 1.0 }
    println(d) // coffee with sugar with milk
    // Coffee <- Sugar <- Milk
    val c1 = new A with Tea with Milk { val basePrice = 1.0 }
    println(c1) // coffee with sugar with tea with milk
    // A <- Coffee <- Sugar <- Tea <- Milk
    val c2 = new A with Tea2 with Milk { val basePrice = 1.0 }
    println(c2) // coffee with milk
    // A <- Sugar2 <- Tea2 <- Coffee <- Milk
    val c3 = new A with Milk with Tea2 { val basePrice = 1.0 }
    println(c3) // coffee with milk with sugar with tea
    // A <- Coffee <- Milk <- Sugar2 <- Tea2
    val e1 = new A with Tea3 with Sugar3 with Milk3 { val basePrice = 1.0 }
    println(e1) // A with tea with sugar with milk
    // A <- Tea3 <- Sugar3 <- Milk3
    val e2 = new A with Tea3 with Sugar3 with Milk { val basePrice = 1.0 }
    println(e2) // coffee with milk
    // A <- Tea3 <- Sugar3 <- Coffee <- Milk
    val e3 = new A with Tea with Tea2 with Tea3 { val basePrice = 1.0 }
    println(e3) // coffee with sugar with tea with sugar with tea with tea
    // A <- Coffee <- Sugar <- Tea <- Sugar2 <- Tea2 <- Tea3
    val e4 = new A with Tea with Tea2 with Tea3 with Milk { val basePrice = 1.0 }
    println(e4) // coffee with sugar with tea with sugar with tea with tea with milk
    // A <- Coffee <- Sugar <- Tea <- Sugar2 <- Tea2 <- Tea3 <- Milk
  }

}
