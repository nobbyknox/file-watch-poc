# TODO

* [x] Implement properties file
* [x] Logging
* [x] Separate TCP and Web servers for H2 database
* [x] Need to implement a simple database pool manager for H2 (or use some library)
    - Check out: http://h2database.com/html/tutorial.html?highlight=pool&search=pool#connection_pool
* [x] Rename "Controller" to "RestController" and possibly move it up one package, removing the "rest" package
* [ ] Ensure proper startup of DB before watcher starts it work
* [x] Create BASH script to run application where arguments can be specified to the script
* [ ] Processors to use a queue to pick tasks (files to be processed). This will enable parallel processing of files, either through multiple consumer processors or via multiple threads.
    - Check out:
        - http://queues.io/
        - https://stackoverflow.com/questions/21108728/persistent-queue-datastructure
        - https://github.com/softwaremill/elasticmq
