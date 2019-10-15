case class PersonWithAge(name: String, age: Int)
case class PersonWithSalary(name: String, salary: Int)
case class Person(name: String, age: Int, salary: Int)
case class Grade(name: String, grade: Int)
case class Student(name: String, grades: Seq[Int])

case class PersonWithAgeOption(name: String, age: Option[Int])
case class PersonWithSalaryOption(name: String, salary: Option[Int])
case class PersonOption(name: String, age: Option[Int], salary: Option[Int])
