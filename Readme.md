# Prerequisites

* Apache Maven 3.6.1
* Java JDK 1.8

# Setup

Ensure you have installed the prerequisites and they are available on the PATH environment variable.

# Compiling via Maven

```
mvn compile
```

# Running via Maven

```
mvn exec:java -D"exec.mainClass"="com.arvil.app.App"
```

# Packaging in a JAR with dependencies

The pom.xml file is set to build with all dependencies.  This will result in a larger JAR file, but will allow you to run the JAR anywhere.

```
mvn package
```

# Running in Java

```
java -cp target/com.arvil.app-jar-with-dependencies.jar com.arvil.app.App
```

