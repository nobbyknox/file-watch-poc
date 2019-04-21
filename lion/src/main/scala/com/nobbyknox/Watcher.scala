package com.nobbyknox

import grizzled.slf4j.Logger

object Watcher {

  private val logger = Logger("Watcher")

  def watchCdi(): Unit = {
    logger.debug(s"Watching CDI directory in thread ${Thread.currentThread().getName}...")

    Utils.getListOfFiles("src/main/resources/cdi").foreach(file => {
      logger.debug(s"name: ${file.getName}, size: ${file.length()}, modified: ${file.lastModified()}")
    })
  }

  def watchCamt53(): Unit = {
    logger.debug(s"Watching CAMT53 directory in thread ${Thread.currentThread().getName}...")

    Utils.getListOfFiles("src/main/resources/camt53").foreach(file => {
      logger.debug(s"name: ${file.getName}, size: ${file.length()}, modified: ${file.lastModified()}")
    })
  }

}
