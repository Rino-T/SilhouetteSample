resolvers += Resolver.jcenterRepo

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    organization := "dev.tchiba",
    scalaVersion := "2.13.4",
    name := "SilhouetteSample",
    scalacOptions := Seq(
      "-deprecation",
      "-feature",
      "-Xfatal-warnings"
    ),
    libraryDependencies ++= Seq(
      guice,
      "net.codingwell"         %% "scala-guice"                     % "4.2.11",
      "org.scalatestplus.play" %% "scalatestplus-play"              % "5.0.0"   % Test,
      "com.h2database"          % "h2"                              % "1.4.200" % Test,
      "org.mariadb.jdbc"        % "mariadb-java-client"             % "2.7.1",
      "com.iheart"             %% "ficus"                           % "1.5.0",
      "com.typesafe.play"      %% "play-slick"                      % "5.0.0",
      "com.typesafe.play"      %% "play-slick-evolutions"           % "5.0.0",
      "com.mohiva"             %% "play-silhouette"                 % "7.0.0",
      "com.mohiva"             %% "play-silhouette-persistence"     % "7.0.0",
      "com.mohiva"             %% "play-silhouette-crypto-jca"      % "7.0.0",
      "com.mohiva"             %% "play-silhouette-password-bcrypt" % "7.0.0",
      "com.mohiva"             %% "play-silhouette-testkit"         % "7.0.0"   % Test
    )
  )
