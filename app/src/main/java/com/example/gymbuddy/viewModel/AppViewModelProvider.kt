package com.example.gymbuddy.viewModel

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.gymbuddy.GymApplication

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            SchedeViewModel(gymApplication().container.gymRepository)
        }
        initializer {
            StartAllenamentoViewModel(
                gymApplication().container.gymRepository,
                this.createSavedStateHandle(),
            )
        }
        initializer {
            SchedaDetailViewModel(
                gymApplication().container.gymRepository,
                this.createSavedStateHandle(),
            )
        }
        initializer{
            CameraScannerViewModel(
                gymApplication().container.gymRepository,
            )
        }
        initializer{
            LoginViewModel(
                gymApplication().container.gymRepository,
            )
        }

    }
}
fun CreationExtras.gymApplication(): GymApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as GymApplication)
