package com.libreria.app.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.libreria.app.data.model.UserProfile
import com.libreria.app.data.repository.LibreriaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AdminViewModel(private val repo: LibreriaRepository) : ViewModel() {
    private val _employees = MutableStateFlow<List<UserProfile>>(emptyList())
    val employees: StateFlow<List<UserProfile>> = _employees

    init {
        refreshEmployees()
    }

//    fun refreshEmployees() {
//        val rolesToFetch = listOf("employee", "admin")
//
//        viewModelScope.launch {
//            repo.getUsersWithRoles(rolesToFetch)
//                .collect { users ->
//                    _employees.value = users
//                }
//        }
//    }

    fun refreshEmployees() {
        val rolesToFetch = listOf("employee", "admin")

        viewModelScope.launch {
            try {
                // ✅ ENVUELVE LA LLAMADA Y LA COLECCIÓN EN UN TRY-CATCH
                repo.getUsersWithRoles(rolesToFetch)
                    .collect { users ->
                        _employees.value = users
                    }
            } catch (e: Exception) {
                // 🛑 Maneja la excepción aquí para evitar que la app se cierre.
                // Es probable que Firestore falle aquí si hay un problema.
                println("Error cargando empleados: ${e.message}")

                // Opcional: Establecer una lista vacía para que la UI no se bloquee
                _employees.value = emptyList()
            }
        }
    }
    fun createAccount(
        email: String,
        password: String,
        displayName: String,
        role: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val uid = repo.createAccount(email, password, displayName, role)
                onResult(true, uid)
            } catch (e: Exception) {
                onResult(false, e.message)
            }
        }
    }

    fun deleteUser(userId: String) {
        viewModelScope.launch {
            try {
                // Llama a la función del repositorio para realizar la eliminación.
                repo.deleteUser(userId)

                // Opcional: Si tienes una lista de empleados en el ViewModel,
                // actualízala después de la eliminación.
                // refreshEmployees()

                println("Usuario $userId eliminado con éxito.")
            } catch (e: Exception) {
                // Manejar errores de eliminación (ej: permisos insuficientes, usuario no encontrado)
                println("Error al eliminar el usuario $userId: ${e.message}")
                // Aquí podrías emitir un error al UI si lo necesitas.
            }
        }
    }

    fun getUserDetails(uid: String): StateFlow<UserProfile?> {
        val userFlow = MutableStateFlow<UserProfile?>(null)

        viewModelScope.launch {
            repo.getUserDetails(uid).collect { user ->
                userFlow.value = user
            }
        }
        return userFlow
    }

}