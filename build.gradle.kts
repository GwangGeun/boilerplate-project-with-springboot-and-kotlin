import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.1.2"
    id("io.spring.dependency-management") version "1.1.2"
    kotlin("jvm") version "1.8.22"
    //	https://kotlinlang.org/docs/all-open-plugin.html#spring-support, https://cheese10yun.github.io/spring-kotlin/#null
    kotlin("plugin.spring") version "1.8.22"
    // https://kotlinlang.org/docs/no-arg-plugin.html#jpa-support
    kotlin("plugin.jpa") version "1.8.22"

    idea
}

object Versions {
    const val APP = "0.0.1-SNAPSHOT"
    const val LOGBACK_ENCODER = "7.2"
    const val SPRING_CLOUD_COORDINATES = "2022.0.4"

    const val KOTEST_COORDINATES = "5.6.2"
    const val KOTEST_EXTENSION = "1.1.3"
    const val MOCKK = "1.13.7"

    const val TEST_CONTAINER_COORDINATES = "1.19.0"
}

group = "com.example"
version = Versions.APP
java.sourceCompatibility = JavaVersion.VERSION_17

// ====== integration test  ======
// https://docs.gradle.org/current/userguide/java_testing.html#sec:configuring_java_integration_tests
// Can refactor with JVM Test Suite Plugin ( incubating mode )
sourceSets {
    create("integrationTest") {
        compileClasspath += sourceSets.main.get().output
        runtimeClasspath += sourceSets.main.get().output
    }
}

configurations {
    "integrationTestImplementation" {
        extendsFrom(configurations.testImplementation.get())
    }

    "integrationTestRuntimeOnly" {
        extendsFrom(configurations.testRuntimeOnly.get())
    }
}

repositories {
    mavenCentral()
}

// Lazy loading (proxy) will not be working without below config. ( without below config - eager loading )
allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}

dependencies {
    // [ boot ]
    implementation("org.springframework.boot:spring-boot-starter-web")
//    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    // [ base ]
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // [ logging ] - https://spring.io/blog/2022/10/12/observability-with-spring-boot-3
    // Need this one for LogbackValve config
    implementation("ch.qos.logback:logback-access")
    // https://github.com/logfellow/logstash-logback-encoder#composite-encoderlayout
    implementation("net.logstash.logback:logstash-logback-encoder:${Versions.LOGBACK_ENCODER}")
    implementation("io.micrometer:micrometer-tracing-bridge-brave")

    // Note - dependencyManagement == platform
    // [ Spring Cloud ] - https://spring.io/projects/spring-cloud
    implementation(platform("org.springframework.cloud:spring-cloud-dependencies:${Versions.SPRING_CLOUD_COORDINATES}"))
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")

    // [ Database ]
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("com.h2database:h2")
    // https://github.com/spring-projects/spring-boot/releases/tag/v2.7.8
    runtimeOnly("com.mysql:mysql-connector-j")

    // ==============================================================================
    // [ test ]
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    // https://kotest.io/docs/extensions/spring.html
    testImplementation("io.kotest.extensions:kotest-extensions-spring:${Versions.KOTEST_EXTENSION}")

    // kotest
    testImplementation(platform("io.kotest:kotest-bom:${Versions.KOTEST_COORDINATES}"))
    testImplementation("io.kotest:kotest-runner-junit5")
    testImplementation("io.kotest:kotest-assertions-core")
    // mockk
    testImplementation("io.mockk:mockk:${Versions.MOCKK}")

    // testContainer
    testImplementation(platform("org.testcontainers:testcontainers-bom:${Versions.TEST_CONTAINER_COORDINATES}"))
    testImplementation("org.testcontainers:mysql")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.junit.jupiter:junit-jupiter")


//    integrationTestImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs += "-Xjsr305=strict"
            jvmTarget = "17"
        }
    }
    withType<Test> {
        useJUnitPlatform()
    }
    register<Test>("integrationTest") {
        description = "Runs integration tests."
        group = "verification"

        testClassesDirs = sourceSets["integrationTest"].output.classesDirs
        classpath = sourceSets["integrationTest"].runtimeClasspath
        shouldRunAfter("test")

        useJUnitPlatform()

        // TODO : https://github.com/radarsh/gradle-test-logger-plugin
        testLogging {
            exceptionFormat = TestExceptionFormat.FULL
            showExceptions = true
            showStandardStreams = true
            events("passed", "failed", "skipped")
        }
    }
    check { dependsOn("integrationTest") }
}

// https://docs.gradle.org/current/userguide/idea_plugin.html#sec:idea_identify_additional_source_sets
idea {
    module {
        testSources.from(sourceSets["integrationTest"].java.srcDirs)
    }
}
