package com.example.codeflow.databaza

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kody")
data class Kod(
    @PrimaryKey val nazov: String,
    val obsah: String,
    val datumUlozenia: Long = System.currentTimeMillis()
)
