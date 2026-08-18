import org.gradle.api.Plugin
import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class MainApplicationGradlePlugin: Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            with(project.pluginManager) {
                apply("com.android.application")
                apply("com.google.devtools.ksp")
            }

            extensions.configure<ApplicationExtension> {

            }
        }
    }

    private fun ApplicationExtension.configureDefaultConfig(project: Project) {
        with(project) {
            defaultConfig {

            }
        }
    }
}