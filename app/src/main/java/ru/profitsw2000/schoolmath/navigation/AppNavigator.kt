package ru.profitsw2000.schoolmath.navigation

import androidx.navigation.NavController
import ru.profitsw2000.navigator.Navigator

class AppNavigator : Navigator {

    private var navController: NavController? = null

    fun bind(navController: NavController) {
        this.navController = navController
    }

    fun unbind() {
        this.navController = null
    }

    override fun navigateUp() {
        navController?.navigateUp()
    }

    override fun navigateToCategoryDetails() {
        navController?.navigate(ru.profitsw2000.mainscreen.R.id.action_main_to_category_details)
    }

}