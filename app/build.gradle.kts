@file:OptIn(ExperimentalEncodingApi::class)

import buildlogic.gitHash
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

plugins {
    id("com.android.application")
    kotlin("plugin.serialization")
    kotlin("plugin.compose")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
    id("org.jlleitschuh.gradle.ktlint")
}

ktlint {
    android.set(true)
    outputToConsole.set(true)
    enableExperimentalRules.set(true)
    filter {
        exclude("**/generated/**")
        exclude("**/build/**")
    }
}

ksp {
    // Fix cache invalidation by stabilizing inputs
    arg("room.schemaLocation", "$projectDir/schemas")
    arg("room.incremental", "true")
    // Stabilize logLevel to prevent cache invalidation
    arg("logging.level", "WARN")
}

android {
    namespace = "com.github.zly2006.zhihu"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.github.zly2006.zhihu"
        minSdk = 27
        targetSdk = 35
        versionCode = property("app.versionCode").toString().toIntOrNull() ?: 1
        versionName = property("app.versionName").toString()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    flavorDimensions += "version"
    productFlavors {
        create("full") {
            dimension = "version"
            buildConfigField("boolean", "IS_LITE", "false")
        }
        create("lite") {
            dimension = "version"
            buildConfigField("boolean", "IS_LITE", "true")
            applicationIdSuffix = ".lite"
//            versionNameSuffix = "-lite"
        }
    }

    androidResources {
        @Suppress("UnstableApiUsage")
        localeFilters += listOf("en", "zh")
    }

    testOptions {
        unitTests {
            isReturnDefaultValues = true
        }
    }

    signingConfigs {
        if (System.getenv("signingKey") != null) {
            register("env") {
                storeFile =
                    file("zhihu.jks").apply {
                        writeBytes(Base64.decode(System.getenv("signingKey")))
                    }
                storePassword = System.getenv("keyStorePassword")
                keyAlias = System.getenv("keyAlias")
                keyPassword = System.getenv("keyPassword")
            }
        }
    }
    buildTypes {
        val gitHash = gitHash(rootProject.projectDir)
        debug {
            buildConfigField("String", "GIT_HASH", "\"$gitHash\"")
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            buildConfigField("String", "GIT_HASH", "\"$gitHash\"")
            if (System.getenv("signingKey") != null) {
                signingConfig = signingConfigs["env"]
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_24
        targetCompatibility = JavaVersion.VERSION_24
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
        compose = true
    }

    packaging {
        resources {
            excludes +=
                listOf(
                    "META-INF/DEPENDENCIES",
//                    "META-INF/*.version",
                    "META-INF/**/LICENSE",
                    "META-INF/**/LICENSE.txt",
                    "META-INF/proguard/*",
                    "**.kotlin_module",
                    "kotlin-tooling-metadata.json",
                    "DebugProbesKt.bin",
//                    "META-INF/*.kotlin_module",
                )
        }
    }

    androidComponents {
        beforeVariants(selector().all()) { variantBuilder ->
            val flavorName = variantBuilder.flavorName
            if (variantBuilder.buildType == "release") {
                val minify =
                    when (flavorName) {
                        "lite" -> true
                        else -> false
                    }
                variantBuilder.isMinifyEnabled = minify
                variantBuilder.shrinkResources = minify
            }
        }
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xdebug")
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_24)
    }
}

val ktor = "3.4.1"
val coil = "3.4.0"
dependencies {
    implementation("androidx.preference:preference:1.2.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation("io.ktor:ktor-client-core-jvm:$ktor")
    implementation("io.ktor:ktor-client-android:$ktor")
    implementation("io.ktor:ktor-client-content-negotiation-jvm:$ktor")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1")
    //noinspection GradleDependency
    implementation("androidx.browser:browser:1.8.0")

    implementation("io.coil-kt.coil3:coil-compose:$coil")
    implementation("io.coil-kt.coil3:coil-network-ktor3-android:$coil")
    implementation("io.coil-kt.coil3:coil-gif:$coil")
    implementation("io.coil-kt.coil3:coil-svg:$coil")

    implementation("com.google.android.material:material:1.13.0")
    implementation("com.materialkolor:material-kolor:4.1.1")

    // MIUIX - Xiaomi HyperOS Design Style Components
    // https://github.com/compose-miuix-ui/miuix
    val miuixVersion = "0.9.0"
    implementation("top.yukonga.miuix.kmp:miuix-ui-android:$miuixVersion")
    implementation("top.yukonga.miuix.kmp:miuix-icons-android:$miuixVersion")
    implementation("top.yukonga.miuix.kmp:miuix-preference-android:$miuixVersion")
    implementation("top.yukonga.miuix.kmp:miuix-shapes-android:$miuixVersion")
    // miuix-blur 需要 minSdk 31，暂不启用
    // implementation("top.yukonga.miuix.kmp:miuix-blur-android:$miuixVersion")

    implementation("org.jsoup:jsoup:1.22.1")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")

    // ZXing for QR code scanning
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")

    implementation("androidx.core:core-ktx:1.17.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.10.0")

    // Activity Embedding (平行视界)
    implementation("androidx.window:window:1.3.0")
    implementation("androidx.startup:startup-runtime:1.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.10.0")
    //noinspection GradleDependency
    implementation("androidx.navigation:navigation-ui-ktx:2.9.2")
    implementation("androidx.webkit:webkit:1.14.0")
    implementation("androidx.activity:activity-compose:1.12.1")
    implementation(platform("androidx.compose:compose-bom:2025.12.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.material:material-icons-extended")
    //noinspection GradleDependency
    implementation("androidx.navigation:navigation-compose:2.9.2")
    //noinspection GradleDependency
    implementation("androidx.compose.animation:animation:1.8.2")
    //noinspection GradleDependency
    implementation("androidx.compose.animation:animation-core:1.8.2")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.room:room-common-jvm:2.8.4")
    implementation("androidx.room:room-runtime-android:2.8.4")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.10.0")
    annotationProcessor("androidx.room:room-compiler:2.8.4")
    ksp("androidx.room:room-compiler:2.8.4")
    "fullImplementation"(project(":sentence_embeddings"))
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-tooling-preview")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    implementation("com.github.chrisbanes:PhotoView:2.0.0") {
        exclude(group = "com.android.support")
    }

    // HanLP for Chinese NLP
    "fullImplementation"("com.hankcs:hanlp:portable-1.8.4")
//    implementation("com.halilibo.compose-richtext:richtext-ui-material3-android:1.0.0-alpha03")
//    implementation("com.halilibo.compose-richtext:richtext-markdown-android:1.0.0-alpha03")

    testImplementation("junit:junit:4.13.2")
    testImplementation("io.ktor:ktor-client-cio:$ktor")
    testImplementation("io.ktor:ktor-client-content-negotiation:$ktor")
    testImplementation("io.ktor:ktor-serialization-kotlinx-json:$ktor")
    //noinspection GradleDependency
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")
}
