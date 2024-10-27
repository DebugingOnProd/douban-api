import java.net.URI

plugins {
    java
    id("io.quarkus")
}

repositories {
    mavenCentral()
    mavenLocal()
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project
val jsoupVersion: String by project
val lombokVersion: String by project
val commonsLang3Version: String by project
val guavaVersion: String by project
val quarkusLoggingLogbackVersion: String by project
val jacksonDateFormatVersion: String by project
val xstreamVersion: String by project

dependencies {
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    implementation("io.quarkus:quarkus-rest")
    implementation("io.quarkiverse.logging.logback:quarkus-logging-logback:${quarkusLoggingLogbackVersion}")
    implementation("io.quarkus:quarkus-rest-jackson")
    implementation("io.quarkus:quarkus-config-yaml")
    implementation("io.quarkus:quarkus-smallrye-openapi")
    implementation("io.quarkus:quarkus-arc")
    implementation("org.jsoup:jsoup:${jsoupVersion}")
    compileOnly("org.projectlombok:lombok:${lombokVersion}")
    implementation("org.apache.commons:commons-lang3:${commonsLang3Version}")
    implementation("com.google.guava:guava:${guavaVersion}")
    implementation("com.thoughtworks.xstream:xstream:${xstreamVersion}")
    annotationProcessor("org.projectlombok:lombok:${lombokVersion}")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:${jacksonDateFormatVersion}")
    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")
}

group = "org.lhq"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType<Test> {
    systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
}
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}

data class RepositoryData(val name: String, val url: URI)

tasks.register("showRepositories") {
    val repositoryData = repositories.withType<MavenArtifactRepository>().map { RepositoryData(it.name, it.url) }
    doLast {
        repositoryData.forEach {
            println("repository: ${it.name} ('${it.url}')")
        }
    }
}