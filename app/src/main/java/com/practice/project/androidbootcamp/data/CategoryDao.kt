package com.practice.project.androidbootcamp.data

import android.arch.persistence.room.*
import com.practice.project.androidbootcamp.model.Category

@Dao
interface CategoryDao {

    @get:Query("SELECT * FROM category")
    val all: List<Category>

    @Query("SELECT * FROM category WHERE category_id LIKE :id LIMIT 1")
    fun findById(id: String): Category

    @Insert
    fun insertAll(categories: List<Category>)

    @Insert
    fun insert(category: Category): Long

    @Update
    fun update(categories: List<Category>)

    @Delete
    fun delete(categories: List<Category>)
}