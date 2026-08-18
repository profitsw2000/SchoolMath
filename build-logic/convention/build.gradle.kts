import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.`kotlin-dsl`

plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.kotlin.ksp.gradlePlugin)
}

gradlePlugin {
    plugins {

    }
}