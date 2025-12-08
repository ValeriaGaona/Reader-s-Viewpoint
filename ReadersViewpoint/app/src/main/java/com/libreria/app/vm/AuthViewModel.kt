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

/**
 * ViewModel responsable de la lógica de autenticación y gestión del usuario activo.
 *
 * @param repo El repositorio [LibreriaRepository] para obtener el perfil de usuario.
 * @param firebase El servicio [FirebaseService] para manejar las operaciones de autenticación.
 */
class AuthViewModel(private val repo: LibreriaRepository, private val firebase: FirebaseService) : ViewModel() {
    /**
     * Flujo de estado interno que almacena el perfil del usuario autenticado ([UserProfile]) o `null`.
     */
    private val _currentUser = MutableStateFlow<UserProfile?>(null)
    /**
     * Flujo de estado público que expone el perfil del usuario activo al UI.
     */
    val currentUser: StateFlow<UserProfile?> = _currentUser

    /**
     * Intenta autenticar a un usuario con email y contraseña.
     *
     * Si la autenticación es exitosa, obtiene el [UserProfile] del usuario y lo almacena
     * en [_currentUser].
     *
     * @param email El correo electrónico del usuario.
     * @param password La contraseña del usuario.
     * @param onResult Callback que se ejecuta con el resultado: `true` si éxito, y un mensaje de error opcional.
     */
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
    /**
     * Cierra la sesión del usuario actual en Firebase y limpia el perfil local.
     */
    fun signOut() {
        firebase.signOut()
        _currentUser.value = null
    }
    /**
     * Simula el inicio de sesión como invitado.
     *
     * @param onResult Callback que se ejecuta para confirmar la acción.
     */
    fun signInAsGuest(onResult: () -> Unit) {
        onResult()
    }
}