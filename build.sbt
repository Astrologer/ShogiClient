enablePlugins(ScalaJSPlugin)

name := "Shogi Client"
version := "0.0.1"
scalaVersion := "2.12.6"

libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.6"

// scalaJSUseMainModuleInitializer := true
// jsEnv := new org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv()
