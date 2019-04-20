# File Watch POC

A proof of concept

## Goals

I want to experiment with the following:

1. Monitor a set of directories for the arrival of new files
1. Delegate the processing of new files to a new thread
1. Keep record of files already processed
1. Allow for ad-hoc requests to process/re-process specified files

## Thoughts on Tech Stack

* Scala, obviously
* H2 filesystem database (thread safe?) for persistence
* REST API
    - What about Akka instead? Might it work?
* Avoid too many 3rd party dependencies
    - Write stuff and get to know Scala
* SBT: yes
