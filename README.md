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

</project>
```

The test class is the following:

```java
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class TodosResourceTest {

    private Client client;
    private WebTarget target;

    @Before
    public void setUp() throws Exception {
        client = ClientBuilder.newClient();
        target = client.target("http://localhost:8080/doit/api/todos");
    }

    @Test
    public void fetchTodos() throws Exception {
        Response response = target.request(MediaType.TEXT_PLAIN).get();
        assertThat(response.getStatus(),is(200));
        String payload = response.readEntity(String.class);
        assertThat(payload, startsWith("Hello, time is"));
    }
}
```

Later on this can become a build job in a Jenkins pipeline to do continuous integration / deployment.


### 06.The Entity In BCE

Instead of a String we should manage entities, so we create an Entity class:


```java
package io.github.dinolupo.doit.business.reminders.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ToDo {
    private String caption;
    private String description;
    private int priority;

    public ToDo(String caption, String description, int priority) {
        this.caption = caption;
        this.description = description;
        this.priority = priority;
    }

    public ToDo() {
    }
}
```

modify the rest service as follows:

```java 
    @GET
    public ToDo hello(){
        return new ToDo("Implement Rest Service", "modify the test accordingly", 100);
    }
```

and the test accordingly:

```java
    @Test
    public void fetchTodos() throws Exception {
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        assertThat(response.getStatus(),is(200));
        JsonObject payload = response.readEntity(JsonObject.class);
        assertThat(payload.getString("caption"), startsWith("Implement Rest Service"));
    }
```

### 07.Create Read Update Delete With JAX-RS

In this step we create simple CRUD services and modify the test class accordingly.

> modified methods of ToDoResource.java

```java
    @GET
    @Path("{id}")
    public ToDo find(@PathParam("id") long id){
        return new ToDo("Implement Rest Service Endpoint id="+id, "modify the test accordingly", 100);
    }

    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") long id) {
        System.out.printf("Deleted Object with id=%d\n", id);
    }

    @GET
    public List<ToDo> all() {
        List<ToDo> all = new ArrayList<>();
        all.add(find(42));
        return all;
    }

    @POST
    public void save(ToDo todo) {
        System.out.printf("Saved ToDo: %s\n", todo);
    }
```

**we have used POST because later on we will use a technical key in the Entity. If you use a business key, a key with a meaning for the business, then use PUT so you can pass a key along with the client call. PUT is idempotent.**


> test class

```java
  @Test
    public void crud() throws Exception {
        // GET all
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        assertThat(response.getStatusInfo(),is(Response.Status.OK));
        JsonArray allTodos = response.readEntity(JsonArray.class);
        assertFalse(allTodos.isEmpty());

        JsonObject todo = allTodos.getJsonObject(0);
        assertThat(todo.getString("caption"), startsWith("Implement Rest Service"));

        // GET with id
        JsonObject jsonObject = target.
                path("42").
                request(MediaType.APPLICATION_JSON).
                get(JsonObject.class);

        assertTrue(jsonObject.getString("caption").contains("42"));

        Response deleteResponse = target.
                path("42").
                request(MediaType.APPLICATION_JSON)
                .delete();

        // DELETE
        assertThat(deleteResponse.getStatusInfo(),is(Response.Status.NO_CONTENT));

    }
```


### 08.Separating The Concerns of A Boundary

Mantaining all the business behaviours in the Rest resource class, can be complicated when you want to create unit tests. So you shoud put all the behaviours in a separate class and inject an instance into the rest resource:

> move all the behaviours into a EJB manager class:

```java
@Stateless
public class TodosManager {

    public ToDo findById(long id) {
        return new ToDo("Implement Rest Service Endpoint id="+id, "modify the test accordingly", 100);
    }

    public void delete(long id) {
        System.out.printf("Deleted Object with id=%d\n", id);
    }

    public List<ToDo> findAll() {
        List<ToDo> all = new ArrayList<>();
        all.add(findById(42));
        return all;
    }

    public void save(ToDo todo) {
        System.out.printf("Saved ToDo: %s\n", todo);
    }
}
``` 

> Inject the property into the resource class

```java
@Stateless
@Path("todos")
public class TodosResource {

    @Inject
    TodosManager todosManager;

