name := "scala-programming-notes"

version := "1.0"

lazy val scala212 = "2.12.10"
lazy val scala213 = "2.13.0"
lazy val supportedScalaVersions = List(scala213, scala212)

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "2.4.4",
  "org.apache.spark" %% "spark-sql" % "2.4.4",
  "org.apache.spark" %% "spark-streaming" % "2.4.4",
  "org.json4s" %% "json4s-native" % "3.6.7",
  "org.json4s" %% "json4s-jackson" % "3.6.7",
  "org.scala-lang.modules" %% "scala-xml" % "1.2.0",
  "org.scalactic" %% "scalactic" % "3.1.0",
  "org.scalatest" %% "scalatest" % "3.1.0" % "test",
  "com.google.code.gson" % "gson" % "2.8.6",
  "org.projectlombok" % "lombok" % "1.18.12" % "compile"
)

