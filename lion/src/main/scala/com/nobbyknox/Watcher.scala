package com.nobbyknox

import java.io.File
import java.util.Properties

import com.nobbyknox.dal.SqlDataProvider
import grizzled.slf4j.Logger

object Watcher {

  private val logger = Logger("Watcher")

  def watchCdi(properties: Properties): Unit = {
    logger.debug(s"Watching CDI directory in thread ${Thread.currentThread().getName}...")

    Utils.getListOfFiles(properties.getProperty("cdi.directory.landing")).foreach(file => {
      logger.debug(s"name: ${file.getName}, size: ${file.length()}, modified: ${file.lastModified()}")

      val newFile: File = new File(properties.getProperty("cdi.directory.completed") + s"/${file.getName}")
      file.renameTo(newFile)
      SqlDataProvider.insertProcessedFile("KE", file.getName)
    })
  }

  def watchCamt53(properties: Properties): Unit = {
    logger.debug(s"Watching CAMT53 directory in thread ${Thread.currentThread().getName}...")

    Utils.getListOfFiles(properties.getProperty("camt53.directory.landing")).foreach(file => {
      logger.debug(s"name: ${file.getName}, size: ${file.length()}, modified: ${file.lastModified()}")

      val newFile: File = new File(properties.getProperty("camt53.directory.completed") + s"/${file.getName}")
      file.renameTo(newFile)
      SqlDataProvider.insertProcessedFile("MZ", file.getName)
    })
  }

}
