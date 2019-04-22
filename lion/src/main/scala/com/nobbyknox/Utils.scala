package com.nobbyknox

import java.io.File

object Utils {

  def getListOfFiles(dir: String): List[File] = {
    val d = new File(dir)
    if (d.exists && d.isDirectory) {
      d.listFiles
        .filter(_.isFile)
        .filter(_.getName.endsWith(".dat"))
        .toList
    } else {
      List[File]()
    }
  }

}
