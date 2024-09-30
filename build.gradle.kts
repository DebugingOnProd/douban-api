plugins {
    java
    id("io.quarkus")
    id("maven-publish")
}

repositories {
    mavenCentral()
    mavenLocal()
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/DebugingOnProd/douban-api")

            credentials {
                username =  System.getenv("GITHUB_ACTOR")
                password =  System.getenv("ACTION_SECRETS")
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project
val jsoupVersion: String by project
val lombokVersion: String by project
val commonsLang3Version: String by project
val guavaVersion: String by project

dependencies {
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    implementation("io.quarkus:quarkus-rest")
    implementation("io.quarkus:quarkus-rest-jackson")
    implementation("io.quarkus:quarkus-config-yaml")
    implementation("io.quarkus:quarkus-smallrye-openapi")
    implementation("io.quarkus:quarkus-arc")
    implementation("org.jsoup:jsoup:${jsoupVersion}")
    compileOnly("org.projectlombok:lombok:${lombokVersion}")
    implementation("org.apache.commons:commons-lang3:${commonsLang3Version}")
    implementation("com.google.guava:guava:${guavaVersion}")
    annotationProcessor("org.projectlombok:lombok:${lombokVersion}")
    testImplementation("io.quarkus:quarkus-junit5")
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
tasks.withType<GenerateModuleMetadata> {
    suppressedValidationErrors.add("enforced-platform")
}
