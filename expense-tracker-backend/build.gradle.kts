import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java")
    id("groovy")
    id("org.springframework.boot") version "2.7.18"
    id("io.spring.dependency-management") version "1.1.4"
    id("org.openapi.generator") version "7.0.1"
    id("com.bmuschko.docker-spring-boot-application") version "9.3.4"
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.jpa") version "1.9.23"
    kotlin("plugin.spring") version "1.9.23"
}

group = rootProject.group
version = rootProject.version

val javaVersion: JavaLanguageVersion by rootProject.extra
val apiSchema: String by rootProject.extra

tasks.withType<KotlinCompile> {
    kotlinOptions.freeCompilerArgs += "-Xjsr305=strict"
}

kotlin {
    jvmToolchain {
        languageVersion.set(javaVersion)
    }
}

sourceSets.create("integrationTest") {
    groovy {
        groovy.srcDir("$projectDir/src/integration-test/groovy")
        resources.srcDir("$projectDir/src/integration-test/resources")
        compileClasspath += sourceSets.main.get().output
        runtimeClasspath += sourceSets.main.get().output
    }
}

val integrationTestImplementation: Configuration by configurations.getting {
    val testImplementation: Configuration by configurations
    extendsFrom(testImplementation)
}

val integrationTestRuntimeOnly: Configuration by configurations.getting {
    val testRuntimeOnly: Configuration by configurations
    extendsFrom(testRuntimeOnly)
}

dependencies {
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // Spring
    implementation("org.springframework.boot:spring-boot-starter")

    // Web
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // Database
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("io.github.perplexhub:rsql-jpa-spring-boot-starter:5.1.1")
    runtimeOnly("org.postgresql:postgresql:42.7.0")
    runtimeOnly("org.liquibase:liquibase-core:4.17.0")

    // Kotlin Support
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // Openapi
    implementation("io.swagger.core.v3:swagger-annotations:2.2.16")
//    implementation("io.swagger.parser.v3:swagger-parser:2.1.16")

    // UI
//    runtimeOnly(project(":expense-tracker-frontend"))

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.spockframework:spock-spring:2.3-groovy-3.0")
    testImplementation("org.codehaus.groovy:groovy-json:3.0.13")

    // Integration Test
    integrationTestRuntimeOnly("com.h2database:h2:2.1.214")
}

tasks.test {
    useJUnitPlatform()
}

val integrationTest = tasks.register<Test>("integrationTest") {
    description = "Run integration tests."
    group = "verification"

    testClassesDirs = sourceSets["integrationTest"].output.classesDirs
    classpath = sourceSets["integrationTest"].runtimeClasspath

    shouldRunAfter("test")
    useJUnitPlatform()
}

tasks.check {
    dependsOn(integrationTest)
}

tasks.bootJar {
    manifest.attributes(
        "Implementation-Title" to "Expense Tracker",
        "Implementation-Version" to project.version
    )
}

tasks.clean {
    delete += listOf(
        "$projectDir/.openapi-generator",
        "$projectDir/src/main/kotlin/matthias/expense_tracker/api"
    )
}

openApiValidate {
    inputSpec.set(apiSchema)
}

openApiGenerate {
    generatorName.set("kotlin-spring")
    library.set("spring-boot")
    inputSpec.set(apiSchema)
    outputDir.set("$projectDir")
    globalProperties.set(
        mapOf(
            "supportingFiles" to "false",
            "modelDocs" to "false",
            "models" to "",
            "apis" to ""
        )
    )
    configOptions.set(
        mapOf(
            "title" to rootProject.name,
            "artifactId" to rootProject.name,
            "packageName" to "matthias.expense_tracker.api",
            "useBeanValidation" to "false",
            "useTags" to "true",
            "dateLibrary" to "java8",
            "serializableModel" to "true",
            "interfaceOnly" to "true",
            "skipDefaultInterface" to "true",
            "useSpringBoot3" to "false"
        )
    )
    typeMappings.set(
        mapOf(
            "DateTime" to "java.time.Instant"
        )
    )
}

docker {
    springBootApplication {
        baseImage.set("openjdk:21-oracle")
        ports.set(listOf(8080))
        jvmArgs.set(listOf("-Xms256m", "-Xmx512m"))
        images.set(
            listOf(
                "${rootProject.name}:${rootProject.version}",
                "${rootProject.name}:latest"
            )
        )
    }
}
