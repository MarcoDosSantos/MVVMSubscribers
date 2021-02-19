package com.marco_dos_santos.mvvmsubscribers.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subscriber_data_table")
data class Subscriber(

    @ColumnInfo(name = "subscriber_id")
    @PrimaryKey(autoGenerate = true)
    var id : Int,

    @ColumnInfo(name = "subscriber_name")
    var name : String,

    @ColumnInfo(name = "subscriber_email")
    var email : String
) {
}