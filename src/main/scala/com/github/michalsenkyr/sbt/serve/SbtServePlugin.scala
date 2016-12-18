package com.github.michalsenkyr.sbt.serve

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import sbt.Keys._
import sbt._
import spray.http.StatusCodes
import spray.routing.SimpleRoutingApp

/**
  * Auto-plugin for easy serving of static files.
  *
  * Settings:
  * - port (default 8000)
  * - sourceDirectory (default src/main/web)
  *
  * Tasks:
  * - serve
  *
  * Uses [[http://spray.io Spray.io]] internally. Akka actor system name is "sbt-serve".
  *
  * Automatically adds redirect from root to index.html
  *
  * @note Task `serve` is asynchronous. SBT must be kept open.
  * @todo `package` task support
  * @todo Task for synchronous serving
  * @todo Task to stop serving
  * @author Michal Senkyr
  */
object SbtServePlugin extends AutoPlugin with SimpleRoutingApp {

  private val cl = getClass.getClassLoader
  implicit val system = ActorSystem("sbt-serve", ConfigFactory.load(cl), cl)

  object autoImport {
    val port = settingKey[Int]("Port")
    val serve = taskKey[Unit]("Serve static files")
    // val packagePath = settingKey[File]("Path inside package")
  }

  import autoImport._

  override lazy val projectSettings = Seq(
    port in serve := 8000,
    sourceDirectory in serve := file("src/main/web"),
    serve := serveTask.value
    // packagePath in serve := new File("web")
  )

  lazy val serveTask =
    Def.task {
      val srcDir = baseDirectory.value / (sourceDirectory in serve).value.getPath
      if (!srcDir.exists) throw new IllegalStateException("Source directory does not exist")

      startServer(interface = "localhost", port = (port in serve).value) {
        pathSingleSlash {
          redirect("/index.html", StatusCodes.PermanentRedirect)
        } ~ get {
          compressResponse() {
            getFromDirectory(srcDir.getPath)
          }
        }
      }
    }
}
