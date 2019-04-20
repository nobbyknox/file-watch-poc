package com.nobbyknox

object Watcher {

  def watchCdi(): Unit = {
    println("Watching CDI directory...")

    Utils.getListOfFiles("src/main/resources/cdi").foreach(file => {
      println(s"name: ${file.getName}, size: ${file.length()}, modified: ${file.lastModified()}")
    })
  }

  def watchCamt53(): Unit = {
    println("Watching CAMT53 directory...")

    Utils.getListOfFiles("src/main/resources/camt53").foreach(file => {
      println(s"name: ${file.getName}, size: ${file.length()}, modified: ${file.lastModified()}")
    })
  }

}
