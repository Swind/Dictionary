import com.twitter.sbt._
 
seq(PackageDist.newSettings: _*)

seq(GitProject.gitSettings: _*)

packageDistZipName := "DictionaryServer.zip"
 
organization := "cc.spray"
 
name := "DictionaryServer"

mainClass in (Compile, packageBin) := Some("spray.examples.Main")
 
version := "0.1.0-SNAPSHOT"
 
scalaVersion := "2.9.2"

resolvers ++= Seq(
"Typesafe repo" at "http://repo.typesafe.com/typesafe/releases/",
"spray repo" at "http://repo.spray.cc/"
)

libraryDependencies ++= Seq(
   	"org.scalatest" %% "scalatest" % "1.8",
    "org.jsoup" % "jsoup" % "1.7.1",
    "com.google.protobuf" % "protobuf-java" % "2.4.1",
   	"com.typesafe.akka" % "akka-actor" % "2.0.3",
	"cc.spray" % "spray-server" % "1.0-M2",
	"cc.spray" % "spray-can" % "1.0-M2",
   	"junit" % "junit" % "4.10",
   	"com.typesafe.akka" % "akka-slf4j" % "2.0.3",
   	"org.slf4j" % "slf4j-api" % "1.6.6",
   	"ch.qos.logback" % "logback-classic" % "1.0.7"
)

//sbteclipse setting
EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource
