import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    application
}

group = "io.github.changwook987"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io/")
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("net.dv8tion:JDA:5.0.0-alpha.20")
    implementation("com.github.minndevelopment:jda-ktx:0.9.5-alpha.19")
    implementation("com.github.hanshsieh:pixivj:1.2.3")
    implementation("ch.qos.logback:logback-classic:1.4.1")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("io.github.changwook987.Main")
}

task("stage").dependsOn("clean")