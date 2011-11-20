name := "scuml"

organization := "com.github.sdb"

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2" % "1.6.1",
  "junit" % "junit" % "4.10"
)

scalaSource in Compile <<= baseDirectory { (base) => base / "src" }

scalaSource in Test <<= baseDirectory { (base) => base / "test" }

testOptions in Test ++= Seq(Tests.Argument("junitxml"), Tests.Argument("console"))