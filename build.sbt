enablePlugins(ScalaJSPlugin)

name := "Shogi Client"
version := "0.1.1"
scalaVersion := "2.12.10"

libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.7"

// scalaJSUseMainModuleInitializer := true
// jsEnv := new org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv()
