package com.moronlu18.database.dao

import androidx.room.Dao
import androidx.room.ForeignKey
import androidx.room.Insert
import androidx.room.Query
import com.moronlu18.data.account.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = ForeignKey.RESTRICT)
    fun insert(user: User) : Long //Si da un error devuelve un valor que es -1 como Long

    @Query("SELECT * FROM user")
    fun selectAll(): Flow<List<User>>

    //Debemos crear una funcion por cada eelemnto
}