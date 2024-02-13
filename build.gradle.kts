plugins {
    id("java")
}

group = "ac.at.uibk.dps.nexa"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("dev.cel:cel:0.3.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")
    implementation("io.nats:jnats:2.17.3")
}

tasks.test {
    useJUnitPlatform()
}
