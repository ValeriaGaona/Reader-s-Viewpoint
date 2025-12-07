package com.libreria.app.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.libreria.app.data.model.UserProfile
import com.libreria.app.data.remote.FirebaseService
import com.libreria.app.data.repository.LibreriaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
class AuthViewModel(private val repo: LibreriaRepository, private val firebase: FirebaseService) : ViewModel() {
    private val _currentUser = MutableStateFlow<UserProfile?>(null)
    val currentUser: StateFlow<UserProfile?> = _currentUser
    fun signIn(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            try {
                firebase.signIn(email, password)
                val uid = firebase.currentUser()?.uid ?: ""
                val profile = repo.getUserProfile(uid)
                _currentUser.value = profile
                onResult(true, null)
            } catch (e: Exception) {
                onResult(false, e.message)
            }
        }
    }
    fun signOut() {
        firebase.signOut()
        _currentUser.value = null
    }
    fun signInAsGuest(onResult: () -> Unit) {
        onResult()
    }
}