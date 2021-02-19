package com.marco_dos_santos.mvvmsubscribers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.marco_dos_santos.mvvmsubscribers.db.SubscriberRepository
import java.lang.IllegalArgumentException

class SubscriberViewModelFactory(private val repository: SubscriberRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SubscriberViewModel::class.java)){
            return SubscriberViewModel(repository) as T
        }
        throw IllegalArgumentException("Clase View Model desconocida")
    }
}