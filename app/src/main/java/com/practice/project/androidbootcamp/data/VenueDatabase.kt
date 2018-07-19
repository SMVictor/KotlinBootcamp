package com.practice.project.androidbootcamp.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.practice.project.androidbootcamp.model.Category
import com.practice.project.androidbootcamp.model.Location
import com.practice.project.androidbootcamp.model.Venue

@Database(entities = arrayOf(Category::class, Location::class, Venue::class), version = 1)
abstract class VenueDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun locationDao(): LocationDao
    abstract fun venueDao(): VenueDao
}