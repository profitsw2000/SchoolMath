package ru.profitsw2000.schoolmath.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import org.koin.android.ext.android.inject
import ru.profitsw2000.schoolmath.R
import ru.profitsw2000.schoolmath.navigation.AppNavigator

class MainActivity : AppCompatActivity() {

    private val appNavigator: AppNavigator by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        appNavigator.bind(navController)
    }

    override fun onDestroy() {
        appNavigator.unbind()
        super.onDestroy()
    }
}