    @GET
    @Path("{id}")
    public ToDo find(@PathParam("id") long id){
        return todosManager.findById(id);
    }

    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") long id) {
        todosManager.delete(id);
    }

    @GET
    public List<ToDo> all() {
        return todosManager.findAll();
    }
    
    @POST
    public void save(ToDo todo) {
        todosManager.save(todo);
    }
}
```

### 09.Saving The State With JPA

After creating the `TodosManager` class, that is a protocol neutral boundary class, we can use it to save data with JPA. 

1) Annotate the ToDo class with `@Entity`

2) Annotate the `id` property with `@Id` and `@GeneratedValue` since it is a technical key

3) Create a persistence unit, putting the `META-INF/persistance.xml` in the war package:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence xmlns="http://xmlns.jcp.org/xml/ns/persistence" version="2.1">
    <persistence-unit name="production" transaction-type="JTA">
        <properties>
            <property name="javax.persistence.schema-generation.database.action" value="drop-and-create"/>
        </properties>
    </persistence-unit>
</persistence>
```

4) Change `TodosManager` to use JPA as follows:

> inject the EntityManager

```java
@Stateless
public class TodosManager {

    @PersistenceContext
    EntityManager entityManager;

    public ToDo findById(long id) {
        return entityManager.find(ToDo.class, id);
    }


    public void delete(long id) {
        ToDo reference = entityManager.getReference(ToDo.class, id);
        entityManager.remove(reference);
    }


    public List<ToDo> findAll() {
        return entityManager.createNamedQuery(ToDo.findAll, ToDo.class).getResultList();
    }

    public void save(ToDo todo) {
        entityManager.merge(todo);
    }
}
```

### 10.Fixing The HTTP POST Implementation

As stated by the HTTP RFC at [https://tools.ietf.org/html/rfc2616#page-54]() that says:

_If a resource has been created on the origin server, the response
   SHOULD be 201 (Created) and contain an entity which describes the
   status of the request and refers to the new resource, and a Location
   header_

we should return 201 and a Location header instead of void (No Content). So let's adjust the POST method as follows:

1) generate getters in the `ToDo` bean

2) change `save` method such that it returns the `ToDo` saved object

```java
@POST
    public Response save(ToDo todo, @Context UriInfo uriInfo) {
        ToDo savedObject = todosManager.save(todo);
        long id = savedObject.getId();
        URI uri = uriInfo.getAbsolutePathBuilder().path("/" + id).build();
        Response response = Response.created(uri).build();
        return response;
    }
```

### 11.Testing The POST Location Header

3) Adjust the test accordingly:

```java
   @Test
    public void crud() throws Exception {

        // create an object with POST
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        JsonObject todoToCreate = jsonObjectBuilder
                .add("caption", "Implement Rest Service with JPA")
                .add("description", "Connect a JPA Entity Manager")
                .add("priority", 100).build();

        Response postResponse = target.request().post(Entity.json(todoToCreate));
        assertThat(postResponse.getStatusInfo(),is(Response.Status.CREATED));

        String location = postResponse.getHeaderString("Location");
        System.out.printf("location = %s\n", location);

        // GET with id, using the location returned before
        JsonObject jsonObject = client.target(location)
                .request(MediaType.APPLICATION_JSON)
                .get(JsonObject.class);

        assertTrue(jsonObject.getString("caption").contains("Implement Rest Service with JPA"));

        // GET all
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        assertThat(response.getStatusInfo(),is(Response.Status.OK));
        JsonArray allTodos = response.readEntity(JsonArray.class);
        assertFalse(allTodos.isEmpty());

        JsonObject todo = allTodos.getJsonObject(0);
        assertThat(todo.getString("caption"), startsWith("Implement Rest Service"));
        System.out.println(todo);

		 // DELETE
        Response deleteResponse = target.
                path("42").
                request(MediaType.APPLICATION_JSON)
                .delete();

        
        assertThat(deleteResponse.getStatusInfo(),is(Response.Status.NO_CONTENT));
    }
```

4) paying attention to the fact that the delete method could raise an unchecked exception `EntityNotFoundException` when you try to delete a proxied non-existent object, so we adjust like the following:

