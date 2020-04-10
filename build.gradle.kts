import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.21"
}

group = "org.example"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.github.microutils:kotlin-logging:1.7.9")
    implementation("org.slf4j:slf4j-simple:1.7.29")
    implementation("javax.servlet:javax.servlet-api:3.1.0")
    implementation("javax.servlet:jstl:1.2")
    implementation("org.apache.tomcat.embed:tomcat-embed-core:8.0.15")
    implementation("org.apache.tomcat.embed:tomcat-embed-logging-juli:8.0.15")
    implementation("org.apache.tomcat.embed:tomcat-embed-jasper:8.0.15")
    testImplementation("org.junit.jupiter:junit-jupiter:5.6.0")
}
repositories {
    mavenCentral()
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

/*
* https://medium.com/@jonashavers/how-to-use-junit-5-with-gradle-fb7c5c3286cc
* */
tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}