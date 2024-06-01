ThisBuild / version := "1.0.0"

ThisBuild / scalaVersion := "2.12.15"

val sparkVersion = "3.5.1"
val log4jVersion = "2.23.0"

lazy val root = (project in file("."))
  .settings(
    name := "apache-spark-exploration"
  )

resolvers ++= Seq(
  "bintray-spark-packages" at "https://dl.bintray.com/spark-packages/maven",
  "Typesafe Simple Repository" at "https://repo.typesafe.com/typesafe/simple/maven-releases",
  "MavenRepository" at "https://mvnrepository.com"
)


libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  // logging
  "org.apache.logging.log4j" % "log4j-api" % log4jVersion,
  "org.apache.logging.log4j" % "log4j-core" % log4jVersion,
  // postgres for DB connectivity
//  "org.postgresql" % "postgresql" % postgresVersion

  // test
  "org.scalatest" %% "scalatest" % "3.2.18" % Test

)
