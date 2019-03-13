package com.dm.client.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RequestDao {

    @Query("SELECT * FROM Request")
    fun getAll(): List<Request>

    @Query("DELETE FROM Request")
    fun deleteAll()

    @Insert
    fun insert(vararg request: Request?)

}