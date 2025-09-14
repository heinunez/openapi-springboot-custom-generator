package com.heinunez.openapi.codegen;

import lombok.Getter;
import lombok.Setter;
import org.openapitools.codegen.CliOption;
import org.openapitools.codegen.config.GlobalSettings;
import org.openapitools.codegen.languages.JavaClientCodegen;
import org.openapitools.codegen.languages.KotlinClientCodegen.DateLibrary;

@Getter
@Setter
public class RetrofitClientGenerator extends JavaClientCodegen {

  private static final String NAME = "retrofit-client";
  protected String configPackage;
  protected String serverBasePackage;
  protected String clientId;
  protected String baseUrlProperty;

  public RetrofitClientGenerator() {
    super();
    this.projectFolder = "src/gen";
    this.serializationLibrary = SERIALIZATION_LIBRARY_JACKSON;
    this.additionalProperties.put(DATE_LIBRARY, DateLibrary.JAVA8.value);
    this.library = RETROFIT_2;
    this.useJakartaEe = true;
    this.openApiNullable = false;

    this.cliOptions.add(CliOption.newString("serverBasePackage", ""));
    this.cliOptions.add(CliOption.newString("clientId", ""));

    GlobalSettings.setProperty("modelDocs", "false");
    GlobalSettings.setProperty("apiDocs", "false");
    GlobalSettings.setProperty("modelTests", "false");
    GlobalSettings.setProperty("apiTests", "false");
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public void processOpts() {
    super.processOpts();
    this.convertPropertyToStringAndWriteBack("serverBasePackage", this::setServerBasePackage);
    this.convertPropertyToStringAndWriteBack("clientId", this::setClientId);

    this.apiPackage = this.serverBasePackage + ".clients." + this.clientId;
    this.modelPackage = this.apiPackage + ".model";
    this.configPackage = this.apiPackage + ".config";
    this.baseUrlProperty = buildBaseUrlProperty();

    this.supportingFiles.clear();
    this.documentationProvider = DocumentationProvider.NONE;
  }

  private String buildBaseUrlProperty() {
    var parts = this.apiPackage.split("\\.");
    return parts[parts.length - 1];
  }
}
