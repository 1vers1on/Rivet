plugins {
    java
    application
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
    mainClass = "net.ellie.bolt.Main"

    applicationDefaultJvmArgs = listOf(
        "-Djava.library.path=${project(":portaudio-jni").layout.buildDirectory.get()}/lib/main/release"
    )
}

tasks.register<Exec>("generateJniHeaders") {
    project.evaluationDependsOn(":portaudio-jni")

    val jniProjectDir = project(":portaudio-jni").projectDir
    val outputDir = file("$jniProjectDir/src/main/c/include")
    val portaudioSourceDir = file("src/main/java/net/ellie/portaudiojni")
    val portaudioSourceFiles = fileTree(portaudioSourceDir) {
        include("**/*.java")
    }

    dependsOn(tasks.classes)
    val classesDir = sourceSets.main.get().output.classesDirs.singleFile
    
    inputs.files(portaudioSourceFiles)
    outputs.dir(outputDir)

    val sourceFiles = portaudioSourceFiles.files.map { it.absolutePath }

    val javaCompiler = javaToolchains.compilerFor {
        languageVersion = JavaLanguageVersion.of(17)
    }
    val javacPath = javaCompiler.get().executablePath.asFile.absolutePath

    commandLine(
        javacPath,
        "-h", outputDir.absolutePath,
        "-d", classesDir.absolutePath,
        "-cp", sourceSets.main.get().compileClasspath.asPath + ":" + classesDir.absolutePath,
        *sourceFiles.toTypedArray()
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

tasks.jar {
    dependsOn(":portaudio-jni:linkRelease")
    manifest {
        attributes(
            "Main-Class" to "net.ellie.bolt.Main"
        )
    }
    
    // Include dependencies in the JAR (fat JAR)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}
