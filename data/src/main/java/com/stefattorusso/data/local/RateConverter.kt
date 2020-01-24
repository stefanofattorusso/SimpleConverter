package com.stefattorusso.data.local

import androidx.room.TypeConverter
import org.json.JSONObject

class RateConverter {

    @TypeConverter
    fun convertMapToString(value: Map<String, Double>): String{
        return JSONObject(value).toString()
    }

    @TypeConverter
    fun convertStringToMap(value: String): Map<String, Double>{
        val map = mutableMapOf<String, Double>()
        val jsonObject = JSONObject(value)
        val keys = jsonObject.keys()
        while (keys.hasNext()){
            val key = keys.next()
            map[key] = jsonObject[key] as Double
        }
        return map
    }
}