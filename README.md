# UniRutas

The UniRutas System is a use-case example that showcases the capabilities of the Flexcore mini framework. It's specifically tailored to keep the university community informed about the status of university routes. However, remember that the framework is entirely separate from the UniRutas System, and you can use it for your own projects with different use cases.

## FlexCore: A Versatile Database-Agnostic Framework

FlexCore is a flexible, database-agnostic mini framework designed for educational purposes, specifically for the "Software 2 - Software Architectures" course. It is not intended as a serious or commercial project. Designed for code agnosticism, this framework provides an essential foundation for building applications that interact with databases, including both SQL and NoSQL solutions.

> **Note:** Actually FlexCore is not ready to be totally independent, this because it doesn't have a "easily" way to add support to more database engines (actually support MySQL, PostgreSQL and MongoDB). This means is needed to modify the source code to add more engines (you can see this in the `SQLDatabaseEngine` and `NoSQLDatabaseEngine` enums inside `com.flexcore.database.enums`).

####

### Key Features

1. #### Database-Agnostic Functionality

   FlexCore brings added functionality to work with SQL databases effortlessly. To enable support for new databases, simply add the corresponding driver's dependency and update the DatabaseConfig, making it truly versatile.

2. #### Code Agnosticism

   We've refactored the codebase to achieve database engine agnosticism. FlexCore works seamlessly with both SQL and NoSQL databases, offering you unparalleled flexibility and scalability.

3. #### Factory Pattern Implementation

   To facilitate this agnostic behavior, we've introduced factory patterns. These factories work with specific providers, each tailored for SQL or NoSQL implementations. This abstraction allows for the seamless integration of various database engines without exposing particular implementation details.

4. #### Database Engine Enums

   To maintain clear communication and facilitate database engine selection, we've introduced enumerations listing available database engines. These enums are a reference point for the factories to determine which classes or information to utilize, ensuring a cohesive and organized structure.

5. #### Architectural Restructuring

   We've restructured the package layout to enhance the overall architecture, improving code organization and maintainability. The codebase now better supports different database engines without compromising code clarity.

6. #### Decoupled Framework Logic

   FlexCore's framework logic has been decoupled from system-specific elements, enabling greater modularity and integration into diverse systems. This separation ensures that the framework operates independently and efficiently.

7. #### Logger Integration
   We've added a logging mechanism to enhance the system's observability and troubleshooting capabilities. The logger captures important events and errors, aiding in system monitoring and debugging.

### Key Interfaces and Methods

#### ICustomQueryBuilder: Core interface for building and executing custom SQL queries.

- `select()`: Start the query builder, equivalent to SELECT \* FROM table.
- `fields(String... fields)`: Select specific fields in the query.
- `where(String field, Object value)`: Add a WHERE clause to the query.
- `and(String field, Object value)`: Add an AND clause to the query.
- `or(String field, Object value)`: Add an OR clause to the query.
- `execute()`: Execute the constructed query and return the result as a matrix of tuples.

###### SQL JOIN Operations

We've provided support for SQL JOIN operations, including:

- `join(String sourceField, Class<?> targetEntity, String targetField)`: Add a join clause to the query.
- `join(Class<?> sourceEntity, String sourceField, Class<?> targetEntity, String targetField)`: Add additional join clauses, enabling nested joins.
- `joinFields(String... fields)`: Specify the fields to select in a query with join clauses.

#### IConnectionPool<?>:

Core interface for accessing the connection pool methods.

###### ConnectionPoolFactoryProvider:

Provider to instance a database connection pool.

- getFactory(): Returns the correct factory to the database engine that is configured in the `database.properties` file.

#### Enhanced Annotations

FlexCore introduces comprehensive annotations:

- **@Table**: Specify the table name for each model, ensuring the correct link to the database table.
- **@Column**: Define the mapping between model attributes and table columns, including options to specify the column name, data type, and more.
- **@PrimaryKey**: Designate the primary key for each model to accurately identify records in the database.
- **@Repository**: This annotation is used to mark classes as repositories, enabling them to be managed by the dependency injection system. Repositories are responsible for data access and persistence.
- **@Inject**: This annotation indicates that a field or method requires dependency injection. The FlexCore framework will automatically inject the required dependencies into these components. It's important to note that multiple implementations of an interface are not yet supported.

##### Example Usages

- **@Repository**

```java
@Repository
public class MyRepository extends CrudRepository<MyModel> {
    // Your custom repository code here
}
```

- **@Inject**

```java
public class MyService {
    @Inject
    private MyRepository myRepository;

    // Your service code here
}

public class Main {
    public static void main(String[] args) {

        IDependencyInjector dependencyInjector = new DependencyInjector();

        MyService service = new MyService();

       dependencyInjector.injectDependencies(service);
    }
}
```

### Added Features

1. #### slf4j Logger Framework

   We've incorporated the slf4j logging framework to enhance logging capabilities. This update allows for improved logging, which is crucial for debugging and monitoring applications.

2. #### Nested Dependency Injection

   FlexCore now supports nested dependency injection. You can inject dependencies at multiple levels, enhancing flexibility and modularity, though note that it may not be fully functional at this time.

3. #### Custom SQL Queries Using Builder Pattern
   We've introduced the ability to create custom SQL queries using the builder pattern. This feature provides an elegant and flexible way to construct and execute custom SQL queries. You can now perform operations such as SELECT, FROM, WHERE, JOIN, and more through a fluent interface.
