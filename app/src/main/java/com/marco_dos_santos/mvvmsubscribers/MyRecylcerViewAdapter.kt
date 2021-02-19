package com.marco_dos_santos.mvvmsubscribers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.marco_dos_santos.mvvmsubscribers.databinding.ListItemBinding
import com.marco_dos_santos.mvvmsubscribers.db.Subscriber
import com.marco_dos_santos.mvvmsubscribers.generated.callback.OnClickListener
                                                                            //C
class MyRecylcerViewAdapter (private val subscribersList: List<Subscriber>, private val clickListener: (Subscriber)->Unit):
    RecyclerView.Adapter<MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)
        val binding : ListItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
                                                //E
        holder.bind(subscribersList[position], clickListener)
    }

    override fun getItemCount(): Int {
        return subscribersList.size
    }
}

class MyViewHolder(val binding: ListItemBinding): RecyclerView.ViewHolder(binding.root){
                                        //D
    fun bind (subscriber: Subscriber, clickListener: (Subscriber)->Unit){
        binding.nameTextView.text = subscriber.name
        binding.emailTextView.text = subscriber.email
        //F
        binding.listItemLayout.setOnClickListener{
            clickListener(subscriber)
        }
    }
}