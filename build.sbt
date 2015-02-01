name := """qiita-playframework-trace-log"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

val kamonVersion = "0.3.5"

val aspectjweaverVersion = "1.8.4"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws,
  "io.kamon"    %% "kamon-core"   % kamonVersion,
  "io.kamon"    %% "kamon-play"   % kamonVersion,
  "org.aspectj" % "aspectjweaver" % aspectjweaverVersion,
  "janino"      % "janino"        % "2.4.3"
)

// 起動シェルにAgentを追加
NativePackagerKeys.bashScriptExtraDefines ++= Seq(
  s"""addJava "-javaagent:$${lib_dir}/org.aspectj.aspectjweaver-$aspectjweaverVersion.jar""""
)
