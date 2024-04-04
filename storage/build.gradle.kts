plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.library")
    id("maven-publish")
}

val dependenciesGroup: String by extra
val dependenciesName: String by extra
val dependenciesVersion: String by extra

group = dependenciesGroup
version = dependenciesVersion

kotlin {
    task("testClasses")
    withSourcesJar()
    androidTarget {
        publishLibraryVariants("release", "debug")
        publishLibraryVariantsGroupedByFlavor = true
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }
    jvm("jvm") {
        compilations.all {
            compilerOptions.configure {
                jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
            }
        }
    }

    sourceSets {
        val commonMain by getting
        commonMain.dependencies {
            api("com.github.DroidLin.common:common-jvm:1.0.3")
        }

        val jvmMain by getting
        jvmMain.dependencies {
            implementation("org.json:json:20240303")
        }

        val androidMain by getting
        androidMain.dependencies {
            implementation("com.github.DroidLin.common:common-android:1.0.3")
        }
    }
}

android {
    namespace = "com.android.dependencies.storage.android"
    compileSdk = 34
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    defaultConfig {
        minSdk = 24
    }
}

publishing {
    artifacts {
//        commonMainApi("common")
//        androidApis("common-android")
//        this.
    }
    repositories {
        maven {
            name = "repositoryLocalRepo"
            url = uri("${rootProject.projectDir}/repo")
        }
    }
}