package com.nobbyknox

import java.util.Properties

import grizzled.slf4j.Logger
import spark.{Request, Response, Spark}

object RestController {

  val logger = Logger("RestController")

  def start(context: AppContext): Unit = {
    logger.trace("Starting REST server...")

    Spark.port(context.properties.getProperty("rest.server.port").toInt)

    Spark.get("/hello", (request: Request, response: Response) => {
      logger.trace("Top of /hello")
      "Hello World!"
    })

    Spark.get("/test001", (request: Request, response: Response) => {
      logger.trace("Top of /test001")
      response.status(200)

      // What is up with the backticks?
      response.`type`("application/json")
      "abc"
    })
  }

}
