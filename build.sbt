resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
   	"org.scalatest" %% "scalatest" % "1.8",
    "org.jsoup" % "jsoup" % "1.7.1",
    "com.google.protobuf" % "protobuf-java" % "2.4.1",
   	"com.typesafe.akka" % "akka-actor" % "2.0.3",
   	"junit" % "junit" % "4.10"
)

//sbteclipse setting
EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource