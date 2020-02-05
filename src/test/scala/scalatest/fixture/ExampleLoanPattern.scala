package scalatest.fixture

import java.io.{File, FileWriter}
import java.util.concurrent.ConcurrentHashMap
import java.util.UUID.randomUUID

object DBServer {
  // simulate a database
  type DB = StringBuffer
  private val dbs = new ConcurrentHashMap[String, DB]()

  def createDB(name: String): DB = {
    val db = new StringBuffer
    dbs.put(name, db)
    db
  }
  def removeDB(name: String): Unit = {
    dbs.remove(name)
  }
}

import org.scalatest.FlatSpec
import DBServer._

class ExampleLoanPattern extends FlatSpec {

  def withDatabase(test: DB => Any): Unit = {
    val name = randomUUID.toString
    val db = createDB(name) // create the fixture
    try {
      db.append("hello ") // perform setup
      test(db) // "loan" the fixture to the test
    } finally {
      removeDB(name) // clean up the fixture
    }
  }

  def withFile(test: (File, FileWriter) => Any): Unit = {
    val file = File.createTempFile("temp_file", "tmp") // create the fixture
    val writer = new FileWriter(file)
    try {
      writer.write("hello ") // perform setup
      test(file, writer) // "loan" the fixture to the test
    } finally {
      writer.close() // clean up the fixture
    }
  }

  "loan example 1" should "succeed" in withFile {
    (file, writer) => {
      writer.write("file!")
      writer.flush()
      assert(file.length == 11)
    }
  }

  "loan example 2" should "succeed" in withDatabase {
    db => {
      db.append("database!")
      assert(db.toString === "hello database!")
    }
  }

  "loan example 3" should "succeed" in withDatabase {
    db => withFile {
      (file, writer) => {
        db.append("database!")
        writer.write("file!")
        writer.flush()
        assert(db.toString === "hello database!")
        assert(file.length == 11)
      }
    }
  }

}
