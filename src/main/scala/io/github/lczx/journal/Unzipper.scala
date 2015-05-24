package io.github.lczx.journal

import java.io.{File, FileOutputStream, InputStream, OutputStream}
import java.util.zip.ZipInputStream

// Reference implementation: https://gist.github.com/Swind/2568527

class Unzipper {

  val buffer = new Array[Byte](8192)

  def extract(source: ZipInputStream, targetDir: File): Unit = {
    val entry = source.getNextEntry
    if (entry != null) {
      if (entry.isDirectory)
        new File(targetDir, entry.getName).mkdirs()
      else
        saveFile(source, new FileOutputStream(new File(targetDir, entry.getName)))
      extract(source, targetDir)
    } else
      source.close()
  }

  private def saveFile(in: InputStream, out: OutputStream) = {
    writeFile(bufferReader(in), out)
    out.close()
  }

  private def bufferReader(in: InputStream)(buf: Array[Byte]) = (in.read(buf), buf)

  def writeFile(reader: Array[Byte] => Tuple2[Int, Array[Byte]], out: OutputStream): Boolean = {
    val (length, data) = reader(buffer)
    if (length >= 0) {
      out.write(data, 0, length)
      writeFile(reader, out)
    } else true
  }

}
