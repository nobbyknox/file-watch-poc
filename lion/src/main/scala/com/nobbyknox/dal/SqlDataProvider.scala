/**
  * Check out
  *
  * - http://h2database.com/javadoc/org/h2/tools/Server.html
  */
package com.nobbyknox.dal

import java.sql.{Connection, DriverManager}

import grizzled.slf4j.Logger
import org.h2.tools.Server

object SqlDataProvider {

  private val logger = Logger("SqlDataProvider")

  // jdbc:h2:./db/dbhmon
  // jdbc:h2:mem:watcher;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=TRUE
  // jdbc:h2:/Users/nobby/tmp/006/db/WatcherDatabase:watcher;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=TRUE
  // jdbc:h2:tcp://localhost:9123/WatcherDatabase
  private val connString = "jdbc:h2:tcp://localhost:9123/WatcherDatabase"
  private val username = "sa"
  private val password = ""

//  private val server = Server.createTcpServer("-tcpPort", "9123", "-tcpAllowOthers", "-trace").start()
  private val server: Unit = Server.main("-tcp", "-tcpPort", "9123", "-tcpAllowOthers", "-trace", "-web", "-webAllowOthers", "-webPort", "8081", "-baseDir", "/Users/nobby/tmp/006/db/")

  def start(): Unit = {
    Class.forName("org.h2.Driver")
  }

  def createSchema(): Unit = {
    val conn = getConnection
    val stmt = conn.createStatement()
    val sql =
      """
        |create schema if not exists watcher;
        |set schema watcher;
        |
        |create table if not exists watcher.processed_files (
        |id long auto_increment primary key,
        |country_code varchar(2) not null,
        |filename varchar(255) not null,
        |process_date timestamp
        |);
      """.stripMargin
    stmt.execute(sql)
    stmt.close()
    conn.close()
  }

  def terminate(): Unit = {
    logger.info("SQLDataProvider cleaning up...")
//    server.stop()
  }

  def testQuery(): Int = {
    val conn = getConnection
    var result = 0

    val stmt = conn.createStatement()
    stmt.execute("select * from WATCHER.PROCESSED_FILES")
    val resultSet = stmt.getResultSet

    while (resultSet.next()) {
      result = resultSet.getInt(1)
      logger.debug(s"id: ${resultSet.getInt(1)}, country: ${resultSet.getString(2)}, filename: ${resultSet.getString(3)}")
    }

//    if (resultSet.first()) {
//      result = resultSet.getInt(1)
//      println(resultSet)
//    }

    stmt.close()
    conn.close()

    result
  }

  def getConnection: Connection = {
    DriverManager.getConnection(connString, username, password)
  }

}
