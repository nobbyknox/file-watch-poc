package com.nobbyknox

import java.io.{FileInputStream, FileNotFoundException}
import java.util.Properties

import com.nobbyknox.dal.DatabaseManager
import grizzled.slf4j.Logger
import org.apache.commons.cli.{DefaultParser, HelpFormatter, Options}
import org.apache.log4j.{Level => Level4J, Logger => Logger4J}

import scala.util.{Failure, Success, Try}

object Application extends App {

  val logger = Logger("Application")

  // Suppress the chatter of others
  Logger4J.getLogger("org").setLevel(Level4J.ERROR)

  val commandLineOptions = getCommandLineOptions
  val commandLineArguments = getUserCommandLineArguments(commandLineOptions)
  var context: AppContext = _

  // If help requested or properties not specified, print help and exit
  if (commandLineArguments.hasOption("h") || !commandLineArguments.hasOption("p")) {
    val helper = new HelpFormatter()
    helper.printHelp("Application", commandLineOptions)
    sys.exit(0)
  }

  // https://alvinalexander.com/scala/functional-error-handling-exceptions-in-scala
  // https://alvinalexander.com/source-code/scala/scala-try-success-and-failure-example
  initializeApp match {
    case Success(c) => context = c
    case Failure(exception) => {
      logger.error(exception)
      sys.exit(1)
    }
  }

  val watcher = Watcher(context)

  context.databaseManager.start()
  RestController.start(context)

  // Run the watch loop in its own thread
  // https://alvinalexander.com/scala/differences-java-thread-vs-scala-future
  val watchThread = new Thread {
    override def run(): Unit = {
      watcher.start()
    }
  }

  watchThread.start()

  // Clean up when we are terminated
  sys.addShutdownHook({
    logger.info("Shutdown hook called")
    watcher.stop()
    context.databaseManager.stop()
    logger.info("Goodbye")
  })

  def initializeApp: Try[AppContext] = Try {
    val props = getProperties
    val dm = getDatabaseManager(props)
    new AppContext(props, dm)
  }

  def getProperties: Properties = {
    val properties = new Properties()
    properties.load(new FileInputStream(commandLineArguments.getOptionValue("p")))
    properties
  }

  def getDatabaseManager(properties: Properties): DatabaseManager = {
    DatabaseManager(properties)
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
