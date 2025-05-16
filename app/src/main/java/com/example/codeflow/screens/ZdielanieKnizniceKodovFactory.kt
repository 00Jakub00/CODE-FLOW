package com.example.codeflow.screens

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.codeflow.ZdielanieKnizniceKodov

class ZdielanieKnizniceKodovFactory(private val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ZdielanieKnizniceKodov::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ZdielanieKnizniceKodov(app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}