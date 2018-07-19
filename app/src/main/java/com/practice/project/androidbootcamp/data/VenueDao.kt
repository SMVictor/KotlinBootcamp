package com.practice.project.androidbootcamp.data

import android.arch.persistence.room.*
import com.practice.project.androidbootcamp.model.Venue

@Dao
interface VenueDao {

    @get:Query("SELECT * FROM venue")
    val all: List<Venue>

    @Query("SELECT * FROM venue WHERE id LIKE :id LIMIT 1")
    fun findById(id: String): Venue

    @Insert
    fun insertAll(venues: List<Venue>)

    @Insert
    fun insert(venue: Venue): Long

    @Update
    fun update(venues: List<Venue>)

    @Delete
    fun delete(venues: List<Venue>)
}