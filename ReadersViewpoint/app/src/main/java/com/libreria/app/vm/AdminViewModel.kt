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
                repo.deleteUser(userId)

                println("Usuario $userId eliminado con éxito.")
            } catch (e: Exception) {
                println("Error al eliminar el usuario $userId: ${e.message}")
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