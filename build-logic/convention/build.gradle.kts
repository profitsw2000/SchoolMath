import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.`kotlin-dsl`

plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.kotlin.ksp.gradlePlugin)
    implementation("org.jetbrains.kotlin.plugin.serialization:org.jetbrains.kotlin.plugin.serialization.gradle.plugin:2.3.20")
}

gradlePlugin {
    plugins {
        register("mainApplicationGradlePlugin") {
            id = libs.plugins.sm.main.app.gradle.plugin.get().pluginId
            implementationClass = "MainApplicationGradlePlugin"
        }
        register("mainLibraryGradlePlugin") {
            id = libs.plugins.sm.main.lib.gradle.plugin.get().pluginId
            implementationClass = "MainLibraryGradlePlugin"
        }
    }
}