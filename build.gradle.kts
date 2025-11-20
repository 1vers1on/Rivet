plugins {
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.openimaj:JTransforms:1.3.10")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

application {
    mainClass = "org.e2k.Rivet"
}
