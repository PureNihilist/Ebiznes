logLevel := Level.Warn

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.5.4")

addSbtPlugin("com.tuplejump" % "sbt-yeoman" % "0.9.0")

addSbtPlugin("org.scalariform" % "sbt-scalariform" % "1.6.0")

addSbtPlugin("com.heroku" % "sbt-heroku" % "0.4.3")

addSbtPlugin("org.foundweekends" % "sbt-bintray" % "0.4.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.2.0-M8")

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.4" exclude("org.apache.maven", "maven-plugin-api"))

addSbtPlugin("me.lessis" % "bintray-sbt" % "0.3.0")
