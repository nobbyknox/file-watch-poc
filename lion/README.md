# Lion

This sub-project aims to be the main application in this POC, responsible for orchestration and delegation of all work. It does the following:

1. Watch the CDI directory (see lion.properties) and "processes" files by moving them into the "completed" directory (see lion.properties).
1. Hosts a H2 database server on port 9123 with file system persistence.
1. Serves a web console for the H2 database on port 8081. Use the following connection parameters:
    - driver class: `org.h2.Driver`
    - JDBC URL: `jdbc:h2:tcp://localhost:9123/WatcherDatabase`
    - username: `sa`
    - password: <none>

Run the application with the following command line arguments:

```
--properties src/main/resources/lion.properties
```

## Resources

### Databases

* http://belablotski.blogspot.com/2016/07/working-with-h2-database-console-and.html
* http://sparkjava.com/tutorials/reducing-java-boilerplate

### Threads

* https://gist.github.com/viktorklang/5409467
