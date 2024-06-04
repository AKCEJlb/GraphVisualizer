// val scala3Version = "3.4.2"

// lazy val root = project
//   .in(file("."))
//   .settings(
//     name := "graph_smth",
//     version := "0.1.0-SNAPSHOT",

//     scalaVersion := scala3Version,

//     libraryDependencies += "org.scalameta" %% "munit" % "1.0.0" % Test
//   )
    
// ThisBuild / libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "3.0.0"
// // libraryDependencies += "org.scalafx" %% "scalafx" % "22.0.0-R33"
      

ThisBuild / scalacOptions ++= Seq("-unchecked", "-deprecation", "-encoding", "utf8", "-feature")

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.4.2"

libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "3.0.0"