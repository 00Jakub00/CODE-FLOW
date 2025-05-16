package com.example.codeflow


import android.app.Application
import com.example.codeflow.databaza.DatabaseProvider
import com.example.codeflow.databaza.Kod


class KniznicaKodov(application: Application) {
    private val database = DatabaseProvider.getDatabase(application)
    private val rozhranie = database.kodDatabazoveRozhranie()
    var aktualneVybranyKod: String? = null

    fun zrusitVyber() {
        aktualneVybranyKod = null
    }

    fun jeVybranyKod(): Boolean {
        return aktualneVybranyKod != null
    }

    suspend fun pridajKod(obsah: String, nazov: String) {
        rozhranie.vlozKod(Kod(obsah = obsah, nazov = nazov))
    }

    suspend fun ziskajVsetkyKody(): List<String> {
        return rozhranie.getVsetkyKody().map { it.nazov }
    }


   suspend fun nastavVybranyKod(nazov: String) {
        aktualneVybranyKod = rozhranie.dajMiKodPodlaNazvu(nazov)?.obsah
    }

    suspend fun odstranKod(nazov: String) {
        rozhranie.odstranKod(nazov)
    }
}
