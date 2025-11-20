plugins {
    java
    application
}

dependencies {
    implementation("org.openimaj:JTransforms:1.3.10")
    // Do not depend on the JNI project directly here
    // implementation(project(":portaudio-jni"))
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

application {
    mainClass = "org.e2k.Rivet"

    applicationDefaultJvmArgs = listOf(
        "-Djava.library.path=${project(":portaudio-jni").layout.buildDirectory.get()}/lib/main/release"
    )
}

tasks.register<Exec>("generateJniHeaders") {
    project.evaluationDependsOn(":portaudio-jni")

    val jniProjectDir = project(":portaudio-jni").projectDir
    val outputDir = file("$jniProjectDir/src/main/c/include")

    dependsOn(tasks.classes)
    val classesDir = sourceSets.main.get().output.classesDirs.singleFile
    outputs.dir(outputDir)

    commandLine(
        "javac",
        "-h", outputDir.absolutePath,
        "-d", classesDir.absolutePath,
        "-cp", sourceSets.main.get().compileClasspath.asPath,
        "src/main/java/net/ellie/portaudiojni/PortAudioJNI.java"
    )
}

tasks.named("assemble") {
    dependsOn(":portaudio-jni:linkRelease")
}

tasks.named("generateJniHeaders") {
    finalizedBy(":portaudio-jni:linkRelease")
}

tasks.named("run") {
    dependsOn(":portaudio-jni:linkRelease")
}
