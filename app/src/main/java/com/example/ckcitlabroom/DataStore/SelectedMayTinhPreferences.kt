/* SelectedMayTinhPerPhieuPreferences.kt */
package com.itlabroom.datastore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/* instance */
private val Context.dataStore by preferencesDataStore(name = "selected_may_per_phieu")

class SelectedMayTinhPerPhieuPreferences(private val context: Context) {

    /* ------ Helpers ------ */
    private fun key(phieuId: String): Preferences.Key<String> =
        stringPreferencesKey("ids_$phieuId")

    private fun String.encodeSet() = Json.encodeToString(this)
    private fun String?.decodeSet(): Set<String> =
        this?.let { Json.decodeFromString(it) } ?: emptySet()

    /* ------ API ------ */
    fun getIds(phieuId: String): Flow<Set<String>> =
        context.dataStore.data.map { pref -> pref[key(phieuId)].decodeSet() }

    suspend fun toggle(phieuId: String, id: String) = context.dataStore.edit { pref ->
        val curr = pref[key(phieuId)].decodeSet()
        pref[key(phieuId)] = Json.encodeToString(
            if (id in curr) curr - id else curr + id
        )
    }

    suspend fun getAllMayTinhIds(): Set<String> {
        val prefs = context.dataStore.data.first() // lấy snapshot Preferences hiện tại
        val allIds = mutableSetOf<String>()

        prefs.asMap().forEach { (key, value) ->
            // kiểm tra nếu key bắt đầu bằng "ids_" thì đó là một phiếu
            if (key.name.startsWith("ids_")) {
                val ids = (value as? String).decodeSet()
                allIds.addAll(ids)
            }
        }

        return allIds
    }

    suspend fun getAllMayTinhIdsExcept(phieuId: String): Set<String> {
        val prefs = context.dataStore.data.first()
        val allIds = mutableSetOf<String>()

        prefs.asMap().forEach { (key, value) ->
            if (key.name.startsWith("ids_") && key.name != "ids_$phieuId") {
                val ids = (value as? String).decodeSet()
                allIds.addAll(ids)
            }
        }

        return allIds
    }


    suspend fun clear(phieuId: String) = context.dataStore.edit { it.remove(key(phieuId)) }
}
