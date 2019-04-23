package com.nobbyknox

import java.io.{FileInputStream, FileNotFoundException}
import java.util.Properties

import com.nobbyknox.dal.DatabaseManager
import grizzled.slf4j.Logger
import org.apache.commons.cli.{DefaultParser, HelpFormatter, Options}
import org.apache.log4j.{Level => Level4J, Logger => Logger4J}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Application extends App {

  val logger = Logger("Application")

  // Suppress the chatter of others
  Logger4J.getLogger("org").setLevel(Level4J.ERROR)

  val commandLineOptions = getCommandLineOptions
  val commandLineArguments = getUserCommandLineArguments(commandLineOptions)

  // If help requested or properties not specified, print help and exit
  if (commandLineArguments.hasOption("h") || !commandLineArguments.hasOption("p")) {
    val helper = new HelpFormatter()
    helper.printHelp("Application", commandLineOptions)
    sys.exit(0)
  }

  // TODO: Rewrite this using Try/Success/Failure
  val context = initializeApp

  context.databaseManager.start()

  RestController.start(context)

  val mainLoopSleepTime = context.properties.getProperty("watcher.sleepTime").toInt

  // Run the watch loop in its own thread
  val watchFuture = Future {
    watchLoop()
  }

  // Clean up when we are terminated
  sys.addShutdownHook({
    logger.info("Shutdown hook called")
    context.databaseManager.stop()
    logger.info("Goodbye")
  })

  def initializeApp: AppContext = {
    val props = getProperties
    val dm = getDatabaseManager(props)
    new AppContext(props, dm)
  }

  def getProperties: Properties = {
    try {
      val properties = new Properties()
      properties.load(new FileInputStream(commandLineArguments.getOptionValue("p")))
      properties
    } catch {
      case e: FileNotFoundException => {
        logger.error(s"The properties file ${commandLineArguments.getOptionValue("p")} does not exist")
        sys.exit(1)
      }
      case e => {
        println(e.printStackTrace())
        sys.exit(1)
      }
    }
  }

  def getDatabaseManager(properties: Properties): DatabaseManager = {
    DatabaseManager(properties)
  }

  def watchLoop(): Unit = {
    val watcher = Watcher(context)

    while (true) {
      watcher.watchCdi()
      watcher.watchCamt53()

      Thread.sleep(mainLoopSleepTime)
    }
  }

  def getCommandLineOptions: Options = {
    val options = new Options()
    options.addOption("h", "help", false, "Shows the usage screen")
    options.addOption("t", "type", true, "Pipeline type: cdi, camt53, camt52")
    options.addOption("f", "file", true, "File to process")
    options.addOption("p", "properties", true, "Properties file (mandatory)")
    options.addOption("m", "migrate", true, "Apply database migration script, exiting when done")
    options
  }

  def getUserCommandLineArguments(options: Options) = {
    val parser = new DefaultParser()
    parser.parse(options, args)
  }

}
