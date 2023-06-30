plugins {
    `kotlin-dsl`
}

repositories {
    mavenLocal()
    mavenCentral()
    maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://jitpack.io")
    jcenter()
    google()
    gradlePluginPortal()
}

dependencies {
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    implementation(Plugins.Android.implementation)
    implementation(Plugins.Shadow.implementation) // NOTE(radchenko): generates jar for server builds
    implementation(Plugins.Kotlin.implementation)
    implementation(Plugins.Kotlin.implementation)
    implementation(Plugins.Ktor.implementation)
    implementation(Plugins.ApacheCompress.implementation) // NOTE(radchenko): needs for `ktor` to `docker`
    implementation(Plugins.MultiplatformCompose.implementation)
    implementation(Plugins.Libres.implementation)
    implementation(Plugins.BuildConfig.implementation)
    implementation(Plugins.Serialization.implementation)
}

val rootDirProject = file("../")

kotlin {
    sourceSets.getByName("main").kotlin.srcDir("buildSrc/src/main/kotlin")
}
