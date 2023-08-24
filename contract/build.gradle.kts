import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id(Plugins.AndroidLib.plugin)
    id(Plugins.Kotlin.plugin)
    id(Plugins.Parcelize.plugin)
    id(Plugins.BuildConfig.plugin)
    id(Plugins.Serialization.plugin)
}

val apiKey: String = gradleLocalProperties(rootDir).getProperty("apiKey")
buildConfig {
    buildConfigField("String", "apiKey", apiKey)
}

android {
    namespace = "band.effective.office.contract"
    compileSdk = 33

    defaultConfig {
        minSdk = 26
        targetSdk = 33
    }
    sourceSets["main"].apply {
        manifest.srcFile("src/androidMain/AndroidManifest.xml")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

}

kotlin {
    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(Dependencies.Ktor.Client.Core)
                implementation(Dependencies.KotlinxSerialization.json)
                implementation(Dependencies.KotlinxDatetime.kotlinxDatetime)
                implementation(Dependencies.Ktor.Client.CIO)
                implementation(Dependencies.Ktor.Client.Auth)
            }
        }
        val androidMain by getting {
            dependencies {

            }
        }
    }
}