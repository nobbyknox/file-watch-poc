/**
  * Stuff of interest:
  *
  * - http://belablotski.blogspot.com/2016/07/working-with-h2-database-console-and.html
  * - http://sparkjava.com/tutorials/reducing-java-boilerplate
  */

package com.nobbyknox.dal

object SqlDataProvider extends DataProvider {

  override def createSchema(): Unit = ???

  override def terminate(): Unit = {
    println("SQLDataProvider cleaning up...")
  }

  def testQuery(): Unit = ???

}
