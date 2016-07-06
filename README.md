# Effective Java EE tutorial and examples

This project contains a fully working application based on  Java EE 7. Mostly based on Effective Java EE course by Adam Bien at: 

[https://vimeo.com/ondemand/effectivejavaee]()

-----

## Project Folder `doit`

### video 01.Initial Maven Setup

The best way to work with Java EE is to start a Maven project based on an Archetype created by Adam Bien:

> Set up a basic JavaEE ready application with Maven

```sh
mvn archetype:generate -Dfilter=com.airhacks:javaee7-essentials-archetype
```

[https://github.com/AdamBien/javaee7-essentials-archetype]()

Choose version 1.3 of the template, that includes file for JAX-RS Configuration (REST web services)

### 02.Structuring Java EE Applications with BCE

The application will be developed with BCE pattern and module definition will reflect the choice.

More information about the pattern can be found on Google and at:

[https://www.youtube.com/watch?v=grJC6RFiB58]()

For package definition we can have the following convention:

`<company name>.<application name>.<layer>.<component>.<type>`

Because in out case we want to separate Presentation from Business Logic and Integration classes, in our case the `layer` part of the package path can be one of the following:

- presentation (layer)
- business (layer)
- integration (layer)

Our application can contain many components that have connections between each other, and every component is a Java Package that comprehend:

o--| B | C | E |

So, after the component part of the package, we should use one of the following to specify the component part type:

- Boundary
- Control
- Entity

**boundary** are all the classes that have all the interfaces for humans or other objects.

**control** this optional package can contain classes with some logic like validation, complex tasks that cannot stay in the boundary package, etc.

**entity** contains all the classes for persistence of entities

### 03.Coding The BCE Structure

In our "DoIt" application, let's suppose we want to create a new component to manage the Reminders and we want to expose REST web services, we can define the  package as follows:

#### `Package`

- company name: `io.github.dinolupo`
- application name: `doit`
- layer name:	 `business`
- component name: `reminders`
- component layer type name: `boundary`

#### `Class`

- class:				`TodosResource.java`

To begin we add a very simple rest service to the class:

```java
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("todos")
public class TodosResource {

    @GET
    //@Produces(MediaType.APPLICATION_JSON)
    public String hello(){
        return "Hello, time is " + System.currentTimeMillis();
    }

}
```

### 04.Saving Time With Testing

Let's create a new project that is useful to test the main project.

We call it **doit-st** where st stands for System Test.

Create a simple Maven project with the following pom.xml:

> pom.xml that contains JAX-RS dependencies to test the rest services, including JUnit and Matchers

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>io.github.dinolupo</groupId>
    <artifactId>doit-st</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>jar</packaging>

    <dependencies>

        <dependency>
            <groupId>org.glassfish.jersey.core</groupId>
            <artifactId>jersey-client</artifactId>
            <version>2.23.1</version>
        </dependency>
        <dependency>
            <groupId>org.glassfish</groupId>
            <artifactId>javax.json</artifactId>
            <version>1.0.4</version>
        </dependency>
        <dependency>
            <groupId>org.glassfish.jersey.media</groupId>
            <artifactId>jersey-media-json-processing</artifactId>
            <version>2.23.1</version>
        </dependency>

        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.hamcrest</groupId>
            <artifactId>hamcrest-core</artifactId>
            <version>1.3</version>
        </dependency>

    </dependencies>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
    </properties>

</project>```


Later on this can become a build job in a Jenkins pipeline to do continuous integration / deployment.



I