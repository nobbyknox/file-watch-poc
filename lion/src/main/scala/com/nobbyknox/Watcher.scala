package com.nobbyknox

import java.io.File

import com.nobbyknox.dal.SqlDataProvider
import grizzled.slf4j.Logger

object Watcher {

  def apply(context: AppContext): Watcher = {
    new Watcher(context)
  }

}

class Watcher(context: AppContext) {

  private val logger = Logger("Watcher")
  private val db: SqlDataProvider = SqlDataProvider(context)
  private var keepRunning = true
  private val mainLoopSleepTime = context.properties.getProperty("watcher.sleepTime").toInt

  def start(): Unit = {
    logger.debug("Starting")
    while (keepRunning) {
      watchCdi()
      watchCamt53()

      Thread.sleep(mainLoopSleepTime)
    }
  }

  def stop(): Unit = {
    logger.debug("Received stop instruction")
    keepRunning = false
  }

  def watchCdi(): Unit = {
    logger.debug(s"Watching CDI directory in thread ${Thread.currentThread().getName}...")

    Utils.getListOfFiles(context.properties.getProperty("cdi.directory.landing")).foreach(file => {
      logger.debug(s"name: ${file.getName}, size: ${file.length()}, modified: ${file.lastModified()}")

      val newFile: File = new File(context.properties.getProperty("cdi.directory.completed") + s"/${file.getName}")
      file.renameTo(newFile)
      db.insertProcessedFile("KE", file.getName)
    })
  }

  def watchCamt53(): Unit = {
    logger.debug(s"Watching CAMT53 directory in thread ${Thread.currentThread().getName}...")

    Utils.getListOfFiles(context.properties.getProperty("camt53.directory.landing")).foreach(file => {
      logger.debug(s"name: ${file.getName}, size: ${file.length()}, modified: ${file.lastModified()}")

      val newFile: File = new File(context.properties.getProperty("camt53.directory.completed") + s"/${file.getName}")
      file.renameTo(newFile)
      db.insertProcessedFile("MZ", file.getName)
    })
  }

}