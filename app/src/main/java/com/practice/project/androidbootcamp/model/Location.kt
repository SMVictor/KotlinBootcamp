package com.practice.project.androidbootcamp.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.Expose
import java.io.Serializable


@Entity
class Location : Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "location_id")
    var locationId: Long = 0
    @Expose
    var lat: Double = 0.toDouble()
    @Expose
    var lng: Double = 0.toDouble()
    var formattedAddressString: String? = null
    @Ignore
    @Expose
    var formattedAddress: List<String> = ArrayList()
}