package io.github.lczx.journal

import java.io.{PrintWriter, File}

import scala.io.{Codec, Source}
import scala.language.implicitConversions

/**
 * Groovy style file on steroids
 */
class FileOps(file: File) {

  def text = Source.fromFile(file)(Codec.UTF8).mkString

  def text_=(s: String) {
    Some(new PrintWriter(file, "UTF-8")).foreach { p => p.write(s); p.close() }
  }

}

object FileOps {

  implicit def enrichFile(file: File): FileOps = new FileOps(file)

}