resolvers += "twitter-repo" at "http://maven.twttr.com"

addSbtPlugin("com.twitter" %% "sbt-package-dist" % "1.0.7")

resolvers += Classpaths.typesafeResolver

addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.1.0")