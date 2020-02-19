package gson

case class Person(name: String, age: Int, gender: String, email: String)

case class Classroom(professor: Person, students: java.util.List[Person])