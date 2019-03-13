package com.dm.client.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Request(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "phone") var phone: String,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "place") var place: String,
    @ColumnInfo(name = "latitude") var latitude: Float,
    @ColumnInfo(name = "longitude") var longitude: Float,
    @ColumnInfo(name = "creationTime") var creationTime: String
)
