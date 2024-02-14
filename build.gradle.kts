import java.nio.file.Files
import java.nio.file.StandardCopyOption

plugins {
    base
    id("org.sonarqube") version "4.4.1.3373"
    id("jacoco-report-aggregation")
    id("jacoco")
}

repositories {
    mavenCentral()
}

dependencies {
    jacocoAggregation(project(":csm-core"))
    jacocoAggregation(project(":csm-ui"))
}

reporting {
    reports {
        val jacocoTestReport by creating(JacocoCoverageReport::class) {
            testType.set(TestSuiteType.UNIT_TEST)
        }
    }
}

tasks.named("check") {
    dependsOn(tasks.named<JacocoReport>("jacocoTestReport"))
}

tasks.withType<JacocoReport> {
    doLast {
        val xmlReportFile = reports.xml.outputLocation

        val targetDir = "${rootProject.buildDir}/../target/site/jacoco"
        val targetFile = "$targetDir/jacoco.xml"

        project.file(targetDir).mkdirs()
        Files.copy(xmlReportFile.asFile.get().toPath(), project.file(targetFile).toPath(), StandardCopyOption.REPLACE_EXISTING)
    }
}

sonarqube {
    properties {
        property("sonar.projectKey", "virtuoso_nexa")
        property("sonar.organization", "virtuoso")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.coverage.jacoco.xmlReportPaths", "${rootProject.buildDir}\\reports\\jacoco\\jacocoTestReport\\jacocoTestReport.xml")
    }
}
