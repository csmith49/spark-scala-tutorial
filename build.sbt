name          := "spark-scala-tutorial"
organization  := "com.lightbend"
description   := "Spark Scala Tutorial"
version       := "6.0.0"
// MIGRATION: Updated Scala version from 2.11.8 to 2.12.15 for Spark 3.0 compatibility
scalaVersion  := "2.12.15"
scalacOptions := Seq("-deprecation", "-unchecked", "-encoding", "utf8", "-Xlint")
excludeFilter in unmanagedSources := (HiddenFileFilter || "*-script.scala")
unmanagedResourceDirectories in Compile += baseDirectory.value / "conf"
unmanagedResourceDirectories in Test += baseDirectory.value / "conf"
//This is important for some programs to read input from stdin
connectInput in run := true
// Works better to run the examples and tests in separate JVMs.
fork := true
// Must run Spark tests sequentially because they compete for port 4040!
parallelExecution in Test := false

// MIGRATION: Added JVM options for Java 17 compatibility with Spark 3.0
javaOptions ++= Seq(
  "--add-opens=java.base/java.lang=ALL-UNNAMED",
  "--add-opens=java.base/java.lang.invoke=ALL-UNNAMED",
  "--add-opens=java.base/java.lang.reflect=ALL-UNNAMED",
  "--add-opens=java.base/java.io=ALL-UNNAMED",
  "--add-opens=java.base/java.net=ALL-UNNAMED",
  "--add-opens=java.base/java.nio=ALL-UNNAMED",
  "--add-opens=java.base/java.util=ALL-UNNAMED",
  "--add-opens=java.base/java.util.concurrent=ALL-UNNAMED",
  "--add-opens=java.base/java.util.concurrent.atomic=ALL-UNNAMED",
  "--add-opens=java.base/sun.nio.ch=ALL-UNNAMED",
  "--add-opens=java.base/sun.nio.cs=ALL-UNNAMED",
  "--add-opens=java.base/sun.security.action=ALL-UNNAMED",
  "--add-opens=java.base/sun.util.calendar=ALL-UNNAMED",
  "--add-opens=java.security.jgss/sun.security.krb5=ALL-UNNAMED"
)

// MIGRATION: Updated Spark version from 2.3.0 to 3.0.3 (latest stable 3.0.x)
val sparkVersion        = "3.0.3"
// MIGRATION: Updated test library versions for compatibility
val scalaTestVersion    = "3.2.9"
val scalaCheckVersion   = "1.15.4"

libraryDependencies ++= Seq(
  "org.apache.spark"  %% "spark-core"      % sparkVersion,
  "org.apache.spark"  %% "spark-streaming" % sparkVersion,
  "org.apache.spark"  %% "spark-sql"       % sparkVersion,
  "org.apache.spark"  %% "spark-hive"      % sparkVersion,
  "org.apache.spark"  %% "spark-repl"      % sparkVersion,

  "org.scalatest"     %% "scalatest"       % scalaTestVersion  % "test",
  "org.scalacheck"    %% "scalacheck"      % scalaCheckVersion % "test")


initialCommands += """
  import org.apache.spark.sql.SparkSession
  import org.apache.spark.SparkContext
  val spark = SparkSession.builder.
    master("local[*]").
    appName("Console").
    config("spark.app.id", "Console").   // To silence Metrics warning.
    getOrCreate()
  val sc = spark.sparkContext
  val sqlContext = spark.sqlContext
  import sqlContext.implicits._
  import org.apache.spark.sql.functions._    // for min, max, etc.
  """

cleanupCommands += """
  println("Closing the SparkSession:")
  spark.stop()
  """

addCommandAlias("ex2",  "runMain WordCount2")
addCommandAlias("ex3",  "runMain WordCount3")
addCommandAlias("ex4",  "runMain Matrix4")
addCommandAlias("ex5a", "runMain Crawl5a")
addCommandAlias("ex5b", "runMain InvertedIndex5b")
addCommandAlias("ex6",  "runMain NGrams6")
addCommandAlias("ex7",  "runMain Joins7")
addCommandAlias("ex8",  "runMain SparkSQL8")

// Note the differences in the next two definitions:
addCommandAlias("ex10directory",  "runMain SparkStreaming10Main")
addCommandAlias("ex10socket",     "runMain SparkStreaming10Main --socket localhost:9900")
