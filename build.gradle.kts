// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    /*val compose_version by extra("1.0.0-beta07")
    val exposedVersion by extra("0.31.1")*/
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.0-beta04")
        val kotlin_version = "1.5.10"
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
        classpath("org.jetbrains.kotlin:kotlin-serialization:$kotlin_version")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}