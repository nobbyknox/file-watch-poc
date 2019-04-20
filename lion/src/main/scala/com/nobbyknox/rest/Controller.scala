package com.nobbyknox.rest

import spark.{Request, Response, Spark}

object Controller {

  println("Top of rest controller")

  def start(): Unit = {
    println("Starting REST server...")

    Spark.port(8080)

    Spark.get("/hello", (request: Request, response: Response) => {
      println("Top of /hello")
      "Hello World!"
    })

    Spark.get("/test001", (request: Request, response: Response) => {
      println("Top of /test001")
      response.status(200)

      // What is up with the backticks?
      response.`type`("application/json")
      "abc"
    })
  }

}
