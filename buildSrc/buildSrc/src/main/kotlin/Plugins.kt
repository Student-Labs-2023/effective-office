/***
 * Object for declaration of project plugins.
 * Declaration rules:
 * - implementation - declare gradle plugin string with version
 * - plugin - declare short string to use in some `build.gradle` file
 */
object Plugins {

    object Android {
        const val implementation = "com.android.tools.build:gradle:7.3.1"
        const val plugin = "com.android.application"
    }

    object Shadow {
        const val implementation = "gradle.plugin.com.github.johnrengelman:shadow:7.1.2"
        const val plugin = "com.github.johnrengelman.shadow"
    }

    object Kotlin {
        const val implementation = "org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.20"
        const val plugin = "org.jetbrains.kotlin.multiplatform"
    }

    object Ktor {
        const val implementation = "io.ktor.plugin:plugin:2.3.1"
        const val plugin = "io.ktor.plugin"
    }

    object ApacheCompress {
        const val implementation = "org.apache.commons:commons-compress:1.21"
    }

    object MultiplatformCompose {
        const val implementation = "org.jetbrains.compose:compose-gradle-plugin:1.4.0"
        const val plugin = "org.jetbrains.compose"
    }

    object CocoaPods {
        const val implementation = "org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.20"
        const val plugin = "org.jetbrains.kotlin.native.cocoapods"
    }
    object Libres {
        const val implementation = "io.github.skeptick.libres:gradle-plugin:1.1.8"
        const val plugin = "io.github.skeptick.libres"
    }

    object BuildConfig {
        const val implementation = "com.github.gmazzo.buildconfig:plugin:4.0.4"
        const val plugin = "com.github.gmazzo.buildconfig"
    }

    object Serialization {
        const val implementation = "org.jetbrains.kotlin:kotlin-serialization:1.8.20"
        const val plugin = "org.jetbrains.kotlin.plugin.serialization"
    }
    object Parcelize {
        const val implementation = "org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.20"
        const val plugin = "org.jetbrains.kotlin.plugin.parcelize"
    }
}