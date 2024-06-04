val scala3Version = "3.4.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "hanoi",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies += "org.scalameta" %% "munit" % "0.7.29" % Test
  )
        libraryDependencies += "org.scalafx" %% "scalafx" % "22.0.0-R33"
      

    scalacOptions ++= Seq("-unchecked", "-deprecation", "-encoding", "utf8", "-feature")
    
lazy val osName = System.getProperty("os.name") match {
  case n if n.startsWith("Linux") => "linux"
  case n if n.startsWith("Mac") => "mac"
  case n if n.startsWith("Windows") => "win"
  case _ => throw new Exception("Unknown platform!")
}
  

    fork := true
