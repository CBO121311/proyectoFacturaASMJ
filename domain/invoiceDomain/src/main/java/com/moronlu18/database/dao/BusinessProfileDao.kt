package com.moronlu18.database.dao

import androidx.room.Dao
import androidx.room.ForeignKey
import androidx.room.Insert
import androidx.room.Query
import com.moronlu18.data.account.BusinessProfile


@Dao
interface BusinessProfileDao {
    @Insert(onConflict = ForeignKey.RESTRICT)
    fun insert(businessProfile: BusinessProfile):Long


    // @Query("SELECT * FROM businnessProfile WHERE id=businessProfileId")
    //Con =: coge el valor de la función que le pasemos como parametro.
    @Query("SELECT * FROM businessprofile WHERE id=:businessProfileId")
    fun selectBusinessProfile(businessProfileId: Int): BusinessProfile
}