import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val keycloakVersion: String by project
val mapstructVersion: String by project
val openApiVersion: String by project

plugins {
    id("org.springframework.boot") version "2.5.2"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.5.20"
    kotlin("plugin.spring") version "1.5.20"
    kotlin("kapt") version "1.5.10"
}

group = "com.saleseaze.api"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.retry:spring-retry")
    implementation("org.springframework:spring-aspects")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("org.keycloak:keycloak-spring-boot-starter")
    implementation("org.keycloak:keycloak-admin-client:${keycloakVersion}")

    kapt("org.mapstruct:mapstruct-processor:${mapstructVersion}")
    implementation("org.mapstruct:mapstruct:${mapstructVersion}")

    implementation("org.springdoc:springdoc-openapi-ui:${openApiVersion}")
    implementation("org.springdoc:springdoc-openapi-security:${openApiVersion}")

    implementation("io.github.microutils:kotlin-logging:1.12.5")

    implementation("com.github.siyoon210:ogparser4j:1.0.1")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}


tasks.withType<Test> {
    useJUnitPlatform()
}


kapt {
    arguments {
        // Set Mapstruct Configuration options here
        // https://kotlinlang.org/docs/reference/kapt.html#annotation-processor-arguments
        // https://mapstruct.org/documentation/stable/reference/html/#configuration-options
        // arg("mapstruct.defaultComponentModel", "spring")
    }
}

dependencyManagement {
    imports {
        mavenBom("org.keycloak.bom:keycloak-adapter-bom:${keycloakVersion}")
    }
}