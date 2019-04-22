/**
  * Check out
  *
  * - http://h2database.com/javadoc/org/h2/tools/Server.html
  */
package com.nobbyknox.dal

import java.util.Properties

import grizzled.slf4j.Logger

object SqlDataProvider {
  def apply(properties: Properties, databaseManager: DatabaseManager): SqlDataProvider = {
    new SqlDataProvider(properties, databaseManager)
  }
}

class SqlDataProvider(properties: Properties, databaseManager: DatabaseManager) {
  private val logger = Logger("SqlDataProvider")

  def createSchema(): Unit = {
    val conn = databaseManager.getConnection
    val stmt = conn.createStatement()
    val sql =
      """
        |create schema if not exists watcher;
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

  def testQuery(): Int = {
    val conn = databaseManager.getConnection
    var result = 0

    val stmt = conn.createStatement()
    stmt.execute("select * from WATCHER.PROCESSED_FILES")
    val resultSet = stmt.getResultSet

    while (resultSet.next()) {
      result = resultSet.getInt(1)
      logger.debug(s"id: ${resultSet.getInt(1)}, country: ${resultSet.getString(2)}, filename: ${resultSet.getString(3)}")
    }

    stmt.close()
    conn.close()

    result
  }


  def insertProcessedFile(countryCode: String, filename: String): Unit = {
    val conn = databaseManager.getConnection
    val sql =
      """
        |insert into WATCHER.PROCESSED_FILES
        |  (COUNTRY_CODE, FILENAME, PROCESS_DATE)
        |values
        |  (?, ?, CURRENT_TIMESTAMP)
      """.stripMargin

    val stmt = conn.prepareStatement(sql)
    stmt.setString(1, countryCode)
    stmt.setString(2, filename)
    stmt.execute()

    stmt.close()
    conn.close()
  }

}