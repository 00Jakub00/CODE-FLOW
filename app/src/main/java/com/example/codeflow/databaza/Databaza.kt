package com.example.codeflow.databaza

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Kod::class], version = 1)
abstract class Databaza : RoomDatabase() {
    abstract fun kodDatabazoveRozhranie(): DatabazoveRozhranie
}