package com.nobbyknox

object Watcher {

  def watchCdi(): Unit = {
    println(s"Watching CDI directory in thread ${Thread.currentThread().getName}...")

    Utils.getListOfFiles("src/main/resources/cdi").foreach(file => {
      println(s"name: ${file.getName}, size: ${file.length()}, modified: ${file.lastModified()}")
    })
  }

  def watchCamt53(): Unit = {
    println(s"Watching CAMT53 directory in thread ${Thread.currentThread().getName}...")

    Utils.getListOfFiles("src/main/resources/camt53").foreach(file => {
      println(s"name: ${file.getName}, size: ${file.length()}, modified: ${file.lastModified()}")
    })
  }

}
