name               in ThisBuild := "csvside"
organization       in ThisBuild := "io.underscore"
version            in ThisBuild := "1.0.1"
scalaVersion       in ThisBuild := "2.13.1"
crossScalaVersions in ThisBuild := Seq("2.13.1")

licenses += ("Apache-2.0", url("http://apache.org/licenses/LICENSE-2.0"))

scalacOptions ++= Seq(
  "-feature",
  "-unchecked",
  "-deprecation",
  )

addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full)

libraryDependencies ++= Seq(
  "net.sf.opencsv"   % "opencsv"    % "2.3",
  "org.typelevel"   %% "cats-core"  % "2.0.0",
  "com.chuusai"     %% "shapeless"  % "2.3.3",
  "com.davegurnell" %% "unindent"   % "1.2.0" % Test,
  "org.scalatest"   %% "scalatest"  % "3.1.1" % Test
)

publishTo := sonatypePublishTo.value

pomExtra in Global := {
  <url>https://github.com/underscoreio/csvside</url>
  <scm>
    <connection>scm:git:github.com/underscoreio/csvside</connection>
    <developerConnection>scm:git:git@github.com:underscoreio/csvside</developerConnection>
    <url>github.com/underscoreio/csvside</url>
  </scm>
  <developers>
    <developer>
      <id>d6y</id>
      <name>Richard Dallaway</name>
      <url>http://twitter.com/d6y</url>
    </developer>
    <developer>
      <id>davegurnell</id>
      <name>Dave Gurnell</name>
      <url>http://twitter.com/davegurnell</url>
    </developer>
  </developers>
}
