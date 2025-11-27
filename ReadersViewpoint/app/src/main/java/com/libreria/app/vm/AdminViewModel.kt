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
            // Here we would call a repo method to list users; omitted for brevity
        }
    }
    fun createAccount(email: String, password: String, displayName: String, role: String, onResult: (Boolean, String?)  viewModelScope.launch {
        try {
            val uid = repo.createAccount(email, password, displayName, role)
            onResult(true, uid)
        } catch (e: Exception) {
            onResult(false, e.message)
        }
    }
}
}
