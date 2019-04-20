package com.nobbyknox

import com.nobbyknox.rest.Controller

object Application extends App {

  Controller.start

  val mainLoopSleepTime = 10000

  mainLoop()

  def mainLoop(): Unit = {

    while (true) {
      Watcher.watchCdi()
      Watcher.watchCamt53()

      Thread.sleep(mainLoopSleepTime)
    }
  }

}
