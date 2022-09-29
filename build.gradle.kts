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

    implementation(kotlin("stdlib"))
    implementation("net.dv8tion:JDA:5.0.0-alpha.20")
    implementation("com.github.minndevelopment:jda-ktx:0.9.5-alpha.19")
    implementation("com.github.hanshsieh:pixivj:1.2.3")
    implementation("net.sf.trove4j:trove4j:3.0.3")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.4")
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.6")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.create<Jar>("discordJar") {
    from(sourceSets["main"].output)

    manifest {
        attributes["Main-Class"] = "io.github.changwook987.Main"
    }

    archiveBaseName.set("DiscordKotlin")
    archiveVersion.set("")

    from(configurations.compileClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    doLast {
        copy {
            from(archiveFile)
            into(rootDir)
        }
    }
}

task("stage").dependsOn("discordJar", "clean")