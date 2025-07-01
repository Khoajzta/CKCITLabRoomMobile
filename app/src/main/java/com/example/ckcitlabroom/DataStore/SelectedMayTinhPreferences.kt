/* SelectedMayTinhPerPhieuPreferences.kt */
package com.itlabroom.datastore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
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

    suspend fun clear(phieuId: String) = context.dataStore.edit { it.remove(key(phieuId)) }
}
