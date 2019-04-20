package com.nobbyknox

object Application extends App {
  println("Hello World!")

  val mainLoopSleepTime = 2000

  mainLoop()

  def mainLoop(): Unit = {

    while (true) {
      Watcher.watchCdi()
      Watcher.watchCamt53()

      println("Resting...")
      Thread.sleep(mainLoopSleepTime)
    }
  }

}
