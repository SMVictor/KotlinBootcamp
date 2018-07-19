package com.practice.project.androidbootcamp.model

import com.google.gson.annotations.Expose
import java.io.Serializable
import java.util.ArrayList


class Response : Serializable {
    @Expose
    var venues: List<Venue> = ArrayList()

}