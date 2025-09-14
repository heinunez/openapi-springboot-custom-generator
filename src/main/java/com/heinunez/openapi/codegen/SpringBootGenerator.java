package com.heinunez.openapi.codegen;

import java.io.File;
import java.util.ArrayList;
import java.util.stream.Collectors;
import org.openapitools.codegen.SupportingFile;
import org.openapitools.codegen.languages.KotlinClientCodegen.DateLibrary;
import org.openapitools.codegen.languages.SpringCodegen;

public class SpringBootGenerator extends SpringCodegen {

  private static final String NAME = "springboot";

  public SpringBootGenerator() {
    super();
    this.projectFolder = "src/gen";
    this.reactive = true;
    this.interfaceOnly = true;
    this.useSpringBoot3 = true;
    this.skipDefaultInterface = true;
    this.library = SPRING_BOOT;
    this.useResponseEntity = false;
    this.openApiNullable = false;
    this.booleanGetterPrefix = "is";
    this.generateBuilders = true;
    this.useBeanValidation = true;
    this.performBeanValidation = true;
    this.useSpringBuiltInValidation = true;
    this.useTags = true;
    this.openApiNullable = false;
    this.additionalProperties.put(DATE_LIBRARY, DateLibrary.JAVA8.value);
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public void processOpts() {
    super.processOpts();

    this.apiPackage = this.basePackage + ".api";
    this.modelPackage = this.basePackage + ".model";
    this.configPackage = this.basePackage + ".config";

    // Remove the ApiUtil supporting file
    this.supportingFiles = this.supportingFiles
        .stream()
        .filter(s -> !s.getDestinationFilename().equals("ApiUtil.java"))
        .collect(Collectors.toCollection(ArrayList::new));

    // Add the main Class
    this.supportingFiles.add(
        new SupportingFile("openapi2SpringBoot.mustache",
            (this.sourceFolder + File.separator + this.basePackage).replace(".", File.separator),
            "OpenApiApplication.java")
    );

    this.supportingFiles.add(
        new SupportingFile("controllerAdvice.mustache",
            (this.sourceFolder + File.separator + this.configPackage).replace(".", File.separator),
            "DefaultControllerAdvice.java")
    );
    this.supportingFiles.add(
        new SupportingFile("retrofitCallAdapterFactory.mustache",
            (this.sourceFolder + File.separator + this.configPackage).replace(".", File.separator),
            "ReactorCallAdapterFactoryBuilder.java")
    );
    this.supportingFiles.add(
        new SupportingFile("jacksonConverterFactory.mustache",
            (this.sourceFolder + File.separator + this.configPackage).replace(".", File.separator),
            "JacksonAdapterFactoryBuilder.java")
    );
  }

  @Override
  public DocumentationProvider defaultDocumentationProvider() {
    return DocumentationProvider.NONE;
  }
}
