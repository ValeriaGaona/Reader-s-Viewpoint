//package com.libreria.app.data.local
//
//import android.content.Context
//import androidx.room.Database
//import androidx.room.Room
//import androidx.room.RoomDatabase
//import com.libreria.app.data.model.Libro
//import com.libreria.app.data.model.Movimiento
//
//@Database(entities = [Libro::class, Movimiento::class], version = 1, exportSchema = false)
//abstract class LibreriaDatabase : RoomDatabase() {
//
//    // Define tus DAOs aquí
//    abstract fun libroDao(): LibroDao
//    abstract fun movimientoDao(): MovimientoDao
//
//    companion object {
//        @Volatile
//        private var INSTANCE: LibreriaDatabase? = null
//
//        fun getDatabase(context: Context): LibreriaDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext, // 🚨 Usar applicationContext para evitar fugas de memoria
//                    LibreriaDatabase::class.java,
//                    "libreria_db"
//                ).build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
//}