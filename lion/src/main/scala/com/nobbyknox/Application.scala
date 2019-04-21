package com.nobbyknox

import java.io.{FileInputStream, FileNotFoundException}
import java.util.Properties

import com.nobbyknox.dal.SqlDataProvider
import com.nobbyknox.rest.Controller
import org.apache.commons.cli.{DefaultParser, HelpFormatter, Options}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Application extends App {

  val commandLineOptions = getCommandLineOptions
  val commandLineArguments = getUserCommandLineArguments(commandLineOptions)
  val properties = new Properties()

  // If help requested or properties not specified, print help and exit
  if (commandLineArguments.hasOption("h") || !commandLineArguments.hasOption("p")) {
    val helper = new HelpFormatter()
    helper.printHelp("Application", commandLineOptions)
    sys.exit(0)
  }

  if (commandLineArguments.hasOption("p")) {
    try {
      properties.load(new FileInputStream(commandLineArguments.getOptionValue("p")))
    } catch {
      case e: FileNotFoundException => {
        println(s"The properties file ${commandLineArguments.getOptionValue("p")} does not exist")
        sys.exit(1)
      }
      case e => {
        println(e.printStackTrace())
        sys.exit(1)
      }
    }
  }

  if (commandLineArguments.hasOption("v")) {
    println("Main thread name: " + Thread.currentThread().getName)
  }

  SqlDataProvider.start()
  Controller.start()

  val mainLoopSleepTime = 10000

  // Run the watch loop in its own thread
  val watchFuture = Future {
    watchLoop()
  }

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

    if (commandLineArguments.hasOption("v")) {
      println("Watch loop thread name: " + Thread.currentThread().getName)
    }

    while (true) {
      Watcher.watchCdi()
      Watcher.watchCamt53()

      Thread.sleep(mainLoopSleepTime)
    }
  }

  def getCommandLineOptions: Options = {
    val options = new Options()
    options.addOption("h", "help", false, "Shows the usage screen")
    options.addOption("v", "verbose", false, "Be verbose")
    options.addOption("t", "type", true, "Pipeline type: cdi, camt53, camt52")
    options.addOption("f", "file", true, "File to process")
    options.addOption("p", "properties", true, "Properties file (mandatory)")
    options
  }

  def getUserCommandLineArguments(options: Options) = {
    val parser = new DefaultParser()
    parser.parse(options, args)
  }

}
