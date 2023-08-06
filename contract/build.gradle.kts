plugins {
    id(Plugins.AndroidLib.plugin)
    id(Plugins.Kotlin.plugin)
    id(Plugins.Parcelize.plugin)
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
    dependencies{
        api(Dependencies.Ktor.Client.Core)
        implementation(Dependencies.KotlinxSerialization.json)
        implementation(Dependencies.KotlinxDatetime.kotlinxDatetime)
        implementation("io.ktor:ktor-client-cio:2.3.2")
    }
}