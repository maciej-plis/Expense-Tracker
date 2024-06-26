plugins {
    id("java-library")
}

val javaVersionNumber: String by project
val javaVersion by extra { JavaLanguageVersion.of(javaVersionNumber) }

val openApiSchemaName by extra { "schema.yaml" }
val openApiSchemaRootInput by extra { "$rootDir/openApi/$openApiSchemaName" }
val openApiSchemaOutput by extra { "$rootDir/build/$openApiSchemaName" }
val openApiSchemaComponents by extra { listOf("$rootDir/openApi/paths", "$rootDir/openApi/components") }

val generateSchema by tasks.registering(Exec::class) {
    group = "openapi"
    description = "Generate complete openapi schema"
    inputs.file(openApiSchemaRootInput)
    openApiSchemaComponents.forEach { inputs.dir(it) }
    outputs.file(openApiSchemaOutput)

    workingDir(projectDir)
    commandLine("npx", "@redocly/cli", "bundle", openApiSchemaRootInput, "-o", openApiSchemaOutput)
}

allprojects {
    group = rootProject.group
    version = rootProject.version
}
