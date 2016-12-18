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

def check(name: String, url: String, content: String) = TaskKey[Unit](name) := {
  val result = download(new URL(url))
  if (result != content) sys.error(s"Unexpected content: $result")
}

lazy val root = (project in file(".")).
  enablePlugins(SbtServePlugin).
  settings(
    scalaVersion := "2.11.8",
    check("checkTest", "http://localhost:8000/test.txt", "test"),
    check("checkIndex", "http://localhost:8000", "index")
  )