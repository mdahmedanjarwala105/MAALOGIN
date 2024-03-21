package com.maa.maalogin.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RegisterEntity::class], version = 1)
abstract class RegisterDatabase : RoomDatabase() {
    abstract fun registerDao(): RegisterDao
}