import org.apache.commons.io.IOUtils

lazy val root = (project in file(".")).
  enablePlugins(SbtServePlugin).
  settings(
    scalaVersion := "2.11.8",
    TaskKey[Unit]("check") := {
      val content = IOUtils.toString(new URL("http://localhost:8000/test.txt"), "utf-8")
      if (content != "test") sys.error(s"Unexpected content: $content")
    }
  )