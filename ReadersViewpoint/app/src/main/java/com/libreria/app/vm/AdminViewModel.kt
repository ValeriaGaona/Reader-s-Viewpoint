package com.libreria.app.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.libreria.app.data.model.UserProfile
import com.libreria.app.data.repository.LibreriaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * ViewModel responsable de las funciones administrativas, principalmente la gestión de empleados.
 *
 * @param repo El repositorio [LibreriaRepository] utilizado para la gestión de datos y usuarios.
 */
class AdminViewModel(private val repo: LibreriaRepository) : ViewModel() {
    /**
     * Flujo de estado interno que almacena la lista de perfiles de empleados ([UserProfile]).
     */
    private val _employees = MutableStateFlow<List<UserProfile>>(emptyList())
    /**
     * Flujo de estado público que expone la lista de empleados al UI.
     */
    val employees: StateFlow<List<UserProfile>> = _employees

    init {
        refreshEmployees()
    }

    /**
     * Carga y actualiza la lista de usuarios que tienen el rol de "employee" o "admin".
     *
     * Utiliza un [Flow] del repositorio para recibir actualizaciones en tiempo real.
     */
    fun refreshEmployees() {
        val rolesToFetch = listOf("employee", "admin")

        viewModelScope.launch {
            try {
                repo.getUsersWithRoles(rolesToFetch)
                    .collect { users ->
                        _employees.value = users
                    }
            } catch (e: Exception) {
                println("Error cargando empleados: ${e.message}")

                _employees.value = emptyList()
            }
        }
    }
    /**
     * Crea una nueva cuenta de usuario (empleado o administrador).
     *
     * El proceso implica crear la cuenta en Firebase Auth y guardar el perfil en Firestore.
     *
     * @param email Correo electrónico para la nueva cuenta.
     * @param password Contraseña para la nueva cuenta.
     * @param displayName Nombre visible del usuario.
     * @param role Rol del usuario ("admin" o "employee").
     * @param onResult Callback que se ejecuta con el resultado: `true` si éxito, y un mensaje de error opcional.
     */
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

    /**
     * Elimina permanentemente un usuario del sistema (Auth y perfil de Firestore).
     *
     * @param userId El ID de usuario (UID) a eliminar.
     */
    fun deleteUser(userId: String) {
        viewModelScope.launch {
            try {
                repo.deleteUser(userId)

                println("Usuario $userId eliminado con éxito.")
                // No se llama a refreshEmployees() aquí, se confía en que el colector
                // de refreshEmployees() se encargará de la actualización en tiempo real.
            } catch (e: Exception) {
                println("Error al eliminar el usuario $userId: ${e.message}")
            }
        }
    }

    /**
     * Obtiene los detalles de un usuario específico por su UID.
     *
     * @param uid El ID de usuario (UID) del perfil a buscar.
     * @return Un [StateFlow] que emite el [UserProfile] encontrado o `null`.
     */
    fun getUserDetails(uid: String): StateFlow<UserProfile?> {
        val userFlow = MutableStateFlow<UserProfile?>(null)

        viewModelScope.launch {
            // Suscribe al flujo del repositorio para recibir actualizaciones en tiempo real
            repo.getUserDetails(uid).collect { user ->
                userFlow.value = user
            }
        }
        return userFlow
    }
}