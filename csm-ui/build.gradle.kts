plugins {
    id("java")
    id("com.google.cloud.tools.jib") version "3.4.0"
}

group = "ac.at.uibk.dps.nexa"
version = "1.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_21

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":csm-core"))

    implementation("org.projectlombok:lombok:1.18.30")
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    testImplementation(platform("org.junit:junit-bom:5.10.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("com.github.docker-java:docker-java:3.3.4")
    implementation("com.github.docker-java:docker-java-transport-httpclient5:3.3.4")
}

jib {
    from {
        image = "ghcr.io/graalvm/jdk-community:21"
    }
    to {
        image = "csm-ui"
    }
    container {
        ports = listOf()
    }
}

tasks.test {
    useJUnitPlatform()
}
