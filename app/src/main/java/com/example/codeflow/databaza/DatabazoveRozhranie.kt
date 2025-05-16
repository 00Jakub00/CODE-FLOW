package com.example.codeflow.databaza

import androidx.room.*

@Dao
interface DatabazoveRozhranie {
    @Query("SELECT * FROM kody ORDER BY datumUlozenia DESC")
    suspend fun getVsetkyKody(): List<Kod>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun vlozKod(kod: Kod)

    @Query("DELETE FROM kody WHERE nazov = :nazov")
    suspend fun odstranKod(nazov: String)

    @Query("SELECT * FROM kody WHERE nazov = :nazov")
    suspend fun dajMiKodPodlaNazvu(nazov: String): Kod?

}