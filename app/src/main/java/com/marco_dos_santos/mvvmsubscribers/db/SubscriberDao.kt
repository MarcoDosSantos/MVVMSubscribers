package com.marco_dos_santos.mvvmsubscribers.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SubscriberDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubscriber (subscriber: Subscriber)//Coroutine : suspend fun

    @Update
    suspend fun updateSubscriber (subscriber: Subscriber)

    @Delete
    suspend fun deleteSubscriber (subscriber: Subscriber)

    @Query("SELECT * FROM subscriber_data_table")
    fun getAllSubscribers(): LiveData<List<Subscriber>>

    @Query("DELETE FROM subscriber_data_table")
    suspend fun deleteAll()
}
