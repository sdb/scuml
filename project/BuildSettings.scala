import sbt._
import Keys._
import Scope.{GlobalScope, ThisScope}

object BuildSettings {
  val buildOrganization = "com.github.sdb"
  val buildVersion      = "0.0.1-SNAPSHOT"
  val buildScalaVersion = "2.9.0-1" 
    
  val buildSettings = Defaults.defaultSettings ++ Seq (
    organization := buildOrganization,
    version      := buildVersion,
    scalaVersion := buildScalaVersion)
}

object Dependencies {
  val specs2 = "org.specs2" %% "specs2" % "1.5" % "test"
}

object ScumlBuild extends Build {
  import BuildSettings._
  import Dependencies._
  
  val testDeps = Seq(specs2)
  
  lazy val scuml = Project (
    "scuml",
    file ("."),
    settings = buildSettings ++ Seq (
        libraryDependencies ++= testDeps,
        testOptions in Test ++= Seq(Tests.Argument("junitxml"), Tests.Argument("console")))
  )
}