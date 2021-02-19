package com.marco_dos_santos.mvvmsubscribers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.marco_dos_santos.mvvmsubscribers.databinding.ActivityMainBinding
import com.marco_dos_santos.mvvmsubscribers.db.Subscriber
import com.marco_dos_santos.mvvmsubscribers.db.SubscriberDatabase
import com.marco_dos_santos.mvvmsubscribers.db.SubscriberRepository

class MainActivity : AppCompatActivity() {

    //1) declaramos instancia de dataBinding
    private lateinit var binding: ActivityMainBinding

    // 3) declaramos instancia de viewModel con lateinit (depende de otros procesos)
    private lateinit var subscriberViewModel: SubscriberViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //2) inicializamos instancia de dataBinding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        /* 4) creamos una instancia del dao, no directamente desde la interfaz dao, sino desde
        el getInstance de la database.*/
        val dao = SubscriberDatabase.getInstance(application).subscriberDao

        /* 5)Creamos una instancia del Repository, y le pasamos el dao que acabamos de crear en el paso anterior*/
        val repository = SubscriberRepository(dao)

        /* 6) Creamos una instancia de la ViewModelFactory*/
        val factory = SubscriberViewModelFactory(repository)

        /* 7) Inicializamos la instancia de ViewModel que habíamos declarado en el paso 3).
        * Le pasamos los argumentos*/
        subscriberViewModel = ViewModelProvider(this, factory).get(SubscriberViewModel::class.java)
        //8)
        binding.myViewModel = subscriberViewModel
        //9) lifeCycleOwner es quién va a manejar los eventos asociados al ciclo de vida de la Activity.
        binding.lifecycleOwner = this
        // 11) Invocamos en el onCreate() el método que creamos en el paso anterior.
        initRecyclerView()

        subscriberViewModel.message.observe(this, Observer{
            it.getContentIfNotHandled()?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        })

    }
    // 10) Creamos el método que observa al viewModel, obtiene los datos y los despliega a la vista del usuario.
    private fun displaySubscribersList(){
        subscriberViewModel.subscribers.observe(this, Observer {
            Log.i("MYTAG", it.toString())
                                                                                //B, deapués vamos a Adapter.
            binding.subscriberRecyclerview.adapter = MyRecylcerViewAdapter(it, {selectedItem: Subscriber-> listItemclicked(selectedItem)})
        })
    }

    private fun initRecyclerView(){
        binding.subscriberRecyclerview.layoutManager = LinearLayoutManager(this)
        displaySubscribersList()
    }
    //A
    private fun listItemclicked(subscriber: Subscriber){
        //Toast.makeText(this, "Seleccionaste: ${subscriber.name}", Toast.LENGTH_LONG).show()
        subscriberViewModel.initUpdateOrDelete(subscriber)
    }
}