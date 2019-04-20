package com.nobbyknox

import com.nobbyknox.dal.SqlDataProvider
import com.nobbyknox.rest.Controller

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object Application extends App {

  println("Main thread name: " + Thread.currentThread().getName)

  SqlDataProvider.start()
  Controller.start()

  val mainLoopSleepTime = 10000

  // Run the watch loop in its own thread
//  val watchFuture = Future {
//    watchLoop()
//  }

//  val result = SqlDataProvider.testQuery()
//  println(s"result: $result")

  SqlDataProvider.start()
//  SqlDataProvider.createSchema()
  SqlDataProvider.testQuery()

  // Clean up when we are terminated
  sys.addShutdownHook({
    println("Shutdown hook called")
    SqlDataProvider.terminate()
    println("Goodbye")
  })

  def watchLoop(): Unit = {

    println("Watch loop thread name: " + Thread.currentThread().getName)

    while (true) {
      Watcher.watchCdi()
      Watcher.watchCamt53()

      Thread.sleep(mainLoopSleepTime)
    }
  }

}