```java
    public void delete(long id) {
        try {
            ToDo reference = entityManager.getReference(ToDo.class, id);
            entityManager.remove(reference);
        } catch (EntityNotFoundException ex) {
            // we want to remove it, so do not care of the exception
        }
    }
```
 
### 12.ToDo Entity Updates

Let's add a PUT method to permit an update of an entity:

```java
    @PUT
    @Path("{id}")
    public void update(@PathParam("id") long id, ToDo todo) {
        todo.setId(id);
        todosManager.save(todo);
    }
```

as you can see, we used the id parameter to set the Entity id.

### 13.Implementing Status Updates

Here we add a new rest service that is needed to update only a field. We add a field "done", that represent if an activity is done, to the entity and we want to update only that field.

We could do it using the PUT method, but it can be dangerous because we want only to update a field, so we introduce a sub-resource that can be used to update only some fields.

1) add the `boolean done;` field to the Entity

2) Implement the test (test first, TDD)

```java
public void crud() throws Exception {

		...
		
        // update status ("done" field) with a subresource PUT method
        JsonObjectBuilder statusBuilder = Json.createObjectBuilder();
        JsonObject status = statusBuilder
                .add("done", true)
                .build();
        client.target(location)
                .path("status")
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.json(status));

        // verify that status is updated
        // find again with GET {id}
        updatedTodo = client.target(location)
                .request(MediaType.APPLICATION_JSON)
                .get(JsonObject.class);
        assertThat(updatedTodo.getBoolean("done"), is(true));
        
        ...
}        
```

3) Implement the method to satisfy the test

> add the following business method to `TodoManager.java`

```java
    public ToDo updateStatus(long id, boolean done) {
        ToDo todo = findById(id);
        todo.setDone(done);
        return todo;
    }
```

> add the following rest service to `TodosResource.java`

```java
    @PUT
    @Path("{id}/status")
    public ToDo statusUpdate(@PathParam("id") long id, JsonObject status) {
        boolean isDone = status.getBoolean("done");
        return todosManager.updateStatus(id, isDone);
    }
```

### 14.Dealing With Exceptions And Status Codes

We are going to show hot to deal with Exceptions in rest services.

In the previous example we could have two `NullPointerException` in the following code:

> in `TodoManager.updateStatus()`, `todo` can be null

```java
   todo.setDone(done);
```

> in `TodosResource.statusUpdate()`, `status` can be null

```java
	boolean isDone = status.getBoolean("done");
```

to test those cases, let's proceed to update the test class:

> test the case of non existing object:

```java
	     // update status on not existing object
        JsonObjectBuilder notExistingBuilder = Json.createObjectBuilder();
        status = notExistingBuilder
                .add("done", true)
                .build();
        Response response = target.path("-42")
                .path("status")
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.json(status));
        assertThat(response.getStatusInfo(), is(Response.Status.BAD_REQUEST));
        assertFalse(response.getHeaderString("reason").isEmpty());
```

> adjust the code to pass the test:

```java
    @PUT
    @Path("{id}/status")
    public Response statusUpdate(@PathParam("id") long id, JsonObject status) {
        boolean isDone = status.getBoolean("done");
        ToDo todo = todosManager.updateStatus(id, isDone);
        if (todo == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .header("reason","ToDo with id " + id + " does not exist.")
                    .build();
        } else {
            return Response.ok(todo).build();
        }
    }
```

> test the code of malformed json

```java
        // update with malformed status
        JsonObjectBuilder malformedBuilder = Json.createObjectBuilder();
        status = malformedBuilder
                .add("something wrong", true)
                .build();
        response = client.target(location)
                .path("status")
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.json(status));
        assertThat(response.getStatusInfo(), is(Response.Status.BAD_REQUEST));
        assertFalse(response.getHeaderString("reason").isEmpty());
```

> correct the code accordingly

```java
    @PUT
    @Path("{id}/status")
    public Response statusUpdate(@PathParam("id") long id, JsonObject status) {
        if (!status.containsKey("done")) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .header("reason","JSON does not contain required key 'done'")
                    .build();
        }
        boolean isDone = status.getBoolean("done");
        ToDo todo = todosManager.updateStatus(id, isDone);
        if (todo == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .header("reason","ToDo with id " + id + " does not exist.")
                    .build();
        } else {
            return Response.ok(todo).build();
        }
    }
```

