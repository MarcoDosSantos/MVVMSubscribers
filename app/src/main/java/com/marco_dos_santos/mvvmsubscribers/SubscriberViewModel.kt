package com.marco_dos_santos.mvvmsubscribers


import android.util.Patterns
import android.widget.Toast
import androidx.core.content.res.TypedArrayUtils.getText
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marco_dos_santos.mvvmsubscribers.db.Subscriber
import com.marco_dos_santos.mvvmsubscribers.db.SubscriberRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SubscriberViewModel(private val repository: SubscriberRepository): ViewModel(), Observable {

    private var isUpdateOrDelete = false
    private lateinit var subscriberToUpdateOrDelete: Subscriber

    //instancia con contenido que "viene" del repositorio. Recibe y guarda un objeto LiveData<List<Subscriber>>
    // Aunque el usuario gire el dispositivo, esta variable (esta clase ViewModel) no se reinicia, de modo que los
    // datos no se pierden. Para que la vista conserve los datos al reiniciar el ciclo, basta con que observe al ViewModel
    // en el onCreate() / onCreateView()*/
    val subscribers = repository.subscribers

    /*Variables que se conectan con los elementos de la vista, mediante dataBinding
    * Se anotan como "Bindable. La comunicación es de doble vía (dan y reciben).
    * Para que se pueda hacer, el tipo de dato tiene que ser MutableLiveData<String>*/
    @Bindable
    val etSubscriberName = MutableLiveData<String>()

    @Bindable
    val etSubscriberEmail = MutableLiveData<String>()
    /*En el caso de los botones, la comunicación es sólo de ida*/
    @Bindable
    val saveOrUpdateButtonText = MutableLiveData<String>()

    @Bindable
    val clearAllOrDeleteText = MutableLiveData<String>()

    private val statusMessage = MutableLiveData<Event<String>>()

    val message: LiveData<Event<String>> get() = statusMessage


    init{//Si no se inicializan los valores, el xml de la vista no los ve y no se puede conectar.
        etSubscriberName.value = ""//Agregado por mi, no está en el tutorial, pero si no inicializo, el xml no lo reconoce
        etSubscriberEmail.value = ""//Agregado por mi, no está en el tutorial, pero si no inicializo, el xml no lo reconoce

        saveOrUpdateButtonText.value = "Agregar"
        clearAllOrDeleteText.value = "Borrar todos"
    }
    //Métodos de los botones. Agregados en la propiedad onClick de cada boton, en el xml.
    fun saveOrUpdate(){
        if (etSubscriberName.value.isNullOrEmpty()){
            statusMessage.value = Event("Debes completar el campo Nombre del suscriptor ...")
        } else if (etSubscriberEmail.value.isNullOrEmpty()){
            statusMessage.value = Event("Debes completar el campo Email del suscriptor ...")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(etSubscriberEmail.value!!).matches()){
            statusMessage.value = Event("Debes ingresar un email válido")
        } else {
            if (isUpdateOrDelete){
                subscriberToUpdateOrDelete.name = etSubscriberName.value!!
                subscriberToUpdateOrDelete.email = etSubscriberEmail.value!!
                update(subscriberToUpdateOrDelete)
            } else {
                val name = etSubscriberName.value!!
                val email = etSubscriberEmail.value!!
                insert(Subscriber(0, name, email))
                etSubscriberName.value = null
                etSubscriberEmail.value = null
            }
        }

    }

    fun clearOrDelete(){
        if(isUpdateOrDelete){
            delete(subscriberToUpdateOrDelete)
        } else{
            clearAll()
        }

    }

    fun insert(subscriber: Subscriber){
        /*Como es una suspend fun, necesita ser llamada desde una Coroutine.
        * Por eso la envolvemos dentro de viewModelScope.launch*/
        viewModelScope.launch {
            repository.insert(subscriber)
            statusMessage.value = Event("Suscriptor agregado exitosamente")
        }

    }
    //Forma equivalente de escribir la misma sintaxis de la función de arriba
    fun update(subscriber: Subscriber): Job = viewModelScope.launch {
        repository.update(subscriber)
        etSubscriberName.value = ""
        etSubscriberEmail.value = ""
        isUpdateOrDelete = false
        saveOrUpdateButtonText.value = "Guardar"
        clearAllOrDeleteText.value = "Borrar todos"
        statusMessage.value = Event("Suscriptor actualizado exitosamente")
    }

    fun delete(subscriber: Subscriber): Job = viewModelScope.launch {
        repository.delete(subscriber)
        etSubscriberName.value = ""
        etSubscriberEmail.value = ""
        isUpdateOrDelete = false
        saveOrUpdateButtonText.value = "Guardar"
        clearAllOrDeleteText.value = "Borrar todos"
        statusMessage.value = Event("Suscriptor borrado exitosamente")
    }

    fun clearAll():Job = viewModelScope.launch {
        repository.deleteAll()
        statusMessage.value = Event("Todos los suscriptores borrados exitosamente")
    }

    fun initUpdateOrDelete(subscriber: Subscriber){
        etSubscriberName.value = subscriber.name
        etSubscriberEmail.value = subscriber.email
        isUpdateOrDelete = true
        subscriberToUpdateOrDelete = subscriber
        saveOrUpdateButtonText.value = "Editar"
        clearAllOrDeleteText.value = "Borrar"
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        //vacío, implementado por la necesidad de que el view model sea observado (interfaz Observable) por la vista.
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        //vacío, implementado por la necesidad de que el view model sea observado (interfaz Observable) por la vista.
    }

}