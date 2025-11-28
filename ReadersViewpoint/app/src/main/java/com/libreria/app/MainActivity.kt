package com.libreria.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.libreria.app.data.remote.FirebaseService
import com.libreria.app.data.repository.LibreriaRepository
import com.libreria.app.ui.screens.*
import com.libreria.app.vm.*
import com.libreria.app.ui.screens.guest.CatalogScreen
import com.libreria.app.ui.screens.login.LoginScreen
import com.libreria.app.ui.screens.employee.InventoryScreen
import com.libreria.app.ui.screens.admin.CreateAccountScreen
import com.libreria.app.ui.screens.employee.AddBookScreen
import com.libreria.app.ui.screens.admin.AdminMovementsScreen
import com.libreria.app.ui.screens.admin.EmployeeListScreen

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
    val firebase = remember { FirebaseService() }
    val repo = remember { LibreriaRepository(firebase) }
    val authVm = remember { AuthViewModel(repo, firebase) }
    val catalogVm = remember { CatalogViewModel(repo) }
    val inventoryVm = remember { InventoryViewModel(repo) }
    val adminVm = remember { AdminViewModel(repo) }

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                authVm,
                onGuest = { navController.navigate("catalog/guest") },
                onSignedIn = {
                    val role = authVm.currentUser.value?.role ?: "employee"
                    if (role == "admin") {
                        navController.navigate("catalog/admin")
                    } else {
                        navController.navigate("catalog/employee")
                    }
                }
            )
        }

        // --- Rutas de Catálogo ---
        composable("catalog/guest") {
            CatalogScreen(
                vm = catalogVm,
                onViewDetails = { bookId -> /* TODO: Navegar a detalles */ },
                onGoInventory = { /* No disponible */ },
                onGoEmployees = { /* No disponible */ },
                onGoMovements = { /* No disponible */ },
                onCreateAccount = { /* No disponible */ },
                isGuest = true
            )
        }

        composable("catalog/employee") {
            CatalogScreen(
                vm = catalogVm,
                onViewDetails = { bookId -> /* TODO: Navegar a detalles */ },
                onGoInventory = { navController.navigate("inventory") },
                onGoEmployees = { navController.navigate("admin/employees") },
                onGoMovements = { navController.navigate("admin/movements") },
                onCreateAccount = { navController.navigate("admin/create") },
                isGuest = false
            )
        }

//        composable("catalog/admin") {
//            CatalogScreen(
//                vm = catalogVm,
//                onViewDetails = { bookId -> /* TODO: Navegar a detalles */ },
//                onGoInventory = { navController.navigate("inventory") },
//                onGoEmployees = { navController.navigate("admin/employees") },
//                onGoMovements = { navController.navigate("admin/movements") },
//                onCreateAccount = { navController.navigate("admin/create") },
//                isGuest = false
//            )
//        }

        // --- Rutas de Empleado ---
        composable("inventory") {
            InventoryScreen(
                vm = inventoryVm,
                onAddBook = { navController.navigate("addbook") },
                currentEmployeeId = authVm.currentUser.value?.uid ?: "EMP_GUEST",
                currentEmployeeName = authVm.currentUser.value?.email ?: "Unknown Employee"
            )
        }

        composable("addbook") {
            AddBookScreen(
                vm = inventoryVm,
                creatorId = authVm.currentUser.value?.uid ?: "EMP_GUEST",
                creatorName = authVm.currentUser.value?.email ?: "Unknown Creator"
            ) {
                navController.popBackStack()
            }
        }

        // --- Rutas de Administrador ---
        composable("admin/movements") {
            AdminMovementsScreen(
                vmInventory = inventoryVm,
                onClose = { navController.popBackStack() }
            )
        }

        composable("admin/employees") {
            val employees by adminVm.employees.collectAsState()
            EmployeeListScreen(
                employees = employees,
                onViewDetails = { employeeId ->
                    navController.navigate("admin/employees/$employeeId")
                },
                onClose = { navController.popBackStack() }
            )
        }

        composable("admin/create") {
            CreateAccountScreen(adminVm) {
                navController.popBackStack()
            }
        }
    }
}