package com.libreria.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.libreria.app.data.remote.FirebaseService
import com.libreria.app.data.repository.LibreriaRepository
import com.libreria.app.ui.screens.*
import com.libreria.app.vm.*
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AppEntry()
            }
        }
    }
}
@Composable
fun AppEntry() {
    val navController = rememberNavController()
    // singletons - in real app use DI (Hilt/Koin)
    val firebase = remember { FirebaseService() }
    val repo = remember { LibreriaRepository(firebase) }
    val authVm = remember { AuthViewModel(repo, firebase) }
    val catalogVm = remember { CatalogViewModel(repo) }
    val inventoryVm = remember { InventoryViewModel(repo) }
    val adminVm = remember { AdminViewModel(repo) }
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(authVm, onGuest = {
                // go to catalog as guest
                navController.navigate("catalog/guest")
            }, onSignedIn = {
                // decide role after sign in
                val role = authVm.currentUser.value?.role ?: "employee"
                if (role == "admin") navController.navigate("catalog/admin") else navController.navigate("catalog/emplo })
            }
                    composable("catalog/guest") {
                CatalogScreen(vm = catalogVm, onViewDetails = { bookId ->
                    // load book and navigate to details - simplified
                }, onGoInventory = {}, onGoAdmin = {}, isGuest = true)
            }
                    composable("catalog/employee") {
                CatalogScreen(vm = catalogVm, onViewDetails = { bookId -> }, onGoInventory = { navController.navigate("inve }
                        composable("catalog/admin") {
                    CatalogScreen(vm = catalogVm, onViewDetails = { bookId -> }, onGoInventory = { navController.navigate("inve }
                            composable("inventory") {
                        // For demo use placeholders for employee id/name
                        InventoryScreen(vm = inventoryVm, onAddBook = { navController.navigate("addbook") }, currentEmployeeId = au }
                            composable("addbook") {
                        AddBookScreen(vm = inventoryVm, creatorId = authVm.currentUser.value?.uid ?: "EMP_GUEST", creatorName = aut navController.popBackStack()
                    }
                    }
                            composable("admin/movements") {
                        AdminMovementsScreen(vmInventory = inventoryVm)
                    }
                            composable("admin/employees") {
                        // would list employees - simplified
                        EmployeeListScreen(listOf())
                    }
                            composable("admin/create") {
                        CreateAccountScreen(adminVm) {
                            navController.popBackStack()
                        }
                    }
                }
                }