### 15.Introducing Standalone JAX-RS Resource

In this step we basically refator the `TodosResource` class creating a new `TodoResource` that manages only basic operation on a single `ToDo` instance, given the **id** parameter. 

Let's see the class here:

```java
public class TodoResource {

    long id;
    TodosManager todosManager;

    public TodoResource(long id, TodosManager todosManager) {
        this.id = id;
        this.todosManager = todosManager;
    }

    @GET
    public ToDo find(){
        return todosManager.findById(id);
    }


    @DELETE
    public void delete() {
        todosManager.delete(id);
    }

    @PUT
    public void update(ToDo todo) {
        todo.setId(id);
        todosManager.save(todo);
    }

    @PUT
    @Path("/status")
    public Response statusUpdate(JsonObject status) {
        if (!status.containsKey("done")) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .header("reason","JSON does not contain required key 'done'")
                    .build();
        }
        boolean isDone = status.getBoolean("done");
        ToDo todo = todosManager.updateStatus(id, isDone);
        if (todo == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .header("reason","ToDo with id " + id + " does not exist.")
                    .build();
        } else {
            return Response.ok(todo).build();
        }
    }
}
```

We have moved all the `{id}` related operations, leaving only the` findAll` and the `save` method. 

The new `{id}` path method in the original `TodosResource` class will become:

```java
	@Path("{id}")
   public TodoResource find(@PathParam("id") long id){
        return new TodoResource(id, todosManager);
   }
```

When executing a path like `/todos/{id}` the JAX-RS engine will enter the `TodosResource` and will hit the previous modified method, so it will return a new `TodoResource` the will execute one of the provided HTTP verb present in that class. 

### 16.Implementing Optimistic Locking

If you want to update a single resource from different clients without risking to overwrite previous values, we could implement an optimistick lock.

To implement optimistick locking we have to introduce a field into the `ToDo` bean:

```java
@Version
private long version;
```

The JPA manager will update that field every time an object is updated. If you try to update an object with the same `@Version` field multiple times, you will get an Exception and a Rollback:

Hibernate will generate a `org.hibernate.StaleObjectStateException` 
that JPA will catch in a `javax.persistence.OptimisticLockException`
that follows in a `javax.ejb.EJBException` that generates a Rollback of the entire transaction.

To verify the problem, let's try to update a single resource two times in the test case, without reading again the bean, the version isn't changed and there is a 500 http response (the EJB exception generates it).

### 17.Exception Mappers and Status 409

To return a more meaningful http response code we could map exceptions using an ExceptionMapper:

```java
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class EJBExceptionMapper implements ExceptionMapper<EJBException>{
    @Override
    public Response toResponse(EJBException exception) {
        Throwable cause = exception.getCause();
        Response unknownError = Response.serverError()
                .header("cause", exception.toString())
                .build();
        if (cause == null) {
            return unknownError;
        }

        if (cause instanceof OptimisticLockException) {
            return Response.status(Response.Status.CONFLICT)
                    .header("cause", "conflict occurred: " + cause)
                    .build();
        }

        return unknownError;
    }
}
```

Conflict is the correct status code to return in this case as stated by the http RFC.

### 18.JAX-RS And Bean Validation

To demonstrate validation we put the following in the `ToDo` class:

```java
    @NotNull
    @Size(min = 1, max = 256)
    private String caption;
``` 

and to validate the bean before it reaches the persistence context add `@Valid` to the rest method:

```java
    ...
    @POST
    public Response save(@Valid ToDo todo, @Context UriInfo uriInfo) {
    ...
    }
```

Test both cases:

```java
    @Test
    public void createNotValidTodo() {
        // create an object with POST with missing "caption" field
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        JsonObject todoToCreate = jsonObjectBuilder
                .add("description", "Connect a JPA Entity Manager")
                .add("priority", 100).build();

        Response postResponse = target.request().post(Entity.json(todoToCreate));
        assertThat(postResponse.getStatusInfo(),is(Response.Status.BAD_REQUEST));
    }

    @Test
    public void createValidTodo() {
        // create an object with POST with missing "caption" field
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        JsonObject todoToCreate = jsonObjectBuilder
                .add("caption", "valid caption")
                .add("description", "Connect a JPA Entity Manager")
                .add("priority", 100).build();

        Response postResponse = target.request().post(Entity.json(todoToCreate));
        assertThat(postResponse.getStatusInfo(),is(Response.Status.CREATED));
    }
```

