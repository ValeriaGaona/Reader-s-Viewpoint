package com.libreria.app.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.libreria.app.data.model.Libro
import com.libreria.app.data.model.Movimiento
import com.libreria.app.data.model.UserProfile
import kotlinx.coroutines.tasks.await
class FirebaseService(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val librosCol = db.collection("libros")
    private val movCol = db.collection("movimientos")
    private val usersCol = db.collection("users") // stores roles and display names
    // Auth helpers
    fun currentUser(): FirebaseUser? = auth.currentUser
    suspend fun signIn(email: String, password: String) = auth.signInWithEmailAndPassword(email, password).await()
    suspend fun signUp(email: String, password: String) = auth.createUserWithEmailAndPassword(email, password).await()
    fun signOut() = auth.signOut()
    // Users collection - store role
    suspend fun setUserProfile(profile: UserProfile) {
        usersCol.document(profile.uid).set(profile).await()
    }
    suspend fun getUserProfile(uid: String): UserProfile? {
        val doc = usersCol.document(uid).get().await()
        return if (doc.exists()) doc.toObject(UserProfile::class.java) else null
    }
    // Libros CRUD
    suspend fun fetchLibros(): List<Libro> {
        val snap = librosCol.get().await()
        return snap.documents.mapNotNull { d -> d.toObject(Libro::class.java) }
    }
    suspend fun getLibroById(id: String): Libro? {
        val doc = librosCol.document(id).get().await()
        return if (doc.exists()) doc.toObject(Libro::class.java) else null
    }
    suspend fun upsertLibro(libro: Libro) {
        librosCol.document(libro.id).set(libro).await()
    }
    suspend fun deleteLibro(id: String) {
        librosCol.document(id).delete().await()
    }
    // Movimientos
    suspend fun addMovimiento(m: Movimiento) {
        movCol.document(m.id).set(m).await()
    }
    suspend fun fetchMovimientos(): List<Movimiento> {
        // CORRECCIÓN: Usar DESCENDING, que es la constante correcta.
        val snap = movCol.orderBy("dateIso", com.google.firebase.firestore.Query.Direction.DESCENDING).get().await()
        return snap.documents.mapNotNull { it.toObject(Movimiento::class.java) }
    }
    // Admin: create account (client-side createUserWithEmailAndPassword)
    suspend fun createAccount(email: String, password: String, displayName: String, role: String): String {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val uid = result.user?.uid ?: throw Exception("No uid")
        val profile = UserProfile(uid = uid, email = email, displayName = displayName, role = role)
        usersCol.document(uid).set(profile).await()
        return uid
    }
}
