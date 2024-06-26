package com.moronlu18.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.ForeignKey.Companion.RESTRICT
import androidx.room.Insert
import androidx.room.Query
import com.moronlu18.data.account.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = RESTRICT)
    fun insert(user: User) : Long //Si da un error devuelve un valor que es -1 como Long

    @Query("SELECT * FROM user")
    fun selectAll(): Flow<List<User>>


    @Delete
    fun delete(user: User)
    //Debemos crear una funcion por cada eelemnto
}