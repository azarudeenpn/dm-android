package com.dm.client.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Request::class], version = 1, exportSchema = false)
abstract class RequestDatabase: RoomDatabase() {
    abstract fun requestDao(): RequestDao

    companion object {
        private var INSTANCE: RequestDatabase? = null

        fun getInstance(context: Context): RequestDatabase?{
            if(INSTANCE == null){
                synchronized(RequestDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        RequestDatabase::class.java, "requests.db").build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance(){
            INSTANCE = null
        }
    }
}