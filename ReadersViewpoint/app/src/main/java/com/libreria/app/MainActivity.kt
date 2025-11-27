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
    // ... (otras inicializaciones de ViewModels)

    NavHost(navController = navController, startDestination = "login") { // <-- Abre el NavHost (Línea 36)
        composable("login") {
            LoginScreen(authVm, onGuest = {
                navController.navigate("catalog/guest")
            }, onSignedIn = {
                val role = authVm.currentUser.value?.role ?: "employee"
                if (role == "admin") {
                    navController.navigate("catalog/admin")
                } else {
                    navController.navigate("catalog/employee")
                }
            }) // <-- Cierra LoginScreen y el composable "login"
        } // <-- Cierra el bloque composable("login")

        composable("catalog/guest") { // <-- Rutas subsiguientes
            CatalogScreen(vm = catalogVm, onViewDetails = { bookId ->
                // ...
            }, onGoInventory = {}, onGoAdmin = {}, isGuest = true)
        }

        composable("catalog/employee") {
            CatalogScreen(vm = catalogVm, onViewDetails = { bookId -> }, onGoInventory = {
                navController.navigate("inventory")
            }, onGoAdmin = {}, isGuest = false) // Agregando argumentos faltantes para un cierre correcto
        }

        composable("catalog/admin") {
            CatalogScreen(vm = catalogVm, onViewDetails = { bookId -> }, onGoInventory = {
                navController.navigate("inventory")
            }, onGoAdmin = { navController.navigate("admin/movements") }, isGuest = false) // Agregando argumentos faltantes
        }

        composable("inventory") {
            InventoryScreen(
                vm = inventoryVm,
                onAddBook = {
                    navController.navigate("addbook")
                },
                currentEmployeeId = authVm.currentUser.value?.uid ?: "EMP_GUEST",
                // 🚨 Añade este argumento faltante:
                currentEmployeeName = authVm.currentUser.value?.email ?: "Unknown Employee"
            )
        }

        composable("addbook") {
            AddBookScreen(vm = inventoryVm, creatorId = authVm.currentUser.value?.uid ?: "EMP_GUEST", creatorName = authVm.currentUser.value?.email ?: "Unknown Creator") {
                navController.popBackStack()
            }
        }

        composable("admin/movements") {
            AdminMovementsScreen(vmInventory = inventoryVm)
        }

        composable("admin/employees") {
            EmployeeListScreen(listOf())
        }

        composable("admin/create") {
            CreateAccountScreen(adminVm) {
                navController.popBackStack()
            }
        }

    } // <-- Cierra el NavHost (FIN DEL ARCHIVO)
}

//package com.libreria.app
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.runtime.*
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//import com.libreria.app.data.remote.FirebaseService
//import com.libreria.app.data.repository.LibreriaRepository
//import com.libreria.app.ui.screens.*
//import com.libreria.app.vm.*
//import com.libreria.app.ui.screens.guest.CatalogScreen
//import com.libreria.app.ui.screens.login.LoginScreen
//import com.libreria.app.ui.screens.employee.InventoryScreen
//import com.libreria.app.ui.screens.admin.CreateAccountScreen
//import com.libreria.app.ui.screens.employee.AddBookScreen
//import com.libreria.app.ui.screens.admin.AdminMovementsScreen
//import com.libreria.app.ui.screens.admin.EmployeeListScreen
//import androidx.compose.ui.platform.LocalContext // 🚨 IMPORTANTE: Añadir esta importación
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            MaterialTheme {
//                AppEntry()
//            }
//        }
//    }
//}
//
//@Composable
//fun AppEntry() {
//    // 🚨 PASO CRÍTICO: Obtener el contexto de Android de forma segura
//    val context = LocalContext.current
//
//    val navController = rememberNavController()
//
//    // 🚨 PASO CRÍTICO: Pasar el contexto al repositorio (si Room lo necesita)
//    val firebase = remember { FirebaseService() }
//    val repo = remember {
//        // Si LibreriaRepository usa Room, necesita el contexto.
//        // Asumo que el constructor de LibreriaRepository podría necesitarlo.
//        LibreriaRepository(context, firebase) // <--- Cambiado: Se pasa 'context'
//    }
//
//    val authVm = remember { AuthViewModel(repo, firebase) }
//    val catalogVm = remember { CatalogViewModel(repo) }
//    val inventoryVm = remember { InventoryViewModel(repo) }
//    val adminVm = remember { AdminViewModel(repo) }
//
//    NavHost(navController = navController, startDestination = "login") {
//        composable("login") {
//            LoginScreen(authVm, onGuest = {
//                navController.navigate("catalog/guest")
//            }, onSignedIn = {
//                val role = authVm.currentUser.value?.role ?: "employee"
//                if (role == "admin") {
//                    navController.navigate("catalog/admin")
//                } else {
//                    navController.navigate("catalog/employee")
//                }
//            })
//        }
//
//        composable("catalog/guest") {
//            CatalogScreen(vm = catalogVm, onViewDetails = { bookId ->
//                // ...
//            }, onGoInventory = {}, onGoAdmin = {}, isGuest = true)
//        }
//
//        composable("catalog/employee") {
//            CatalogScreen(vm = catalogVm, onViewDetails = { bookId -> }, onGoInventory = {
//                navController.navigate("inventory")
//            }, onGoAdmin = {}, isGuest = false)
//        }
//
//        composable("catalog/admin") {
//            CatalogScreen(vm = catalogVm, onViewDetails = { bookId -> }, onGoInventory = {
//                navController.navigate("inventory")
//            }, onGoAdmin = { navController.navigate("admin/movements") }, isGuest = false)
//        }
//
//        composable("inventory") {
//            InventoryScreen(
//                vm = inventoryVm,
//                onAddBook = {
//                    navController.navigate("addbook")
//                },
//                currentEmployeeId = authVm.currentUser.value?.uid ?: "EMP_GUEST",
//                currentEmployeeName = authVm.currentUser.value?.email ?: "Unknown Employee"
//            )
//        }
//
//        composable("addbook") {
//            AddBookScreen(vm = inventoryVm, creatorId = authVm.currentUser.value?.uid ?: "EMP_GUEST", creatorName = authVm.currentUser.value?.email ?: "Unknown Creator") {
//                navController.popBackStack()
//            }
//        }
//
//        composable("admin/movements") {
//            AdminMovementsScreen(vmInventory = inventoryVm)
//        }
//
//        composable("admin/employees") {
//            EmployeeListScreen(listOf())
//        }
//
//        composable("admin/create") {
//            CreateAccountScreen(adminVm) {
//                navController.popBackStack()
//            }
//        }
//
//    } // Cierra NavHost
//}