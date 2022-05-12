import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.31"
}

group = "me.pipe"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.5.31")

    // https://mvnrepository.com/artifact/org.mariadb.jdbc/mariadb-java-client
    implementation("org.mariadb.jdbc:mariadb-java-client:3.0.4")

    // https://mvnrepository.com/artifact/ch.qos.logback/logback-classic
    implementation("ch.qos.logback:logback-classic:1.2.11")

    // https://mvnrepository.com/artifact/io.github.cdimascio/dotenv-kotlin
    implementation("io.github.cdimascio:dotenv-kotlin:6.2.2")

    implementation("org.jetbrains.exposed:exposed-core:0.38.2")
    implementation("org.jetbrains.exposed:exposed-dao:0.38.2")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.38.2")
    implementation("org.jetbrains.exposed:exposed-java-time:0.38.2")

    // https://mvnrepository.com/artifact/org.kohsuke/wordnet-random-name
    implementation("org.kohsuke:wordnet-random-name:1.5")


}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}