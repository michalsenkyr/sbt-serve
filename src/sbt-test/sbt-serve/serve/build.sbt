import org.apache.commons.io.IOUtils

lazy val root = project.
  enablePlugins(SbtServePlugin).
  settings(
    scalaVersion := "2.11.8",
    TaskKey[Unit]("check") := {
      val input = new URL("http://localhost:8000/test.txt").openConnection().getInputStream
      val content = IOUtils.toString(input, "utf-8")
      if (content != "test") sys.error(s"Unexpected content: $content")
    }
  )