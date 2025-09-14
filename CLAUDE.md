# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a custom OpenAPI Generator plugin that provides two specialized code generators:
- **SpringBootGenerator**: Extends SpringCodegen to generate reactive Spring Boot server code with WebFlux
- **RetrofitClientGenerator**: Extends JavaClientCodegen to generate Retrofit client libraries

## Build and Development Commands

- **Build project**: `mvn clean compile`
- **Run tests**: `mvn test`
- **Package JAR**: `mvn package`
- **Clean build artifacts**: `mvn clean`

## Architecture

### Core Components

- **SpringBootGenerator** (`src/main/java/com/heinunez/openapi/codegen/SpringBootGenerator.java`):
  - Configured for reactive Spring Boot 3 with WebFlux
  - Generates interface-only controllers with bean validation
  - Uses custom Mustache templates in `src/main/resources/JavaSpring/libraries/spring-boot/`
  - Removes ApiUtil and adds custom supporting files (OpenApiApplication, DefaultControllerAdvice, etc.)

- **RetrofitClientGenerator** (`src/main/java/com/heinunez/openapi/codegen/RetrofitClientGenerator.java`):
  - Generates Retrofit 2 client code with Jackson serialization
  - Uses Jakarta EE annotations
  - Configurable with `serverBasePackage` and `clientId` parameters
  - No documentation or test generation (disabled via GlobalSettings)

### Template System

Custom Mustache templates located in:
- `src/main/resources/JavaSpring/libraries/spring-boot/` - Spring Boot generator templates
- `src/main/resources/Java/libraries/retrofit2/` - Retrofit client templates

Templates include:
- `openapi2SpringBoot.mustache` - Main Spring Boot application class
- `controllerAdvice.mustache` - Global exception handler
- `jacksonConverterFactory.mustache` - Jackson configuration
- `retrofitCallAdapterFactory.mustache` - Reactor call adapter setup

## Testing

- Test class: `SpringBootGeneratorTest.java` provides a debuggable test harness
- Uses petstore.yaml as sample OpenAPI spec for testing
- Outputs generated code to `out/my-codegen` directory for inspection

## Maven Configuration

- Java 21 target/source
- OpenAPI Generator 7.15.0
- JUnit 5.10.2 for testing
- Maven Surefire configured with parallel test execution and increased memory