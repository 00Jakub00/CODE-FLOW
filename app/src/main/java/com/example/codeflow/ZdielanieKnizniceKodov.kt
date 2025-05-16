package com.example.codeflow

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel

class ZdielanieKnizniceKodov(app: Application) : AndroidViewModel(app) {
    val kniznicaKodov = KniznicaKodov(app)
}