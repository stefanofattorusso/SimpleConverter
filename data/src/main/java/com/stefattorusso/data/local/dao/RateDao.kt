package com.stefattorusso.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.stefattorusso.data.local.entity.RateEntity
import io.reactivex.Flowable

@Dao
interface RateDao {

    @Insert(onConflict = REPLACE)
    fun save(rate: RateEntity)

    @Query("SELECT * FROM rates WHERE code = :code")
    fun load(code: String): Flowable<RateEntity>
}