package com.practice.project.androidbootcamp.model

import android.arch.persistence.room.*
import com.google.gson.annotations.Expose
import java.io.Serializable
import java.util.ArrayList


@Entity(foreignKeys = arrayOf(ForeignKey(entity = Category::class, parentColumns = arrayOf("category_id"), childColumns = arrayOf("category_id"), onDelete = ForeignKey.CASCADE), ForeignKey(entity = Location::class, parentColumns = arrayOf("location_id"), childColumns = arrayOf("location_id"), onDelete = ForeignKey.CASCADE)))
class Venue : Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "venue_id")
    var venueId: Long = 0
    @Expose
    var id: String? = null
    @Expose
    var name: String? = null
    @Ignore
    @Expose
    var location = Location()
    @Ignore
    @Expose
    var categories: MutableList<Category> = ArrayList()
    @ColumnInfo(name = "category_id")
    var categoryId: Long = 0
    @ColumnInfo(name = "location_id")
    var locationId: Long = 0
}