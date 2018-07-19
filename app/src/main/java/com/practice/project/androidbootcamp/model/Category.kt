package com.practice.project.androidbootcamp.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.Expose
import java.io.Serializable


@Entity
class Category : Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "category_id")
    var categoryId: Long = 0
    @Expose
    var id: String? = null
    @Expose
    var name: String? = null
}