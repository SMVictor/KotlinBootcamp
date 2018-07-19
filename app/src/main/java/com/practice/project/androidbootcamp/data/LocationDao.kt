package com.practice.project.androidbootcamp.data

import android.arch.persistence.room.*
import com.practice.project.androidbootcamp.model.Location

@Dao
interface LocationDao {

    @get:Query("SELECT * FROM location")
    val all: List<Location>

    @Query("SELECT * FROM location WHERE location_id LIKE :id LIMIT 1")
    fun findById(id: String): Location

    @Insert
    fun insertAll(locations: List<Location>)

    @Insert
    fun insert(location: Location): Long

    @Update
    fun update(locations: List<Location>)

    @Delete
    fun delete(locations: List<Location>)
}