package com.libreria.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.google.firebase.firestore.FirebaseFirestore
import com.libreria.app.data.remote.FirebaseService
import com.libreria.app.data.repository.LibreriaRepository
import com.libreria.app.ui.screens.admin.AdminMovementsScreen
import com.libreria.app.ui.screens.admin.CreateAccountScreen
import com.libreria.app.ui.screens.admin.EmployeeListScreen
import com.libreria.app.ui.screens.admin.SalesHistoryScreen
import com.libreria.app.ui.screens.admin.AdministrationScreen
import com.libreria.app.ui.screens.employee.AddBookScreen
import com.libreria.app.ui.screens.employee.EmployeeDetailsScreen
import com.libreria.app.ui.screens.employee.InventoryScreen
import com.libreria.app.ui.screens.guest.CatalogScreen
import com.libreria.app.ui.screens.guest.ReceiptScreen
import com.libreria.app.ui.screens.guest.ShoppingCartScreen
import com.libreria.app.ui.screens.login.LoginScreen
import com.libreria.app.vm.*
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize


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

    val firestore = remember { FirebaseFirestore.getInstance() }

    val repo = remember { LibreriaRepository(firebase, firestore) }

    val authVm = remember { AuthViewModel(repo, firebase) }
    val catalogVm = remember { CatalogViewModel(repo) }
    val inventoryVm = remember { InventoryViewModel(repo) }
    val adminVm = remember { AdminViewModel(repo) }

    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            LoginScreen(
                authVm,
                onGuest = {
                    navController.navigate("guest_catalog") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onSignedIn = {
                    val role = authVm.currentUser.value?.role ?: "employee"
                    val destination = when (role) {
                        "admin", "employee" -> "admin_dashboard"
                        else -> "guest_catalog"
                    }
                    navController.navigate(destination) {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("guest_catalog") {
            CatalogScreen(
                vm = catalogVm,
                onViewDetails = { bookId -> navController.navigate("book_details/$bookId") },
                onGoShoppingCart = { navController.navigate("shoppingcart") },
                onGoBack = {
                    navController.navigate("login") {
                        popUpTo("guest_catalog") { inclusive = true }
                    }
                }
            )
        }

        composable("admin_dashboard") {
            AdministrationScreen(
                onGoInventory = { navController.navigate("inventory") },
                onGoEmployees = { navController.navigate("admin/employees") },
                onGoMovements = { navController.navigate("admin/movements") },
                onCreateAccount = { navController.navigate("admin/create") },
                onGoSalesHistory = { navController.navigate("sales_history") },
                onGoBack = {
                    navController.navigate("login") {
                        popUpTo("admin_dashboard") { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = "book_details/{bookId}",
            arguments = listOf(navArgument("bookId") { type = NavType.StringType })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId")

            if (bookId.isNullOrEmpty()) {
                navController.popBackStack()
                return@composable
            }

            val bookDetails by catalogVm.getBookById(bookId).collectAsState(initial = null)

            if (bookDetails != null) {
                com.libreria.app.ui.screens.guest.BookDetailsScreen(
                    book = bookDetails!!,
                    vm = catalogVm,
                    onBack = { navController.popBackStack() }
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }

        composable("shoppingcart") {
            ShoppingCartScreen(
                vm = catalogVm,
                onClose = { navController.popBackStack() },
                onCheckoutSuccess = { ticketId ->
                    navController.navigate("receipt/$ticketId?origin=cart") {
                        popUpTo("shoppingcart") { inclusive = true }
                    }
                },
                onGoBack = { navController.popBackStack() },
                onGoCheckout = { /* No se usa */ }
            )
        }

        composable(
            route = "receipt/{ticketId}?origin={origin}",
            arguments = listOf(
                navArgument("ticketId") { defaultValue = "" },
                navArgument("origin") { type = NavType.StringType; defaultValue = "cart" }
            )
        ) { backStackEntry ->
            val ticketId = backStackEntry.arguments?.getString("ticketId") ?: return@composable
            val origin = backStackEntry.arguments?.getString("origin") ?: "cart"


            ReceiptScreen(
                vm = catalogVm,
                ticketId = ticketId,
                origin = origin,
                onGoHome = {
                    navController.navigate("guest_catalog") {
                        popUpTo("guest_catalog") { inclusive = true }
                        launchSingleTop = true
                    }
                },

                onNavigateBackToHistory = {
                    navController.popBackStack("sales_history", inclusive = false)
                }
            )
        }

        composable("sales_history") {
            SalesHistoryScreen(
                vm = catalogVm,
                onClose = { navController.popBackStack() },
                onViewTicketDetails = { ticketId ->
                    navController.navigate("receipt/$ticketId?origin=history")
                }
            )
        }

        composable("inventory") {
            InventoryScreen(
                vm = inventoryVm,
                onAddBook = { navController.navigate("addbook") },
                currentEmployeeId = authVm.currentUser.value?.uid ?: "EMP_GUEST",
                currentEmployeeName = authVm.currentUser.value?.email ?: "Unknown Employee",
                onGoBack = { navController.popBackStack() }
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

        composable("admin/movements") {
            AdminMovementsScreen(
                vmInventory = inventoryVm,
                onClose = { navController.popBackStack() }
            )
        }


        composable("admin/employees") {
            // Esto recopila el flujo de empleados en la UI
            val employees by adminVm.employees.collectAsState()
            EmployeeListScreen(
                employees = employees,
                onViewDetails = { employeeId ->
                    navController.navigate("admin/employees/$employeeId")
                },
                onClose = { navController.popBackStack() }
            )
        }

        composable(
            route = "admin/employees/{employeeId}",
            arguments = listOf(navArgument("employeeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val employeeId = backStackEntry.arguments?.getString("employeeId") ?: return@composable
            EmployeeDetailsScreen(
                employeeId = employeeId,
                adminVm = adminVm,
                onBack = { navController.popBackStack() },
                onDelete = { uid ->
                    adminVm.deleteUser(uid)
                    navController.popBackStack()
                }
            )
        }

        composable("admin/create") {
            CreateAccountScreen(
                adminVm = adminVm,
                onDone = {
                    navController.popBackStack()
                },
                onClose = {
                    navController.popBackStack()
                }
            )
        }
    }
}