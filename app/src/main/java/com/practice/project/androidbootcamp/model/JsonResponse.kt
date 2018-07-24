package com.practice.project.androidbootcamp.model

import com.google.gson.annotations.Expose
import java.io.Serializable

class JsonResponse : Serializable {

    @Expose
    var response: Response = Response()
}