import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import gradleplugins.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.getByType

class AndroidUiTestConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            extensions.findByType<LibraryExtension>()?.apply {
                testOptions {
                    unitTests {
                        isIncludeAndroidResources = true
                    }
                }
            }

            dependencies {
                add("debugImplementation", libs.findLibrary("androidx-fragment-testing-manifest").get())

                add("testImplementation", libs.findLibrary("robolectric").get())
                add("testImplementation", libs.findLibrary("androidx-test").get())
                add("testImplementation", libs.findLibrary("junit").get())
                add("testImplementation", libs.findLibrary("androidx-junit").get())
                add("testImplementation", libs.findLibrary("androidx-test-ext-junit").get())
                add("testImplementation", libs.findLibrary("androidx-espresso-core").get())
                add("testImplementation", libs.findLibrary("androidx-espresso-contrib").get())
                add("testImplementation", libs.findLibrary("androidx-fragment-testing").get())
                add("testImplementation", libs.findLibrary("androidx-navigation-testing").get())
                add("testImplementation", libs.findLibrary("io-mockk").get())
                add("testImplementation", libs.findLibrary("koin-test").get())
                add("testImplementation", libs.findLibrary("koin-test-junit4").get())
                add("testImplementation", libs.findLibrary("kotlinx-coroutines-test").get())
                add("testImplementation", libs.findLibrary("room-testing").get())

            }
        }
    }
}