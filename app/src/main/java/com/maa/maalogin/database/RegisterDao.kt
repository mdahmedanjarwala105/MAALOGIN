package com.maa.maalogin.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RegisterDao {
    @Insert
    fun insertBook(registerEntity: RegisterEntity)

    @Delete
    fun deleteBook(registerEntity: RegisterEntity)

    @Query("SELECT * FROM register")
    fun getAllBooks(): List<RegisterEntity>

    @Query("SELECT * FROM register WHERE mobileNumber = :mobileNumber")
    fun getBookById(mobileNumber: String): RegisterEntity
}