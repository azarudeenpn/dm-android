package com.dm.client.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RequestDao {

    @Query("SELECT * FROM Request")
    fun getAll(): List<Request>

    @Query("DELETE FROM Request")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(vararg request: Request?)

}