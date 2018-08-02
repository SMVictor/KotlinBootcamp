package com.practice.project.androidbootcamp.utilities

import android.content.Context
import com.practice.project.androidbootcamp.R
import java.io.BufferedReader
import java.io.InputStreamReader

class FileUtilities(private val mContext: Context) {

    private val fourSquareFileData: MutableList<String> = ArrayList()

    init {
        readFourSquareDataFile()
    }

    private fun readFourSquareDataFile() {
        val `is` = mContext.resources.openRawResource(R.raw.four_square_data)
        val reader = BufferedReader(InputStreamReader(`is`!!))
        var data: String?

        do {
            data = reader.readLine()
            if (data == null)
                break
            fourSquareFileData.add(data)
        } while (true)
    }

    fun getFourSquareData(dataName: String): String {

        var value = ""

        for (data in fourSquareFileData) {
            if (data.contains(dataName)) {
                val split = data.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                value = split[1]
                break
            }
        }
        return value
    }
}