### 19.Cross Field Validation

Let's suppose we want to introduce a more complicated validation, for example we want that a ToDo has to have a description not null if a priority is higher than 10. We are going to show one method to do that:

1) Let's implement an interface in the business layer that is called `ValidEntity`

```java
public interface ValidEntity {
    public boolean isValid();
}
```

2) Our `ToDo` bean should implement the interface:

```java
public class ToDo implements ValidEntity {
...

    @Override
    public boolean isValid() {
        return (priority > 10 && description != null) || priority <= 10;
    }


}
```

3) We can create unit tests for the bean like the following (unit tests can go in the same business project `doit`, while we mantain integration tests in the other `doit-st` project):

```java
public class ToDoTest {
    @Test
    public void valid() {
        ToDo toDo = new ToDo("", "description", 11);
        assertTrue(toDo.isValid());
    }

    @Test
    public void notValid() {
        ToDo toDo = new ToDo("", null, 11);
        assertFalse(toDo.isValid());
    }
}
```

4) Now we can create a reusable ConstraintValidator class that will be used with an Annotation:

> `CrossCheckConstraintValidator` - put this in the business package

```java
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CrossCheckConstraintValidator implements ConstraintValidator<CrossCheck, ValidEntity> {
    @Override
    public void initialize(CrossCheck constraintAnnotation) {
    }

    @Override
    public boolean isValid(ValidEntity entity, ConstraintValidatorContext context) {
        return entity.isValid();
    }
}
```

> `CrossCheck` annotation - put this in the business package

```java
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CrossCheckConstraintValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CrossCheck {

    String message() default "Cross check failed!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
```

5) Now we can use the annotation to validate our `ToDo` bean:

> put `CrossCheck` annotation to the bean that you want to validate during a transaction

```java
...
@CrossCheck
public class ToDo implements ValidEntity {...}
```

6) Deploy and add an integration test

### 21.Logging And How To Deal With Cross Cutting Concerns

We add an Interceptor to log all the calls to the methods of the TodosManager class

1) Create an interceptor class:

> create the interceptor class in the business package

```java
package io.github.dinolupo.doit.business;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

public class BoundaryLogger {

    @AroundInvoke
    public Object logCall(InvocationContext invocationContext) throws Exception {
        System.out.printf("--> %s\n", invocationContext.getMethod());
        return invocationContext.proceed();
    }
}
```

2) add the annotation to the manager class

> adding the annotation

```java
...
@Interceptors(BoundaryLogger.class)
public class TodosManager {...}
```

3) instead of the System.out line in the interceptor, we could be smarter and add a functional interface (we choose later what concrete class to inject) to collect all the logging

> in the business package, create a new functional interface that specify what to do with log messages

```java
@FunctionalInterface
public interface LogSink {
    void log(String msg);
}
```

4) change the `BoundaryLogger` as follows:

> injecting the `LogSink` and use it

```java
public class BoundaryLogger {

    @Inject
    LogSink LOG;

    @AroundInvoke
    public Object logCall(InvocationContext invocationContext) throws Exception {
        LOG.log("--> " + invocationContext.getMethod());
        return invocationContext.proceed();
    }
}
```

5) Now to use that `LogSink` interface, someone has to produce a concrete class:

> create a `LogSinkProducer` that returns the JDK log info method

```java
package io.github.dinolupo.doit.business;

import io.github.dinolupo.doit.business.LogSink;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import java.util.logging.Logger;

public class LogSinkProducer {

    @Produces
    public LogSink produce(InjectionPoint injectionPoint) {
        Class<?> injectionTarget = injectionPoint.getMember().getDeclaringClass();
        return Logger.getLogger(injectionTarget.getName())::info;
    }
}
```

6) If the logging is a bunsiness requirement, than we can put the logging classes in a business package

> refactor to put all the three logging classes into the correct package

`package io.github.dinolupo.doit.business.logging.boundary;`

### 22.From Logging To Monitoring

Let's capture the performance of the method:

