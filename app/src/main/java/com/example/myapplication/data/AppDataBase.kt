package com.example.myapplication.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.model.Movimiento
import com.example.myapplication.model.Reserva
import com.example.myapplication.model.Usuario

@Database(
    entities = [Usuario::class, Reserva::class, Movimiento::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun reservaDao(): ReservaDao
    abstract fun movimientoDao(): MovimientoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        //  unica base de datos para toda la app
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "zaror.db")
                    .fallbackToDestructiveMigration() // para recrear si hay cambios
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}