# OpenAPI Spring Boot Generator

Custom OpenAPI Generator plugin providing specialized code generators for Spring Boot reactive servers and Retrofit clients.

## Overview

This plugin extends the OpenAPI Generator with two custom generators:

- **`springboot`**: Generates reactive Spring Boot 3 server code with WebFlux
- **`retrofit-client`**: Generates Retrofit 2 client libraries with Jackson serialization

## Building the Plugin

```bash
# Clean and compile
mvn clean compile

# Run tests
mvn test

# Package the plugin JAR
mvn package
```

The packaged JAR will be available at `target/springboot-generator-1.0.0.jar`.

## Installation

After building, install the plugin JAR to make it available to OpenAPI Generator:

```bash
# Add to your local Maven repository
mvn install

# Or copy to your OpenAPI Generator plugins directory
cp target/springboot-generator-1.0.0.jar /path/to/openapi-generator/modules/
```

## Usage

### Maven Plugin Integration

For existing Maven projects, you can integrate the generators using the OpenAPI Generator Maven plugin:

#### pom.xml Configuration

Add the following to your project's `pom.xml`:

```xml
<build>
  <plugins>
    <plugin>
      <groupId>org.openapitools</groupId>
      <artifactId>openapi-generator-maven-plugin</artifactId>
      <version>7.15.0</version>
      <dependencies>
        <!-- Add this plugin as a dependency -->
        <dependency>
          <groupId>com.heinunez.openapi</groupId>
          <artifactId>springboot-generator</artifactId>
          <version>1.0.0</version>
        </dependency>
      </dependencies>
      <executions>
        <!-- Spring Boot Server Generation -->
        <execution>
          <id>generate-server</id>
          <goals>
            <goal>generate</goal>
          </goals>
          <configuration>
            <inputSpec>${project.basedir}/src/main/resources/openapi.yaml</inputSpec>
            <generatorName>springboot</generatorName>
            <configOptions>
              <basePackage>com.example.petstore</basePackage>
            </configOptions>
          </configuration>
        </execution>

        <!-- Retrofit Client Generation -->
        <execution>
          <id>generate-client</id>
          <goals>
            <goal>generate</goal>
          </goals>
          <configuration>
            <inputSpec>${project.basedir}/src/main/resources/openapi.yaml</inputSpec>
            <generatorName>retrofit-client</generatorName>
            <configOptions>
              <serverBasePackage>com.example</serverBasePackage>
              <clientId>petstore</clientId>
            </configOptions>
          </configuration>
        </execution>
      </executions>
    </plugin>
  </plugins>
</build>
```

#### Generate Code

```bash
# Generate code during build
mvn generate-sources

# Or run the full build
mvn clean compile
```

### SpringBoot Generator

Generates reactive Spring Boot 3 server code with WebFlux support.

#### Basic Usage

```bash
openapi-generator-cli generate \
  -i petstore.yaml \
  -g springboot \
  -o ./generated-server \
  --package-name com.example.petstore
```

#### Features

- **Reactive WebFlux**: Uses Spring WebFlux for reactive programming
- **Interface-only Controllers**: Generates controller interfaces without implementation
- **Bean Validation**: Built-in validation using Jakarta Bean Validation
- **Spring Boot 3**: Targets Spring Boot 3 with modern Java features
- **Custom Configuration**: Includes exception handling and converter factories

#### Generated Structure

```
generated-server/
├── src/gen/
│   └── com/example/petstore/
│       ├── OpenApiApplication.java           # Main Spring Boot application
│       ├── api/                              # Controller interfaces
│       │   └── PetApi.java
│       ├── model/                            # Data models
│       │   └── Pet.java
│       └── config/                           # Configuration classes
│           ├── DefaultControllerAdvice.java  # Global exception handler
│           ├── ReactorCallAdapterFactoryBuilder.java
│           └── JacksonAdapterFactoryBuilder.java
```

#### Configuration Options

All standard SpringCodegen options are supported, plus:
- `basePackage`: Base package for generated code (default: `org.openapitools`)
- Uses Java 8 date/time API by default

### Retrofit Client Generator

Generates Retrofit 2 client libraries for consuming REST APIs.

#### Basic Usage

```bash
openapi-generator-cli generate \
  -i petstore.yaml \
  -g retrofit-client \
  -o ./generated-client \
  --additional-properties=serverBasePackage=com.example,clientId=petstore
```

#### Required Parameters

- `serverBasePackage`: Base package for the generated client
- `clientId`: Unique identifier for this client (used in package naming)

#### Features

- **Retrofit 2**: Modern HTTP client library for Android and Java
- **Jackson Serialization**: JSON serialization using Jackson
- **Jakarta EE**: Uses Jakarta EE annotations
- **No Documentation**: Optimized for code generation without docs/tests
- **Configurable Packages**: Flexible package structure based on parameters

#### Generated Structure

```
generated-client/
└── src/gen/
    └── com/example/clients/petstore/
        ├── api/                    # Retrofit service interfaces
        │   └── PetApi.java
        ├── model/                  # Data models
        │   └── Pet.java
        └── config/                 # Configuration (minimal)
```

#### Package Structure

The generated packages follow this pattern:
- **API Package**: `{serverBasePackage}.clients.{clientId}`
- **Model Package**: `{serverBasePackage}.clients.{clientId}.model`
- **Config Package**: `{serverBasePackage}.clients.{clientId}.config`

## Development

### Running Tests

The project includes a test harness for debugging generators:

```bash
# Run the test (generates code to out/my-codegen)
mvn test -Dtest=SpringBootGeneratorTest#launchCodeGenerator
```

### Debugging

1. Set breakpoints in the generator classes
2. Run `SpringBootGeneratorTest#launchCodeGenerator` in debug mode
3. Step through the code generation process
4. Inspect generated output in `out/my-codegen`

### Custom Templates

Templates are located in `src/main/resources/`:
- **Spring Boot**: `JavaSpring/libraries/spring-boot/*.mustache`
- **Retrofit**: `Java/libraries/retrofit2/*.mustache`

Modify these templates to customize the generated code structure and content.

## Requirements

- Java 21+
- Maven 3.6+
- OpenAPI Generator 7.15.0+

## License

This project follows the same license as the OpenAPI Generator project.