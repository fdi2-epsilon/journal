package io.github.lczx.journal

import java.io.File
import java.util.zip.ZipInputStream

import io.github.lczx.journal.FileOps._
import play.api.libs.json._

import scala.io.Source

object Application extends App {

  val apiPath = "https://api.github.com/repos/fdi2-epsilon/teams-game/issues?state=all&labels=story"
  val outDir = new File(System.getProperty("user.dir"), "report-output")

  Report.generate(apiPath, outDir)

}

object Report {

  class Generator(apiPath: String, outDir: File) {

    def apply() {
      extractAssets()
      generateDocument()
    }

    def extractAssets() {
      val htmlAssets = new File(outDir, "user-stories_files")
      htmlAssets.mkdirs()

      val z = new ZipInputStream(getClass.getClassLoader.getResourceAsStream("html_resources.zip"))
      new Unzipper().extract(z, htmlAssets)
    }

    def generateDocument() {
      val json = Json.parse(Source.fromURL(apiPath)("UTF-8").mkString)
      val htmlIndex = new File(outDir, "user-stories.html")
      htmlIndex.text = Issues.fromJson(json).html

      println(s"Document saved at ${htmlIndex.getPath}")
    }

  }

  def generate(apiPath: String, outDir: File) {
    new Generator(apiPath, outDir)()
  }

}

