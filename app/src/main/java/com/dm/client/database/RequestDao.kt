package com.dm.client.database

import androidx.room.Dao

@Dao
interface RequestDao {
    fun insert()
}