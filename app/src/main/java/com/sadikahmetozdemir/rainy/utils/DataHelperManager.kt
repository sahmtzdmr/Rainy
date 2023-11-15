package com.sadikahmetozdemir.rainy.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataHelperManager @Inject constructor(private val context: Context) {
    suspend fun saveLatitude(latitude: String) {
        context.dataStore.edit {
            it[LATITUDE] = latitude
        }
    }

    suspend fun saveLongitude(longitude: String) {
        context.dataStore.edit {
            it[LONGITUDE] = longitude
        }
    }

    suspend fun getLatitude(): String = context.dataStore.data.map {
        it[LATITUDE] ?: "41"
    }.first()

    suspend fun getLongitude(): String = context.dataStore.data.map {
        it[LONGITUDE] ?: "28"
    }.first()

    suspend fun isFirstAttach(): Boolean = context.dataStore.data.map {
        it[ATTACH] ?: true
    }.first()

    suspend fun firstAttach() {
        context.dataStore.edit {
            it[ATTACH] = false
        }
    }

    companion object {
        private val LATITUDE = stringPreferencesKey("latitude")
        private val LONGITUDE = stringPreferencesKey("longitude")
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore("Data")
        private val ATTACH = booleanPreferencesKey("attach")

    }
}