package com.libreria.app.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.libreria.app.data.model.UserProfile
import com.libreria.app.data.repository.LibreriaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminViewModel(private val repo: LibreriaRepository) : ViewModel() {
    private val _employees = MutableStateFlow<List<UserProfile>>(emptyList())
    val employees: StateFlow<List<UserProfile>> = _employees

    fun refreshEmployees() {
        viewModelScope.launch {
            // For demo: fetch movements and extract employees from repository movements or users collection
            // Aquí llamaríamos a un método del repo para listar usuarios; omitido por brevedad
        }
    }

    fun createAccount(
        email: String,
        password: String,
        displayName: String,
        role: String,
        onResult: (Boolean, String?) -> Unit // <-- El último parámetro es una lambda (función)
    ) { // <-- ¡Abre el cuerpo de la función aquí!
        viewModelScope.launch {
            try {
                // Aquí el error 'val uid = ...' se corrige porque ahora está dentro de un 'launch' válido.
                val uid = repo.createAccount(email, password, displayName, role)
                onResult(true, uid)
            } catch (e: Exception) {
                onResult(false, e.message)
            }
        } // Cierra viewModelScope.launch
    } // <-- Cierra la función createAccount

} // <-- Cierra la clase AdminViewModel