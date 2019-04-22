package com.nobbyknox.dal

import java.sql.Connection
import java.util.Properties

import grizzled.slf4j.Logger
import org.h2.jdbcx.JdbcConnectionPool
import org.h2.tools.Server

object DatabaseManager {
  def apply(properties: Properties): DatabaseManager = {
    new DatabaseManager(properties)
  }
}

class DatabaseManager(properties: Properties) {

  val logger = Logger("DatabaseManager")
  var tcpServer: Server = _
  var webServer: Server = _
  var pool: JdbcConnectionPool = _

  def start(): Unit = {
    Class.forName("org.h2.Driver")

    logger.trace("Creating H2 TCP server")
    tcpServer = Server.createTcpServer("-tcpPort", properties.getProperty("database.tcp.port"), "-tcpAllowOthers",
      "-baseDir", "./src/main/resources/").start()

    logger.trace("Creating H2 web server")
    webServer = Server.createWebServer("-webPort", properties.getProperty("database.web.port"), "-webAllowOthers").start()

    this.pool = JdbcConnectionPool.create(
      properties.getProperty("database.connectionString"),
      properties.getProperty("database.username"),
      properties.getProperty("database.password"))

  }

  def stop(): Unit = {
    logger.info("SQLDataProvider cleaning up...")
    webServer.stop()
    tcpServer.stop()
  }

  def getConnection: Connection = {
    this.pool.getConnection
  }
}
