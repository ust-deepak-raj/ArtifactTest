plugins {
    java
}

group = "org.sdet"
version = "1.0-SNAPSHOT"

val seleniumVersion = "4.45.0"
val junitVersion = "5.13.4"
val cucumberVersion = "7.31.0"
val testcontainersVersion = "1.21.3"
val flywayVersion = "11.11.2"
val mysqlVersion = "8.4.0"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {

    testImplementation("org.seleniumhq.selenium:selenium-java:$seleniumVersion")

    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")

    testImplementation("io.cucumber:cucumber-java:$cucumberVersion")
    testImplementation("io.cucumber:cucumber-junit-platform-engine:$cucumberVersion")
    testImplementation("io.cucumber:cucumber-picocontainer:$cucumberVersion")

    testImplementation("org.junit.platform:junit-platform-suite:1.13.4")

    testImplementation(platform("org.testcontainers:testcontainers-bom:$testcontainersVersion"))
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:mysql")

    testImplementation("org.flywaydb:flyway-core:$flywayVersion")
    testImplementation("org.flywaydb:flyway-mysql:$flywayVersion")

    testImplementation("com.mysql:mysql-connector-j:$mysqlVersion")

    // Allure
    testImplementation(platform("io.qameta.allure:allure-bom:2.29.1"))
    testImplementation("io.qameta.allure:allure-junit5")
    testImplementation("io.qameta.allure:allure-cucumber7-jvm")
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()

    systemProperty(
        "baseUrl",
        providers.gradleProperty("baseUrl")
            .orElse("http://localhost:5173")
            .get()
    )

    systemProperty(
        "allure.results.directory",
        layout.buildDirectory.dir("allure-results").get().asFile.absolutePath
    )

    systemProperty(
        "headless",
        providers.gradleProperty("headless")
            .orElse("false")
            .get()
    )

    systemProperty(
        "browser",
        providers.gradleProperty("browser")
            .orElse("chrome")
            .get()
    )

    systemProperty("cucumber.publish.quiet", "true")

    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks.test {
    useJUnitPlatform()
}