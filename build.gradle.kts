plugins {
    kotlin("jvm") version "2.1.0"
    application
}

group = "com.codekopf"
version = "0.0.1-SNAPSHOT"

application {
    mainClass.set("com.codekopf.hotstocks.HotStocksCrawlerKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.seleniumhq.selenium:selenium-java:4.27.0")
    implementation("org.apache.commons:commons-csv:1.12.0")
    implementation("commons-io:commons-io:2.18.0")
}

kotlin {
    jvmToolchain(21)
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.codekopf.hotstocks.HotStocksCrawlerKt"
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}
