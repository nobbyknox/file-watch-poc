package com.nobbyknox

import java.util.Properties

import com.nobbyknox.dal.DatabaseManager

object AppContext {
  def apply(properties: Properties, databaseManager: DatabaseManager) = {
    new AppContext(properties, databaseManager)
  }
}

case class AppContext(properties: Properties, databaseManager: DatabaseManager) {
}