1) Let's create a class that is needed for our monitoring purpose

> create a `CallEvent` class in the `business.monitoring.entity` package

```java
package io.github.dinolupo.doit.business.monitoring.entity;

public class CallEvent {
    private String methodName;
    private long duration;

    public CallEvent(String methodName, long duration) {
        this.methodName = methodName;
        this.duration = duration;
    }

    public String getMethodName() {
        return methodName;
    }

    public long getDuration() {
        return duration;
    }
    
    @Override
    public String toString() {
        return "CallEvent{" +
                "methodName='" + methodName + '\'' +
                ", duration=" + duration +
                '}';
    }
}
```

2) Let's modify the BoundaryLogger to fire an Enterprise Event of type CallEvent

> remove the previous `@Inject` and modify the method to send an Enterpise Event with the duration of the method call

```java
public class BoundaryLogger {

    @Inject
    Event<CallEvent> monitoring;

    @AroundInvoke
    public Object logCall(InvocationContext invocationContext) throws Exception {
        long start = System.currentTimeMillis();
        try {
            return invocationContext.proceed();
        } finally {
            long duration = System.currentTimeMillis() - start;
            monitoring.fire(new CallEvent(invocationContext.getMethod().getName(),duration));
        }
    }
}
```

3) Create a class that captures and processes that Event

> use `@Observes` to process the Event, inject again the LogSink created before to show the output

```java
package io.github.dinolupo.doit.business.monitoring.boundary;

import io.github.dinolupo.doit.business.logging.boundary.LogSink;
import io.github.dinolupo.doit.business.monitoring.entity.CallEvent;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class MonitoringSink {

    @Inject
    LogSink LOG;

    public void onCallEvent(@Observes CallEvent callEvent) {
        LOG.log(callEvent.toString());
    }
}
```

### 23.Exposing Performance Metrics

We want to expose the Events via a Rest interface:

1) Add a structure to hold the Events and add every Event to it. For now we don't care about the the OutOfMemory Exception, we cover this topic later ;)

> add a field `CopyOnWriteArrayList<CallEvent>` and add every event to this collection 

```java
public class MonitoringSink {

    @Inject
    private LogSink LOG;

    private CopyOnWriteArrayList<CallEvent> recentEvents;

    @PostConstruct
    public void postConstruct() {
        recentEvents = new CopyOnWriteArrayList<>();
    }

    public void onCallEvent(@Observes CallEvent callEvent) {
        LOG.log(callEvent.toString());
        recentEvents.add(callEvent);
    }

    public List<CallEvent> getRecentEvents() {
        return recentEvents;
    }
}
```

2) modify the `CallEvent` to add a default constructor needed by JAX-RS and define the annotations to serialize the object:

```java
	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	public class CallEvent {
	...
   		public CallEvent() {
   		}
   	...
   }
```

3) In the `monitoring.boundary` package add a class to expose the list of events

> new Resource to expose events via REST

```java
@Stateless
@Path("boundary-invocations")
public class BoundaryInvocationsResource {

    @Inject
    MonitoringSink monitoringSink;

    @GET
    public List<CallEvent> expose() {
        return monitoringSink.getRecentEvents();
    }
}
```

4) run and then execute the tests to add some data. Then retrieve it with the following:

> curl command

```sh
% curl -i -H "accept: application/json" http://localhost:8080/doit/api/boundary-invocations
HTTP/1.1 200 OK
Connection: keep-alive
X-Powered-By: Undertow/1
Server: WildFly/10
Content-Type: application/json
Content-Length: 423
Date: Sun, 17 Jul 2016 12:45:52 GMT
```

> shell output

```json

[	{"methodName":"save","duration":118},
	{"methodName":"findById","duration":14},
	{"methodName":"save","duration":2},
	{"methodName":"save","duration":3},
	{"methodName":"findById","duration":1},
	{"methodName":"updateStatus","duration":1},
	{"methodName":"findById","duration":2},
	{"methodName":"updateStatus","duration":1},
	{"methodName":"findAll","duration":15},
	{"methodName":"delete","duration":7},
	{"methodName":"save","duration":3}
]
```

5) to be more flexible, you could change the return type of the `expose()` method to `JsonArray` and format information as you like.

