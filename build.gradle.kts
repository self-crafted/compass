plugins {
    alias(libs.plugins.shadowJar)
    java
}

group = project.properties["maven_group"]!!
version = project.properties["maven_version"]!!

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
}

dependencies {
    compileOnly(libs.minestom)
    implementation(libs.minimessage)
    implementation(libs.toml)
}

tasks {
    processResources {
        filesMatching("extension.json") {
            expand(
                mapOf(
                    "Name" to rootProject.name,
                    "version" to version
                )
            )
        }
    }

    shadowJar {
        archiveBaseName.set(rootProject.name)
        archiveClassifier.set("")
        archiveVersion.set(project.version.toString())
    }

    test {
        useJUnitPlatform()
    }

    build {
        dependsOn(shadowJar)
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}