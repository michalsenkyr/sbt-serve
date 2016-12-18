import java.net.HttpURLConnection
import java.util.zip.GZIPInputStream

import org.apache.commons.io.IOUtils

def download(url: URL): String = {
  url.openConnection() match {
    case c: HttpURLConnection if c.getResponseCode == 308 =>
      download(new URL(url, c.getHeaderField("Location")))
    case c =>
      val input = new GZIPInputStream(c.getInputStream)
      try {
        IOUtils.toString(input, "utf-8")
      } finally {
        input.close()
      }
  }
}

lazy val root = (project in file(".")).
  enablePlugins(SbtServePlugin).
  settings(
    scalaVersion := "2.11.8",
    port in serve := 8001,
    TaskKey[Unit]("check") := {
      val result = download(new URL("http://localhost:8001/test.txt"))
      if (result != "testPort") sys.error(s"Unexpected content: $result")
    }
  )