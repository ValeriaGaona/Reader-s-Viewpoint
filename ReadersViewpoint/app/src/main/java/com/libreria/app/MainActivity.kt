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


/**
 * La actividad principal de la aplicación y el punto de entrada para Compose.
 *
 * Configura el tema de Material Design y carga el componente raíz [AppEntry].
 */
class MainActivity : ComponentActivity() {
    /**
     * Se llama cuando la actividad es creada.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AppEntry()
            }
        }
    }
}

/**
 * Componente Composable raíz que establece la arquitectura de la aplicación (Service, Repository, ViewModels).
 *
 * Contiene el [NavHost] principal que maneja la navegación entre todas las pantallas de la aplicación,
 * desde la autenticación hasta las funciones administrativas.
 */
@Composable
fun AppEntry() {
    val navController = rememberNavController()
    // Inicialización y persistencia de servicios y repositorios usando remember
    val firebase = remember { FirebaseService() }

    val firestore = remember { FirebaseFirestore.getInstance() }

    val repo = remember { LibreriaRepository(firebase, firestore) }

    // Inicialización y persistencia de ViewModels
    val authVm = remember { AuthViewModel(repo, firebase) }
    val catalogVm = remember { CatalogViewModel(repo) }
    val inventoryVm = remember { InventoryViewModel(repo) }
    val adminVm = remember { AdminViewModel(repo) }

    /**
     * Define el grafo de navegación de la aplicación.
     * @param navController Controlador de navegación para manejar las transiciones.
     * @param startDestination Ruta inicial al abrir la aplicación ("login").
     */
    NavHost(navController = navController, startDestination = "login") {

        /**
         * Ruta de inicio de sesión.
         * Maneja la navegación basada en el rol de usuario o acceso como invitado.
         */
        composable("login") {
            LoginScreen(
                authVm,
                onGuest = {
                    navController.navigate("guest_catalog") {
                        // Limpia la pila para que 'login' no sea accesible con el botón atrás.
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
                        // Limpia la pila para evitar regresar a 'login' después del éxito.
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        /**
         * Ruta del catálogo de libros para usuarios invitados o no autenticados.
         */
        composable("guest_catalog") {
            CatalogScreen(
                vm = catalogVm,
                onViewDetails = { bookId -> navController.navigate("book_details/$bookId") },
                onGoShoppingCart = { navController.navigate("shoppingcart") },
                onGoBack = {
                    // Cierra la sesión/vista de invitado y regresa a la pantalla de login.
                    navController.navigate("login") {
                        popUpTo("guest_catalog") { inclusive = true }
                    }
                }
            )
        }

        /**
         * Ruta del panel principal de administración/empleados.
         * La navegación es manejada por [AdministrationScreen] (botones).
         */
        composable("admin_dashboard") {
            AdministrationScreen(
                onGoInventory = { navController.navigate("inventory") },
                onGoEmployees = { navController.navigate("admin/employees") },
                onGoMovements = { navController.navigate("admin/movements") },
                onCreateAccount = { navController.navigate("admin/create") },
                onGoSalesHistory = { navController.navigate("sales_history") },
                onGoBack = {
                    // Cierra la sesión y regresa a la pantalla de login.
                    navController.navigate("login") {
                        popUpTo("admin_dashboard") { inclusive = true }
                    }
                }
            )
        }

        /**
         * Ruta para ver los detalles de un libro específico.
         *
         * @param route Define el argumento requerido: bookId.
         * @param arguments Define el tipo de argumento.
         */
        composable(
            route = "book_details/{bookId}",
            arguments = listOf(navArgument("bookId") { type = NavType.StringType })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId")

            if (bookId.isNullOrEmpty()) {
                navController.popBackStack()
                return@composable
            }

            // Recopila los detalles del libro por ID como estado.
            val bookDetails by catalogVm.getBookById(bookId).collectAsState(initial = null)

            if (bookDetails != null) {
                com.libreria.app.ui.screens.guest.BookDetailsScreen(
                    book = bookDetails!!,
                    vm = catalogVm,
                    onBack = { navController.popBackStack() }
                )
            } else {
                // Muestra un indicador de carga si los detalles del libro aún no están disponibles.
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }

        /**
         * Ruta del carrito de compras.
         */
        composable("shoppingcart") {
            ShoppingCartScreen(
                vm = catalogVm,
                onClose = { navController.popBackStack() },
                onCheckoutSuccess = { ticketId ->
                    // Navega a la pantalla de recibo tras el éxito de la compra.
                    navController.navigate("receipt/$ticketId?origin=cart") {
                        // Limpia la pila para que no se pueda volver al carrito después del checkout.
                        popUpTo("shoppingcart") { inclusive = true }
                    }
                },
                onGoBack = { navController.popBackStack() },
                onGoCheckout = { /* No se usa en esta implementación */ }
            )
        }

        /**
         * Ruta para mostrar el recibo de una venta.
         *
         * @param route Define argumentos: ticketId (requerido) y origin (opcional, para saber de dónde viene).
         */
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
                    // Regresa al catálogo de invitados (limpia las vistas transaccionales).
                    navController.navigate("guest_catalog") {
                        popUpTo("guest_catalog") { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateBackToHistory = {
                    // Regresa al historial de ventas.
                    navController.popBackStack("sales_history", inclusive = false)
                }
            )
        }

        /**
         * Ruta del historial de ventas (accesible desde el dashboard de administrador).
         */
        composable("sales_history") {
            SalesHistoryScreen(
                vm = catalogVm,
                onClose = { navController.popBackStack() },
                onViewTicketDetails = { ticketId ->
                    navController.navigate("receipt/$ticketId?origin=history")
                }
            )
        }

        /**
         * Ruta del inventario principal (accesible desde el dashboard).
         */
        composable("inventory") {
            InventoryScreen(
                vm = inventoryVm,
                onAddBook = { navController.navigate("addbook") },
                currentEmployeeId = authVm.currentUser.value?.uid ?: "EMP_GUEST",
                currentEmployeeName = authVm.currentUser.value?.email ?: "Unknown Employee",
                onGoBack = { navController.popBackStack() }
            )
        }

        /**
         * Ruta para añadir un nuevo libro al inventario.
         */
        composable("addbook") {
            AddBookScreen(
                vm = inventoryVm,
                creatorId = authVm.currentUser.value?.uid ?: "EMP_GUEST",
                creatorName = authVm.currentUser.value?.email ?: "Unknown Creator"
            ) {
                navController.popBackStack()
            }
        }

        /**
         * Ruta para ver los movimientos de inventario (auditoría).
         */
        composable("admin/movements") {
            AdminMovementsScreen(
                vmInventory = inventoryVm,
                onClose = { navController.popBackStack() }
            )
        }

        /**
         * Ruta para ver la lista de empleados.
         */
        composable("admin/employees") {
            // Recopila el flujo de empleados en la UI
            val employees by adminVm.employees.collectAsState()
            EmployeeListScreen(
                employees = employees,
                onViewDetails = { employeeId ->
                    navController.navigate("admin/employees/$employeeId")
                },
                onClose = { navController.popBackStack() }
            )
        }

        /**
         * Ruta para ver los detalles de un empleado específico.
         *
         * @param route Define el argumento requerido: employeeId.
         */
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
                    adminVm.deleteUser(uid) // Llama a la lógica de eliminación del ViewModel
                    navController.popBackStack() // Vuelve a la lista de empleados
                }
            )
        }

        /**
         * Ruta para crear una nueva cuenta de empleado/administrador.
         */
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